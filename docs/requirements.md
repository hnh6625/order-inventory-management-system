# Business Requirements — MOC Order Management System (MOC-OMS)
## MOC — Fashion Brand bán hàng qua nhiều sàn Thương mại điện tử

> **Bối cảnh dự án**: **MOC** là fashion brand streetwear bán qua 2 sàn TMĐT **Shopee** và **TikTok Shop**. Sản phẩm có biến thể (size + màu). Đồng bộ sản phẩm 1 chiều (hệ thống nội bộ → sàn). Đơn hàng từ sàn gửi vào hệ thống qua webhook giả lập. Vận chuyển: Marketplace Managed (sàn tự lo) hoặc Self-arranged (nhân viên tự đặt Bee/Grab). Đây là **dự án portfolio CV** mô phỏng hệ thống OMS nội bộ thực tế.

---

## 1. Business Requirement Document (BRD)

### 1.1. Bối cảnh
**MOC** là fashion brand streetwear đang bán hàng trên nhiều sàn TMĐT (Shopee, TikTok Shop). Mỗi sản phẩm có nhiều **biến thể (variant)** theo size (S/M/L/XL) và màu sắc — mỗi combination size+màu là 1 SKU riêng biệt với tồn kho độc lập. Vì tồn kho vật lý **dùng chung** cho mọi sàn (cùng 1 kho hàng), việc không đồng bộ real-time dẫn đến tình trạng **oversell đa kênh**: khách trên Shopee và khách trên TikTok Shop có thể cùng mua variant cuối cùng gần như đồng thời, dẫn đến nhận đơn nhưng không còn hàng để giao. Ngoài ra, nhân viên vận hành phải vào từng sàn để xác nhận đơn, đóng gói, theo dõi vận chuyển — tốn thời gian và dễ sai sót.

### 1.2. Mục tiêu hệ thống
- Quản lý sản phẩm theo **Style** (mẫu thiết kế) và **Variant** (biến thể size+màu), mỗi Variant có SKU và tồn kho riêng.
- Tập trung tồn kho và đơn hàng từ nhiều sàn về 1 hệ thống nội bộ duy nhất.
- Đảm bảo tồn kho không bao giờ bị bán vượt (oversell) dù đơn đến từ nhiều sàn gần như đồng thời cho cùng 1 SKU variant.
- Tự động đồng bộ sản phẩm (Style, Variant, giá, tồn kho) từ hệ thống nội bộ lên các sàn.
- Tự động đẩy đơn hàng đã xác nhận sang đơn vị vận chuyển, theo dõi trạng thái giao hàng.
- Cho phép nhân viên vận hành xem và xử lý mọi đơn hàng từ tất cả các sàn ở 1 nơi duy nhất.

### 1.3. Đối tượng sử dụng (Actors)

| Actor | Loại | Mô tả | Mục tiêu khi tương tác hệ thống |
|---|---|---|---|
| **Marketplace** (Shopee, TikTok Shop) | Hệ thống ngoài | Gửi đơn hàng vào hệ thống qua webhook khi có khách mua trên sàn | Đơn hàng được xác nhận hoặc từ chối nhanh, chính xác tồn kho |
| **Shipping Carrier** (qua webhook, ví dụ logistics riêng của Marketplace) | Hệ thống ngoài | Gửi cập nhật trạng thái giao hàng cho đơn `MARKETPLACE_MANAGED` | Gửi callback trạng thái, không cần hệ thống nội bộ gọi ngược |
| **Operations Staff** | Người dùng nội bộ | Xem đơn hàng từ mọi sàn, xác nhận đóng gói, xử lý đơn lỗi/hủy | Xử lý đơn hàng nhanh, không phải vào nhiều sàn riêng lẻ |
| **Warehouse Staff** | Người dùng nội bộ | Nhập kho, kiểm tồn kho thực tế, xem cảnh báo sắp hết hàng | Tồn kho hệ thống khớp tồn kho thực tế |
| **System Admin** | Người dùng nội bộ | Quản lý sản phẩm gốc, cấu hình kết nối sàn (API Key), quản lý user/role nội bộ | Toàn quyền vận hành và cấu hình hệ thống |

