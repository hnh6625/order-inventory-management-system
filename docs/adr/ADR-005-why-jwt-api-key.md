# ADR-005: Tại sao chọn JWT + API Key?

## Status

Accepted

## Context

OIMS có nhiều loại actor khác nhau tương tác với hệ thống.

Các internal users như:

- System Admin
- Operations Staff
- Warehouse Staff

cần đăng nhập vào hệ thống và được phân quyền dựa trên role.

Trong khi đó, các marketplace như Shopee và TikTok Shop không phải là người dùng nội bộ. Các hệ thống này gọi vào OIMS thông qua webhook.

Vì vậy OIMS có hai loại authentication khác nhau:

```text
Internal Users
      ↓
   Login
      ↓
     JWT
      ↓
   OIMS API


Marketplace
      ↓
   Webhook
      ↓
   API Key
      ↓
   OIMS API
```

Nếu chỉ sử dụng một cơ chế authentication cho tất cả trường hợp, thiết kế sẽ không phù hợp với đặc điểm của từng loại client.

Các lựa chọn được cân nhắc:

| Option | Đánh giá |
|---|---|
| Chỉ sử dụng JWT | Phù hợp với user authentication nhưng không cần thiết cho server-to-server webhook authentication. |
| Chỉ sử dụng API Key | Đơn giản nhưng không phù hợp với internal user authentication và role-based authorization. |
| JWT + API Key | JWT phù hợp cho internal users, API Key phù hợp cho marketplace webhooks. |

## Decision

Chọn **JWT + API Key** cho authentication của OIMS.

### JWT cho Internal Users

Internal users đăng nhập thông qua authentication endpoint.

Sau khi username và password được xác thực thành công, OIMS tạo JWT.

JWT chứa thông tin cần thiết để xác định user và role.

Ví dụ payload:

```json
{
  "sub": "admin",
  "role": "SYSTEM_ADMIN",
  "iat": 1234567890,
  "exp": 1234571490
}
```

Client gửi JWT trong mỗi request thông qua:

```text
Authorization: Bearer <JWT>
```

Spring Security sử dụng `JwtAuthenticationFilter` để:

1. Đọc JWT từ request.
2. Verify token.
3. Kiểm tra token hợp lệ.
4. Lấy thông tin user/role.
5. Tạo Authentication trong SecurityContext.
6. Cho phép Spring Security thực hiện authorization.

OIMS sử dụng các role:

```text
SYSTEM_ADMIN
OPERATIONS_STAFF
WAREHOUSE_STAFF
MARKETPLACE
```

Các API được phân quyền dựa trên role.

Ví dụ:

```text
/api/inventory/**
→ WAREHOUSE_STAFF
→ SYSTEM_ADMIN

/api/styles/**
→ SYSTEM_ADMIN

/api/orders/**
→ OPERATIONS_STAFF
→ SYSTEM_ADMIN

/api/fulfillment/**
→ OPERATIONS_STAFF
→ SYSTEM_ADMIN
```

### API Key cho Marketplace Webhooks

Shopee và TikTok Shop gửi order vào OIMS thông qua webhook.

Các webhook endpoint:

```text
POST /webhooks/shopee/orders

POST /webhooks/tiktokshop/orders
```

Marketplace gửi API Key thông qua HTTP header:

```text
X-API-Key: <API_KEY>
```

OIMS sử dụng `ApiKeyAuthenticationFilter` để:

1. Đọc `X-API-Key`.
2. Kiểm tra API Key.
3. Xác định marketplace tương ứng.
4. Tạo authentication với role `MARKETPLACE`.
5. Cho phép request tiếp tục nếu API Key hợp lệ.

Các webhook endpoint yêu cầu:

```text
ROLE_MARKETPLACE
```

Do đó request không có hoặc có API Key không hợp lệ sẽ không được phép truy cập webhook.

### Password Security

Password của internal users không được lưu dạng plaintext.

OIMS sử dụng BCrypt thông qua Spring Security:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Password được hash trước khi lưu vào database.

Khi login, hệ thống sử dụng password encoder để kiểm tra password được cung cấp với password hash đã lưu.

### Stateless Authentication

OIMS sử dụng stateless authentication:

```java
.sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

Server không lưu HTTP session cho authentication.

Mỗi request phải tự cung cấp thông tin authentication thông qua:

```text
Authorization: Bearer <JWT>
```

hoặc:

```text
X-API-Key: <API_KEY>
```

Điều này phù hợp với REST API và giúp application dễ scale hơn.

## Consequences

### (+) Ưu điểm

- JWT phù hợp với authentication của internal users.
- API Key đơn giản và phù hợp với server-to-server webhook.
- Có thể phân biệt rõ internal user và marketplace client.
- Hỗ trợ Role-Based Access Control.
- Authentication stateless.
- Không cần lưu HTTP session trên server.
- Password được bảo vệ bằng BCrypt.
- Có thể dễ dàng mở rộng thêm marketplace hoặc role trong tương lai.
- Phù hợp với kiến trúc REST API của OIMS.

### (-) Nhược điểm

- Hệ thống phải duy trì hai cơ chế authentication.
- Security flow phức tạp hơn so với chỉ sử dụng một cơ chế.
- JWT cần có expiration time và phải được verify ở mỗi request.
- API Key phải được bảo vệ và quản lý an toàn.
- Nếu API Key bị lộ, attacker có thể giả lập marketplace cho đến khi key bị revoke hoặc thay đổi.
- JWT có thể cần refresh/re-authentication khi hết hạn.

## Alternatives

### Chỉ sử dụng JWT

Có thể sử dụng JWT cho cả internal users và marketplace.

Tuy nhiên marketplace webhook không phải là user login flow.

Việc yêu cầu marketplace thực hiện JWT authentication sẽ làm server-to-server integration phức tạp hơn cần thiết.

Do đó OIMS sử dụng API Key cho webhook.

### Chỉ sử dụng API Key

Có thể cấp API Key cho từng internal user.

Tuy nhiên cách này không phù hợp với user authentication và role-based authorization.

Ngoài ra API Key không thể hiện tốt lifecycle của user như login, expiration và identity.

Do đó OIMS sử dụng JWT cho internal users.

## Note

JWT và API Key có vai trò khác nhau trong OIMS:

```text
JWT
→ Authentication cho internal users
→ Chứa user identity và role
→ Authorization dựa trên role


API Key
→ Authentication cho marketplace webhook
→ Server-to-server authentication
→ Xác định marketplace
```

API Key không được xem là user password và JWT cũng không được sử dụng để thay thế API Key cho webhook.

Trong production, API Key và JWT signing secret/private key cần được quản lý bằng secret management solution thay vì hard-code hoặc commit trực tiếp vào source code.

Nếu hệ thống phát triển thêm các external integrations khác, có thể cân nhắc OAuth2 hoặc các cơ chế authentication phù hợp với từng integration.

## Security Boundary

OIMS áp dụng security boundary như sau:

```text
                    OIMS
                     |
          +----------+----------+
          |                     |
   Internal Users          Marketplaces
          |                     |
         JWT                 API Key
          |                     |
          ↓                     ↓
   Role-based ACL        Webhook endpoints
```

Điều này giúp authentication mechanism phù hợp với từng loại client thay vì sử dụng một cơ chế duy nhất cho toàn bộ hệ thống.

