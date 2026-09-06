````markdown
# ADR-003: Tại sao chọn Optimistic Locking?

## Status

Accepted

## Context

OIMS phải xử lý nhiều order có thể được tạo đồng thời từ các marketplace như Shopee và TikTok Shop.

Một vấn đề quan trọng là nhiều request có thể cùng lúc reserve cùng một SKU.

Ví dụ:

Stock ban đầu:

10 sản phẩm

Request A:
- đọc stock = 10
- reserve 1
- ghi stock = 9

Request B:
- đọc stock = 10
- reserve 1
- ghi stock = 9

Nếu không có cơ chế kiểm soát concurrency, cả hai request đều có thể thành công nhưng kết quả cuối cùng chỉ còn 9 sản phẩm.

Đây là vấn đề **Lost Update** và có thể dẫn đến trạng thái inventory không chính xác.

Trong trường hợp nghiêm trọng hơn, nhiều request đồng thời có thể khiến hệ thống bán vượt quá số lượng tồn thực tế (**overselling**).

OIMS cần một cơ chế đảm bảo rằng hai transaction không thể âm thầm ghi đè lên thay đổi của nhau.

Các lựa chọn được cân nhắc:

| Option | Đánh giá |
|---|---|
| Không locking | Đơn giản nhưng có thể xảy ra Lost Update và overselling. |
| Pessimistic Locking | Lock database row trong transaction, đảm bảo consistency nhưng có thể làm tăng contention và thời gian transaction phải chờ. |
| Optimistic Locking | Cho phép các transaction hoạt động bình thường nhưng phát hiện conflict khi update. Phù hợp với trường hợp contention không xảy ra liên tục. |

## Decision

Chọn **Optimistic Locking** cho inventory của OIMS.

OIMS sử dụng JPA `@Version` trên `StockItemJpaEntity`:

```java
@Version
private Long version;
````

Hibernate sẽ sử dụng trường `version` để phát hiện concurrent update.

Ví dụ:

Ban đầu:

```text
quantity = 10
version = 1
```

Transaction A đọc:

```text
quantity = 10
version = 1
```

Transaction B cũng đọc:

```text
quantity = 10
version = 1
```

Transaction A update thành công:

```text
quantity = 9
version = 2
```

Transaction B cố gắng update dựa trên:

```text
version = 1
```

Nhưng database hiện tại đã là:

```text
version = 2
```

Hibernate phát hiện conflict và transaction B không thể ghi đè thay đổi của transaction A.

Điều này giúp bảo vệ inventory khỏi Lost Update khi nhiều request cùng thao tác trên một SKU.

Các inventory operations như:

* reserve
* release
* restock

được thực hiện trong transaction và sử dụng optimistic locking thông qua `@Version`.

OIMS cũng có integration test để kiểm tra concurrent reservation.

Ví dụ:

```text
Initial stock = 10
Concurrent requests = 20
Each request reserves = 1
```

Kết quả hợp lệ phải đảm bảo:

```text
successful reservations + final stock = initial stock
```

Ví dụ một test run:

```text
Success = 3
Failed = 17
Final stock = 7
```

Do đó:

```text
3 + 7 = 10
```

Inventory vẫn nhất quán và không xảy ra overselling.

## Consequences

### (+) Ưu điểm

* Phát hiện concurrent update thay vì âm thầm ghi đè dữ liệu.
* Giúp bảo vệ inventory khỏi Lost Update.
* Giảm nguy cơ overselling.
* Không cần giữ database lock trong toàn bộ thời gian xử lý business logic.
* Phù hợp với hệ thống mà concurrent conflict có thể xảy ra nhưng không phải lúc nào cũng xảy ra.
* Dễ tích hợp với JPA/Hibernate thông qua `@Version`.
* Giữ transaction tương đối nhẹ so với việc sử dụng pessimistic locking cho mọi request.

### (-) Nhược điểm

* Khi xảy ra conflict, transaction có thể fail với optimistic locking exception.
* Application có thể cần retry transaction trong một số trường hợp.
* Khi contention trên cùng một SKU cực kỳ cao, optimistic locking có thể dẫn đến nhiều transaction retry/fail.
* Developer phải hiểu rõ transaction boundary và cách xử lý optimistic locking exception.

## Alternatives

### Không sử dụng locking

Không phù hợp vì concurrent requests có thể đọc cùng một inventory value và ghi đè lên nhau.

Điều này có thể gây:

* Lost Update
* Inventory không chính xác
* Overselling

### Pessimistic Locking

Pessimistic locking có thể được sử dụng để lock row inventory trong transaction.

Ưu điểm:

* Conflict được ngăn chặn ngay từ đầu.
* Phù hợp với workload có contention rất cao.

Nhược điểm:

* Transaction có thể phải chờ lock.
* Có thể làm giảm concurrency.
* Có thể tăng nguy cơ lock contention và ảnh hưởng performance nếu transaction giữ lock quá lâu.

OIMS hiện tại chưa cần mức locking mạnh này vì workload và contention chưa đủ cao để justify thêm database locking overhead.

## Note

Optimistic locking không có nghĩa là hệ thống sẽ không bao giờ có concurrent conflict.

Ngược lại, optimistic locking chấp nhận rằng conflict có thể xảy ra và **phát hiện conflict khi update**, thay vì khóa resource ngay từ đầu.

Nếu trong tương lai inventory có contention rất cao hoặc workload thay đổi đáng kể, có thể cân nhắc:

* Pessimistic Locking
* Retry mechanism
* Queue-based inventory processing
* Distributed locking

Việc lựa chọn cơ chế khác cần dựa trên workload thực tế và performance requirements.
