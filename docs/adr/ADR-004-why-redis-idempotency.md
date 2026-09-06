````markdown
# ADR-004: Tại sao chọn Redis + Database cho Idempotency?

## Status

Accepted

## Context

OIMS nhận order từ các marketplace thông qua webhook như Shopee và TikTok Shop.

Webhook có thể được gửi lại nhiều lần cho cùng một order.

Ví dụ:

```text
Shopee
   ↓
POST /webhooks/shopee/orders
   ↓
OIMS tạo Order
   ↓
Reserve Inventory
````

Sau đó vì network timeout hoặc marketplace retry:

```text
Shopee
   ↓
POST /webhooks/shopee/orders
   ↓
OIMS nhận lại cùng order
```

Nếu OIMS xử lý request thứ hai như một order mới, hệ thống có thể:

* tạo duplicate order
* reserve inventory nhiều lần
* làm sai số lượng tồn kho
* gây overselling
* tạo dữ liệu không nhất quán

Vì vậy webhook processing phải có tính chất **Idempotent**.

Một operation được gọi là idempotent khi việc thực hiện operation nhiều lần với cùng input vẫn tạo ra cùng một kết quả cuối cùng như thực hiện một lần.

OIMS sử dụng `marketplaceOrderId` kết hợp với `channel` để xác định một webhook/order đã được xử lý hay chưa.

Các lựa chọn được cân nhắc:

| Option                                | Đánh giá                                                                                                               |
| ------------------------------------- | ---------------------------------------------------------------------------------------------------------------------- |
| Chỉ kiểm tra trong application memory | Không phù hợp vì dữ liệu mất khi application restart và không hoạt động tốt nếu có nhiều application instances.        |
| Chỉ sử dụng Redis                     | Nhanh nhưng dữ liệu có TTL và có thể bị mất. Không phù hợp làm source of truth cho trạng thái đã xử lý.                |
| Chỉ sử dụng Database                  | Đảm bảo persistence nhưng mỗi webhook đều phải truy vấn database, không tối ưu cho các duplicate request thường xuyên. |
| Redis + Database                      | Redis dùng làm fast cache, Database giữ persistent record và source of truth. Phù hợp với OIMS.                        |

## Decision

Chọn **Redis + Database** để xử lý webhook idempotency.

OIMS sử dụng hai tầng:

```text
Webhook
   ↓
Redis
   ↓
Database
   ↓
Order Processing
```

### Redis

Redis được sử dụng như một **fast idempotency cache**.

Key được tạo từ:

```text
webhook:{channel}:{marketplaceOrderId}
```

Ví dụ:

```text
webhook:SHOPEE:ORDER-123
```

Redis entry có TTL để tránh việc cache tồn tại vô thời hạn.

Trong OIMS, Redis cache được sử dụng để nhanh chóng phát hiện những webhook đã được xử lý gần đây mà không cần truy vấn database trong mọi request duplicate.

### Database

Database lưu `ProcessedWebhook` để ghi nhận webhook đã được xử lý.

Record bao gồm thông tin như:

```text
marketplaceOrderId
channel
```

Database là **source of truth** cho trạng thái processed.

Điều này đảm bảo trạng thái idempotency vẫn tồn tại ngay cả khi:

* Redis restart.
* Redis cache entry hết TTL.
* Application restart.

### Processing flow

Khi nhận webhook:

```text
1. Nhận webhook
        ↓
2. Tạo Redis key
        ↓
3. Kiểm tra Redis
        ↓
4. Nếu Redis có key
        → Order đã được xử lý
        ↓
5. Nếu Redis không có key
        ↓
6. Kiểm tra ProcessedWebhook trong Database
        ↓
7. Nếu Database đã có record
        → Order đã được xử lý
        ↓
8. Nếu chưa có
        ↓
9. Process Order
        ↓
10. Lưu ProcessedWebhook
        ↓
11. Lưu Redis key với TTL
```

OIMS hiện tại sử dụng:

```java
redisService.get(redisKey);
```

để kiểm tra Redis cache và:

```java
redisService.setIfAbsent(
    redisKey,
    "processed",
    Duration.ofHours(1)
);
```

để lưu trạng thái processed vào Redis.

Database vẫn được kiểm tra thông qua:

```java
processedWebhookRepository
    .existsByMarketplaceOrderIdAndChannel(
        marketplaceOrderId,
        channel
    );
```

Điều này tạo ra kiến trúc:

```text
Redis
Fast cache
   +
Database
Persistent source of truth
```

## Consequences

### (+) Ưu điểm

* Duplicate webhook có thể được xử lý nhanh bằng Redis.
* Tránh tạo duplicate order.
* Giảm nguy cơ reserve inventory nhiều lần.
* Database vẫn giữ persistent record của webhook đã xử lý.
* Redis có TTL nên cache không tăng vô hạn.
* Hệ thống vẫn có thể kiểm tra trạng thái processed sau khi Redis cache hết hạn.
* Phù hợp với webhook retry từ marketplace.
* Có thể mở rộng tốt hơn khi số lượng webhook tăng.

### (-) Nhược điểm

* Hệ thống phải quản lý thêm Redis.
* Có thêm một dependency ngoài Database.
* Logic xử lý idempotency phức tạp hơn so với chỉ sử dụng Database.
* Redis cache và Database có thể tạm thời không đồng bộ.
* Cần thiết kế transaction và concurrency cẩn thận để tránh race condition.

## Alternatives

### Chỉ sử dụng Database

Có thể chỉ lưu `ProcessedWebhook` trong Database và kiểm tra record trước khi xử lý webhook.

Ưu điểm:

* Kiến trúc đơn giản.
* Database là source of truth duy nhất.
* Không cần Redis.

Nhược điểm:

* Mỗi duplicate webhook đều phải query Database.
* Tăng database load khi marketplace retry nhiều webhook.

OIMS vẫn sử dụng Database làm source of truth nhưng bổ sung Redis để tối ưu các duplicate request thường xuyên.

### Chỉ sử dụng Redis

Không phù hợp vì Redis được sử dụng như cache và key có TTL.

Nếu Redis key hết hạn hoặc Redis mất dữ liệu, hệ thống có thể không còn biết webhook đã được xử lý.

Do đó Redis không được xem là source of truth.

## Note

Redis trong OIMS **không thay thế Database**.

Vai trò của hai thành phần khác nhau:

```text
Redis
→ Fast cache
→ TTL
→ Giảm database query

Database
→ Persistent record
→ Source of truth
→ Audit/idempotency history
```

Ngoài ra, việc kiểm tra Redis rồi mới kiểm tra Database không tự nó đảm bảo tuyệt đối rằng hai request concurrent sẽ không cùng xử lý một webhook.

Database persistent record và unique constraint vẫn đóng vai trò safeguard quan trọng.

Nếu hệ thống phát triển với concurrency rất cao, có thể cải thiện thêm bằng:

* Atomic Redis operation.
* Distributed lock.
* Database unique constraint.
* Transaction retry.
* Message queue.

Các cơ chế này chỉ nên được bổ sung khi workload thực tế yêu cầu.