> Lưu ý: hệ thống này **không có Customer (khách mua hàng cuối) là người dùng trực tiếp** — khách mua hàng trên sàn TMĐT, không vào hệ thống nội bộ.

### 1.4. Phạm vi

**Trong phạm vi:**
- Đồng bộ sản phẩm 1 chiều: hệ thống nội bộ → sàn (tên, giá, tồn kho).
- Nhận đơn hàng từ sàn qua webhook (giả lập).
- Quản lý tồn kho tập trung, đảm bảo không oversell đa kênh.
- Quản lý trạng thái đơn hàng từ lúc nhận đến khi giao xong.
- Đẩy đơn cho đơn vị vận chuyển (giả lập), nhận cập nhật trạng thái giao hàng qua webhook.
- Phân quyền nội bộ (Operations Staff / Warehouse Staff / System Admin) + xác thực cho hệ thống ngoài (Marketplace, Shipping Carrier).

**Ngoài phạm vi:**
- Đồng bộ 2 chiều (sàn → nội bộ) cho thông tin sản phẩm (sàn không được tự sửa giá/tên sản phẩm).
- Thanh toán thật (giả định sàn TMĐT đã xử lý thanh toán trước khi gửi đơn vào hệ thống).
- Tích hợp API thật của Shopee/TikTok Shop/đơn vị vận chuyển logistics riêng của sàn (dự án dùng webhook giả lập để mô phỏng hành vi).
- **Tích hợp API Bee/Grab**: nhân viên tự đặt vận chuyển bằng app riêng trên điện thoại, hệ thống chỉ ghi nhận thông tin bằng tay (xem FR-15 đến FR-18), không gọi API của Bee/Grab.
- Multi-warehouse (nhiều kho vật lý) — giả định 1 kho duy nhất.
- Multi-currency, đa ngôn ngữ.

### 1.5. Lợi ích kỳ vọng
- Loại bỏ tình trạng oversell đa kênh.
- Giảm thời gian xử lý đơn hàng do tập trung về 1 hệ thống.
- Có dữ liệu tồn kho và đơn hàng chính xác, real-time, làm nền cho báo cáo sau này.

---

## 2. Functional Requirements (FR)

| Mã | Yêu cầu | Actor liên quan | Độ ưu tiên |
|---|---|---|---|
| FR-01 | System Admin tạo, sửa, xem danh sách **Style** (mẫu thiết kế: tên, mô tả, danh mục) | System Admin | High |
| FR-02 | System Admin tạo, sửa **Variant** cho mỗi Style (size + màu + giá) — SKU **tự động sinh** theo quy tắc `MOC-{StyleCode}-{Size}-{Color}`, không nhập tay | System Admin | High |
| FR-03 | Hệ thống tự động đồng bộ Style/Variant (tên, giá, tồn kho) lên từng sàn đã kết nối khi có thay đổi | Marketplace | High |
| FR-04 | Warehouse Staff nhập thêm tồn kho (restock) theo từng **SKU Variant** cụ thể | Warehouse Staff | High |
| FR-05 | Hệ thống nhận đơn hàng mới từ Marketplace qua webhook, kèm thông tin SKU Variant + số lượng + mã đơn của sàn | Marketplace | High |
| FR-06 | Hệ thống phải reserve tồn kho **theo từng SKU Variant** và xác nhận/từ chối đơn ngay khi nhận webhook, đảm bảo không bán vượt tồn kho dù nhiều sàn gửi đơn gần như đồng thời cho cùng 1 SKU Variant | Marketplace | **High — yêu cầu kỹ thuật quan trọng nhất** |
| FR-07 | Nếu đơn bị từ chối do hết hàng, hệ thống gửi callback từ chối về đúng Marketplace đã gửi đơn | Marketplace | High |
| FR-08 | Operations Staff xem danh sách đơn hàng từ mọi sàn, lọc theo trạng thái, theo sàn | Operations Staff | High |
| FR-09 | Operations Staff xác nhận đóng gói đơn hàng và chọn FulfillmentType | Operations Staff | High |
| FR-10 | Hệ thống nhận cập nhật trạng thái giao hàng từ Shipping Carrier qua webhook (đã lấy hàng, đang giao, giao thành công, giao thất bại) | Shipping Carrier | High |
| FR-11 | Hệ thống hoàn lại tồn kho khi đơn bị hủy hoặc giao thất bại hoàn toàn | Operations Staff | High |
| FR-12 | Operations Staff hủy đơn khi đơn chưa được đẩy cho Shipping Carrier | Operations Staff | Medium |
| FR-13 | Warehouse Staff nhận cảnh báo khi tồn kho 1 SKU Variant xuống dưới ngưỡng tối thiểu | Warehouse Staff | Medium |
| FR-14 | System Admin quản lý kết nối sàn (thêm/xóa sàn, cấu hình API Key cho từng sàn) | System Admin | Medium |
| FR-15 | System Admin quản lý user/role nội bộ (Operations Staff, Warehouse Staff) | System Admin | Medium |
| FR-16 | Operations Staff chọn loại vận chuyển cho đơn: `MARKETPLACE_MANAGED` hoặc `SELF_ARRANGED` (nhân viên tự đặt qua app Bee/Grab) | Operations Staff | High |
| FR-17 | Với đơn `SELF_ARRANGED`, Operations Staff tự nhập tay thông tin vận chuyển (tên đơn vị, mã vận đơn nếu có) | Operations Staff | High |
| FR-18 | Với đơn `SELF_ARRANGED`, Operations Staff tự cập nhật tay trạng thái giao hàng sau khi theo dõi qua app Bee/Grab | Operations Staff | High |
| FR-19 | Với đơn `MARKETPLACE_MANAGED`, hệ thống chỉ đánh dấu `READY_FOR_PICKUP` và chờ webhook cập nhật trạng thái từ Marketplace | Operations Staff | High |

