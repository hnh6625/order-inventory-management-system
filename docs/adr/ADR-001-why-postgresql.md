# ADR-001: Tại sao chọn PostgreSQL?

## Status

Accepted

## Context

OIMS cần một hệ quản trị cơ sở dữ liệu quan hệ (RDBMS) để lưu trữ các dữ liệu có quan hệ chặt chẽ với nhau, ví dụ:

* Style → Variant → SKU
* Order → OrderLine
* Order → Shipment
* ProcessedWebhook → thông tin idempotency của webhook

Ngoài việc lưu trữ dữ liệu, hệ thống còn có các yêu cầu quan trọng:

* Đảm bảo ACID transaction.
* Đảm bảo tính toàn vẹn dữ liệu thông qua database constraints.
* Hỗ trợ xử lý concurrency cho inventory.
* Hỗ trợ các cơ chế locking ở tầng database khi cần.
* Hỗ trợ UUID làm primary key.
* Có thể chạy ổn định trong Docker và phù hợp với Integration Test bằng Testcontainers.

Các lựa chọn được cân nhắc:

| Option  | Đánh giá                                                                                                                             |
| ------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| MySQL   | Có thể đáp ứng use case, nhưng PostgreSQL phù hợp hơn với các yêu cầu và thiết kế hiện tại của project.                              |
| H2      | Phù hợp cho Unit Test hoặc test đơn giản, nhưng không được sử dụng làm production database của OIMS.                                 |
| MongoDB | Có thể sử dụng trong một số use case, nhưng mô hình dữ liệu của OIMS có nhiều quan hệ và transaction nên RDBMS phù hợp tự nhiên hơn. |

## Decision

Chọn **PostgreSQL 16** làm database chính của OIMS.

PostgreSQL được chọn vì phù hợp với mô hình dữ liệu quan hệ của hệ thống, transaction, database constraints, UUID và các yêu cầu về concurrency.

Việc sử dụng `@Version` cho Optimistic Locking là cơ chế của JPA/Hibernate. PostgreSQL cung cấp transaction và update semantics ở database layer để cơ chế này hoạt động đúng.

PostgreSQL cũng hỗ trợ các cơ chế row-level locking như `SELECT FOR UPDATE`, có thể được sử dụng nếu hệ thống cần chuyển sang Pessimistic Locking trong tương lai.

## Consequences

### (+) Ưu điểm

* Phù hợp với dữ liệu quan hệ của OIMS.
* Hỗ trợ ACID transaction.
* Hỗ trợ database constraints để bảo vệ tính toàn vẹn dữ liệu.
* Hỗ trợ UUID native.
* Hỗ trợ row-level locking.
* Phù hợp với cơ chế Optimistic Locking thông qua JPA/Hibernate `@Version`.
* Chạy dễ dàng bằng Docker.
* Phù hợp với Integration Test sử dụng Testcontainers.
* Là một RDBMS phổ biến trong các hệ thống backend production.

### (-) Nhược điểm

* Nặng hơn database embedded như H2 đối với các test đơn giản.
* Integration Test với PostgreSQL/Testcontainers có thời gian khởi động lâu hơn Unit Test.
* Cần quản lý database riêng trong môi trường production.
* Việc sử dụng PostgreSQL làm tăng một phần operational complexity so với database embedded.

## Note

ADR này là nền tảng cho các quyết định liên quan đến:

* **ADR-003:** Optimistic Locking cho Inventory.
* **ADR-004:** Persistence của Idempotency bằng database kết hợp Redis cache.

PostgreSQL là database chính và là source of truth cho dữ liệu nghiệp vụ của OIMS.
