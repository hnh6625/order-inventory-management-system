# ADR-002: Tại sao chọn Modular Monolith?

## Status

Accepted

## Context

OIMS là hệ thống Order Management System phục vụ việc quản lý sản phẩm, tồn kho, đơn hàng và fulfillment cho một fashion brand bán hàng qua nhiều marketplace như Shopee và TikTok Shop.

Hệ thống có nhiều domain/business capability khác nhau:

* Catalog
* Inventory
* Ordering
* Channel Integration
* Fulfillment
* Security

Các domain này có trách nhiệm tương đối độc lập nhưng vẫn có nhiều mối quan hệ nghiệp vụ với nhau.

Ví dụ:

```text
Marketplace Webhook
        ↓
Channel Integration
        ↓
Ordering
        ↓
Inventory
        ↓
Fulfillment
```

OIMS cũng có các yêu cầu về transaction và consistency, đặc biệt trong quá trình xử lý order và inventory.

Các lựa chọn được cân nhắc:

| Option                | Đánh giá                                                                                                                                                                              |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Monolith không module | Đơn giản nhưng dễ tạo coupling giữa các domain và khó duy trì boundary rõ ràng khi project phát triển.                                                                                |
| Modular Monolith      | Giữ deployment đơn giản nhưng vẫn tổ chức hệ thống theo các module/domain boundary rõ ràng.                                                                                           |
| Microservices         | Có khả năng scale và deploy độc lập nhưng tạo thêm distributed-system complexity, networking, service discovery, deployment và monitoring overhead không cần thiết ở quy mô hiện tại. |

## Decision

Chọn **Modular Monolith** cho OIMS.

OIMS được triển khai dưới dạng một Spring Boot application duy nhất nhưng được chia thành các module theo business domain:

```text
catalog
inventory
ordering
channelintegration
fulfillment
security
shared
```

Mỗi module được tổ chức theo các layer phù hợp với Hexagonal Architecture:

```text
module
├── domain
├── application
└── infrastructure
```

Business logic được giữ trong domain/application layer, trong khi infrastructure chịu trách nhiệm giao tiếp với database, HTTP và các external system.

Việc lựa chọn Modular Monolith cho phép OIMS giữ được:

* Domain boundaries rõ ràng.
* Separation of concerns.
* Dễ phát triển và refactor.
* Một deployment unit đơn giản.
* Transactional consistency dễ quản lý hơn so với distributed transactions.
* Chi phí vận hành thấp hơn Microservices.

Trong tương lai, nếu một bounded context có nhu cầu scale hoặc deploy độc lập, module đó có thể được tách thành service riêng sau khi boundary đã được xác định rõ.

## Consequences

### (+) Ưu điểm

* Deployment đơn giản vì chỉ cần deploy một application.
* Không cần service discovery hoặc inter-service communication.
* Dễ quản lý transaction giữa các domain.
* Dễ debug và test toàn hệ thống.
* Giảm operational complexity so với Microservices.
* Domain boundary vẫn được thể hiện rõ thông qua module structure.
* Phù hợp với quy mô hiện tại của OIMS.
* Có khả năng evolve thành Microservices trong tương lai nếu business requirements thực sự cần.

### (-) Nhược điểm

* Các module vẫn chạy trong cùng một process.
* Không thể scale từng module độc lập ở mức infrastructure.
* Một deployment có thể ảnh hưởng đến toàn bộ hệ thống.
* Nếu boundary giữa các module không được giữ tốt, hệ thống có thể dần trở thành một monolith có coupling cao.
* Việc tách thành Microservices trong tương lai vẫn có thêm migration cost.

## Note

OIMS chọn Modular Monolith không phải vì Microservices không tốt, mà vì **độ phức tạp của Microservices chưa cần thiết đối với quy mô và yêu cầu hiện tại của hệ thống**.

Architecture được thiết kế theo hướng có thể tách các bounded context thành service độc lập trong tương lai nếu nhu cầu scale hoặc organizational requirements xuất hiện.