### 2.1. Non-Functional Requirements (NFR)

| Mã | Yêu cầu | Loại |
|---|---|---|
| NFR-01 | Hệ thống phải đảm bảo tồn kho không âm dù nhận nhiều webhook đặt hàng đồng thời từ các sàn khác nhau cho cùng 1 SKU | Reliability/Concurrency |
| NFR-02 | Webhook nhận đơn từ Marketplace phải có cơ chế chống xử lý trùng (idempotency) — nếu sàn gửi lại webhook do timeout, hệ thống không tạo đơn trùng | Reliability |
| NFR-03 | Mọi request từ Marketplace/Shipping Carrier phải được xác thực (API Key hoặc OAuth2 Client Credentials), không nhận webhook ẩn danh | Security |
| NFR-04 | Mọi hành động thay đổi tồn kho/đơn hàng phải ghi lại được ai/hệ thống nào thực hiện, lúc nào (audit trail) | Auditability |
| NFR-05 | API nội bộ phải phản hồi trong thời gian hợp lý để không làm timeout webhook từ sàn | Performance |

---

## 3. Use Case Diagram (mô tả văn bản)

**Marketplace** →
- Send New Order (webhook)
- Receive Order Confirmation/Rejection (callback)

**Shipping Carrier** →
- Send Delivery Status Update (webhook)

**Operations Staff** →
- View Orders (multi-channel)
- Confirm & Pack Order
- Cancel Order
- Select Fulfillment Type (Marketplace Managed / Self-arranged)
- Record Self-arranged Shipment (manual)
- Manually Update Delivery Status (for Self-arranged orders)

**Warehouse Staff** →
- Restock Inventory
- View Low-stock Alerts

**System Admin** →
- Manage Products
- Manage Marketplace Connections
- Manage Users/Roles

---

## 4. Use Case Specification chi tiết

### UC-01: Receive Order from Marketplace
```
Actor: Marketplace
Precondition: Marketplace đã kết nối hệ thống (có API Key hợp lệ)
Main Flow:
  1. Marketplace gửi webhook đơn hàng mới: marketplace_order_id, danh sách SKU + số lượng
  2. Hệ thống xác thực API Key của Marketplace
  3. Hệ thống kiểm tra idempotency (đã xử lý marketplace_order_id này chưa)
  4. Hệ thống reserve tồn kho cho từng SKU trong đơn (có cơ chế chống race condition
     khi nhiều webhook đến đồng thời cho cùng SKU)
  5. Hệ thống tạo đơn nội bộ với trạng thái RESERVED, gắn nguồn gốc = Marketplace nào
  6. Hệ thống gửi callback xác nhận về Marketplace
Alternative Flow:
  2a. API Key không hợp lệ → trả lỗi 401, không xử lý đơn
  3a. marketplace_order_id đã xử lý trước đó → trả lại kết quả lần xử lý trước (idempotent),
      không tạo đơn mới
  4a. Một SKU không đủ tồn kho (kể cả khi 2 webhook từ 2 sàn khác nhau đến cùng lúc
      cho cùng SKU) → hệ thống đảm bảo chỉ 1 trong các webhook reserve thành công,
      các đơn còn lại nhận callback từ chối với lý do "INSUFFICIENT_STOCK"
Postcondition: Đơn được tạo RESERVED và tồn kho trừ đúng, hoặc đơn bị từ chối và
               tồn kho không bị ảnh hưởng.
```

### UC-02: Confirm & Pack Order (chọn phương thức vận chuyển)
```
Actor: Operations Staff
Precondition: Đơn đang ở trạng thái RESERVED hoặc CONFIRMED
Main Flow:
  1. Operations Staff xem chi tiết đơn, xác nhận đã đóng gói
  2. Hệ thống chuyển trạng thái đơn sang PACKED
  3. Operations Staff chọn FulfillmentType cho đơn: MARKETPLACE_MANAGED hoặc SELF_ARRANGED
  4a. Nếu MARKETPLACE_MANAGED: hệ thống chuyển trạng thái sang READY_FOR_PICKUP,
      chờ webhook từ Marketplace cập nhật tiếp (xem UC-03)
  4b. Nếu SELF_ARRANGED: chuyển sang UC-06 (Record Self-arranged Shipment)
Alternative Flow:
  3a. Đơn đến từ Marketplace có chính sách bắt buộc dùng logistics riêng của sàn
      (ví dụ 1 số chương trình của Shopee) → hệ thống không cho chọn SELF_ARRANGED,
      tự động gán MARKETPLACE_MANAGED
Postcondition: Đơn có FulfillmentType xác định, ở trạng thái tương ứng để xử lý tiếp.
```

### UC-06: Record Self-arranged Shipment (mới)
```
Actor: Operations Staff
Precondition: Đơn đã PACKED, đã chọn FulfillmentType = SELF_ARRANGED
Main Flow:
  1. Operations Staff đặt vận chuyển bằng app riêng của Bee/Grab trên điện thoại (ngoài hệ thống)
  2. Operations Staff vào hệ thống, nhập tay: tên đơn vị vận chuyển (Bee/Grab), mã vận đơn
     nếu có, thời gian dự kiến lấy hàng
  3. Hệ thống chuyển trạng thái đơn sang SHIPPED (ghi nhận đã giao cho bên vận chuyển)
Alternative Flow:
  2a. Operations Staff để trống mã vận đơn (một số app không cung cấp mã) → hệ thống
      vẫn cho phép lưu, không bắt buộc trường này
Postcondition: Đơn ở trạng thái SHIPPED, có thông tin vận chuyển ghi nhận thủ công,
               hệ thống KHÔNG tự động theo dõi trạng thái giao hàng tiếp theo.
```

### UC-07: Manually Update Delivery Status (mới — chỉ áp dụng cho SELF_ARRANGED)
```
Actor: Operations Staff
Precondition: Đơn ở trạng thái SHIPPED với FulfillmentType = SELF_ARRANGED
Main Flow:
  1. Operations Staff tự theo dõi trạng thái giao hàng qua app Bee/Grab (ngoài hệ thống)
  2. Khi thấy đã giao thành công, Operations Staff vào hệ thống bấm xác nhận "Đã giao"
  3. Hệ thống chuyển trạng thái đơn sang DELIVERED
Alternative Flow:
  2a. Nếu giao thất bại (khách không nhận, sai địa chỉ...) → Operations Staff bấm
      "Giao thất bại", hệ thống chuyển đơn sang CANCELLED và hoàn lại tồn kho (theo FR-10)
Postcondition: Trạng thái đơn khớp với thực tế giao hàng, dù không có webhook tự động.
```

### UC-03: Receive Delivery Status Update (chỉ áp dụng cho FulfillmentType = MARKETPLACE_MANAGED)
```
Actor: Marketplace (đóng vai trò Shipping Carrier khi tự lo vận chuyển, ví dụ Shopee Logistics)
Precondition: Đơn đã ở trạng thái READY_FOR_PICKUP/SHIPPED, FulfillmentType = MARKETPLACE_MANAGED
Main Flow:
  1. Marketplace gửi webhook cập nhật trạng thái (đã lấy hàng / đang giao / giao thành công / giao thất bại)
  2. Hệ thống xác thực webhook, cập nhật trạng thái đơn tương ứng
  3. Nếu trạng thái = delivered → đơn chuyển DELIVERED
Alternative Flow:
  3a. Nếu trạng thái = failed (giao thất bại hoàn toàn, không giao lại được) →
      đơn chuyển CANCELLED, hệ thống hoàn lại tồn kho đã reserve
Postcondition: Trạng thái đơn khớp với trạng thái giao hàng thật.

Lưu ý: với đơn FulfillmentType = SELF_ARRANGED (nhân viên tự đặt Bee/Grab), KHÔNG áp
dụng use case này — xem UC-07 (cập nhật trạng thái bằng tay) vì hệ thống không nhận
webhook tự động từ Bee/Grab.
```

### UC-04: Restock Inventory
```
Actor: Warehouse Staff
Precondition: Warehouse Staff đã đăng nhập, có quyền tương ứng
Main Flow:
  1. Warehouse Staff nhập số lượng hàng nhập thêm cho 1 SKU
  2. Hệ thống cộng thêm vào tồn kho hiện có
  3. Hệ thống tự đồng bộ tồn kho mới lên các sàn đã kết nối (theo FR-02)
Alternative Flow:
  1a. Số lượng nhập <= 0 → hệ thống từ chối, báo lỗi validate
Postcondition: Tồn kho được cập nhật và đồng bộ lên các sàn.
```

### UC-05: Cancel Order (nội bộ)
```
Actor: Operations Staff
Precondition: Đơn chưa ở trạng thái SHIPPED
Main Flow:
  1. Operations Staff chọn hủy đơn, nhập lý do
  2. Hệ thống chuyển trạng thái đơn sang CANCELLED
  3. Hệ thống hoàn lại tồn kho đã reserve cho đơn này
  4. Hệ thống gửi callback thông báo hủy về Marketplace tương ứng
Alternative Flow:
  1a. Đơn đã ở trạng thái SHIPPED → hệ thống từ chối hành động hủy, báo lỗi
      "ORDER_ALREADY_SHIPPED"
Postcondition: Đơn CANCELLED, tồn kho hoàn lại, Marketplace được thông báo.
```

---

## 5. User Story

### US-01
**As a** Marketplace system, **I want** to send new orders via webhook, **so that** inventory and order status stay synced with the brand's internal system.

**Acceptance Criteria:**
- Given product "SKU-001" has 10 units in stock
- When Marketplace sends a webhook order for 3 units of "SKU-001"
- Then the order is confirmed and stock becomes 7 units

### US-02
**As an** Operations Staff, **I want** to view orders from all marketplaces in one place, **so that** I don't have to log into each marketplace separately.

### US-03
**As an** Operations Staff, **I want** to confirm and pack an order so the system automatically forwards it to the shipping carrier, **so that** I don't have to manually enter shipping info.

### US-04
**As a** Warehouse Staff, **I want** to restock inventory and have it automatically synced to all connected marketplaces, **so that** stock displayed on each platform is always accurate.

### US-05
**As a** Warehouse Staff, **I want** to be alerted when a product's stock falls below a threshold, **so that** I can reorder before it runs out.

### US-06 (Non-functional, viết dưới dạng story)
**As the system**, I must guarantee stock never goes negative even when multiple marketplaces send concurrent orders for the same SKU, **so that** the brand never oversells and disappoints customers on any channel.

### US-07 (Non-functional)
**As the system**, I must reject or safely ignore duplicate webhook deliveries from the same marketplace order, **so that** a network retry never creates a duplicate order.

### US-08 (mới)
**As an** Operations Staff, **I want** to choose between marketplace-managed shipping and self-arranged shipping (Bee/Grab) for each order, **so that** I can deliver nearby orders faster without waiting for marketplace logistics.

### US-09 (mới)
**As an** Operations Staff, **I want** to manually record and update the delivery status for self-arranged shipments, **so that** order status stays accurate even though Bee/Grab don't send automatic updates to our system.

---

## 6. Context Diagram

```
 [Shopee] ──┐
            ├── gửi đơn hàng (webhook) ──►
 [TikTok Shop] ──┘                          │
                                             ▼
                                      ┌─────────────┐
        [System Admin] ── cấu hình ─►│             │
                                      │     OMS     │
     [Operations Staff] ── xử lý ───►│  (nội bộ)   │── đồng bộ sản phẩm/tồn kho ──► [Shopee/TikTok Shop]
                                      │             │
      [Warehouse Staff] ── nhập kho ─►             │── đẩy đơn đã đóng gói ──► [Shipping Carrier (mock)]
                                      └─────────────┘
                                             ▲
                                             │
                              cập nhật trạng thái giao hàng (webhook)
                                             │
                                   [Shipping Carrier (mock)]
```

---

## 7. Glossary — Ubiquitous Language

| Thuật ngữ | Định nghĩa |
|---|---|
| **Style** | Mẫu thiết kế sản phẩm của MOC (ví dụ: "Áo thun Basic MOC") — chứa nhiều Variant |
| **Variant** | Biến thể cụ thể của 1 Style theo size và màu (ví dụ: "Áo thun Basic MOC / Size M / Màu Trắng") — có SKU và tồn kho riêng |
| **SKU** | Mã định danh duy nhất cho 1 Variant (ví dụ: `MOC-BASIC-M-WHITE`) — đây là đơn vị nhỏ nhất được track tồn kho và xuất hiện trong đơn hàng |
| Marketplace | Sàn TMĐT bên ngoài (Shopee, TikTok Shop) — nguồn gửi đơn hàng vào hệ thống |
| Channel | Tên gọi chung cho 1 kênh bán hàng (= 1 Marketplace đã kết nối) |
| Webhook | Cơ chế Marketplace/Shipping Carrier gọi ngược vào hệ thống nội bộ khi có sự kiện (đơn mới, cập nhật giao hàng) |
| Reserve (stock) | Giữ tạm tồn kho của 1 SKU Variant cho 1 đơn hàng ngay khi nhận webhook, trước khi đóng gói/giao |
| Restock | Nhập thêm số lượng vào tồn kho cho 1 SKU Variant cụ thể |
| Idempotency | Tính chất đảm bảo xử lý 1 sự kiện nhiều lần (do retry) vẫn ra kết quả như xử lý 1 lần |
| Fulfillment | Quá trình từ xác nhận đơn → đóng gói → đẩy cho vận chuyển → giao thành công |
| FulfillmentType | Cách thức vận chuyển: `MARKETPLACE_MANAGED` (sàn tự lo) hoặc `SELF_ARRANGED` (nhân viên tự đặt Bee/Grab) |
| Marketplace Managed | Đơn được sàn TMĐT tự điều phối lấy hàng và giao, hệ thống chỉ chờ webhook cập nhật |
| Self-arranged | Đơn được Operations Staff tự đặt vận chuyển ngoài bằng app điện thoại, hệ thống chỉ lưu thông tin nhập tay |
| Order Status | `RESERVED → CONFIRMED → PACKED → READY_FOR_PICKUP/SHIPPED → DELIVERED`, hoặc `→ CANCELLED` |

---

*Cập nhật lần cuối: [ngày bạn chỉnh sửa] — Người viết: [tên bạn]*
*Lưu ý: các giả định về số sàn, hướng đồng bộ, đơn vị vận chuyển ở đầu file cần được bạn xác nhận/chỉnh lại nếu khác thực tế brand của bạn.*