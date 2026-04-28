# 圖書館館藏管理系統需求文件

> 版本：v1.1  
> 改寫方向：以既有「圖書館館藏管理系統」為基礎，移除所有讀書心得功能，保留訊息通知功能，新增 `book_copies` 後端表格以支援「同一本書有多本館藏」的借閱邏輯。  
> 後端：Eclipse + Spring Boot  
> 前端：VSCode + Angular 6+，可少量搭配 jQuery 進行 DOM 操作或舊版套件整合  
> 資料庫：MySQL
> 後端設計原則：DTO 僅在「登入、跨資料表查詢回傳、批次操作、避免暴露敏感欄位、表單欄位與 Entity 明顯不一致」時新增；一般 CRUD 若欄位與 Entity 高度一致，優先使用 Entity 或簡化參數接收，避免為每個功能都新增 Request / Response class。

---

## 1. 專案概述

### 1.1 專案名稱

圖書館館藏管理系統

### 1.2 專案目標

本系統目標是將原本桌面式 Java 圖書館管理系統改寫為前後端分離的 Web 系統，使讀者可以在線上查詢館藏、借閱書籍、申請歸還、預約書籍與查看通知；管理員可以維護書籍資料、管理每一本實體館藏、審核歸還、管理讀者資料與發送系統訊息。

### 1.3 系統設計理念

| 設計理念 | 說明 |
|---|---|
| 書目與實體館藏分離 | 同一本書可能有多本實體館藏，因此系統不應只用一本書一筆資料表示。應將「書籍基本資料」放在 `book_titles`，將每一本可借閱的實體書放在 `book_copies`。 |
| 借閱狀態以實體書為單位 | 借書、還書、毀損、遺失、預約都應綁定到某一本 `book_copy`，避免同一本書有多本時狀態互相干擾。 |
| 讀者與管理員權限分離 | 讀者只能操作自己的借閱、預約、訊息；管理員可維護館藏、讀者與借閱審核。 |
| 前後端分離 | 後端提供 REST API，前端 Angular 負責頁面與互動，降低 Swing 桌面程式與資料庫直接耦合的問題。 |
| 保留訊息通知機制 | 借閱成功、歸還審核、預約到書、逾期提醒等事件可透過 message 通知讀者。 |
| DTO 最小化設計 | 不為每個新增、修改、查詢動作機械式建立 DTO。只有當 API 輸入輸出與 Entity 不一致、需隱藏敏感欄位、需組合多表資料、或批次操作時才新增 DTO。 |
| 前端隱藏欄位輔助傳值 | 對於頁面已知且不需額外封裝的識別欄位，例如 `bookTitleId`、`copyId`、`borrowId`，可由 Angular 表單或 hidden 欄位帶入後端，避免多建 Request class。 |
| 移除讀書心得功能 | 原系統中的 `reviews`、`review_drafts`、心得審核、公開心得等功能全部不納入新版系統。 |

---

## 2. 系統範圍

### 2.1 保留功能

| 模組 | 是否保留 | 說明 |
|---|---:|---|
| 登入功能 | 保留 | 讀者與管理員登入。 |
| 讀者資料管理 | 保留 | 管理員維護讀者資料；讀者查看個人資料。 |
| 書籍資料管理 | 保留並重構 | 原 `books` 改為 `book_titles` + `book_copies`。 |
| 借閱功能 | 保留並重構 | 借閱對象改為 `book_copy`，不是只對書目借閱。 |
| 歸還申請與審核 | 保留 | 讀者提出歸還申請，管理員確認歸還與書況。 |
| 預約功能 | 保留並重構 | 可針對書目或特定館藏策略預約，建議以 `book_title` 為預約對象。 |
| 訊息通知 message | 保留 | 借閱、歸還、預約、系統通知使用。 |
| Excel 匯入匯出 | 可保留為管理員進階功能 | 若時間不足，可列為第二階段。 |

### 2.2 移除功能

| 原功能 | 移除原因 | 對應移除項目 |
|---|---|---|
| 讀書心得撰寫 | 本次專案聚焦館藏與借閱流程 | `ReviewUI`、`ReviewPublicUI`、`ReviewCheckUI` |
| 心得草稿 | 與讀書心得功能綁定 | `review_drafts`、`ReviewDraftsDao`、`ReviewDraftsService` |
| 心得公開與審核 | 與讀書心得功能綁定 | `reviews`、`ReviewsDao`、`ReviewsService` |
| 心得相關 VO | 不再需要 | `ReviewView`、`ReviewViewDaoImpl`、`ReviewViewServiceImpl` |

---

## 3. 使用者角色

| 角色 | 說明 | 主要權限 |
|---|---|---|
| 訪客 | 未登入使用者 | 可查看公開館藏查詢頁，不能借閱與預約。 |
| 讀者 | 已註冊並登入的使用者 | 查詢館藏、借書、預約、申請歸還、查看借閱紀錄、查看訊息。 |
| 管理員 | 圖書館後台管理者 | 維護書目與館藏、管理讀者、審核歸還、處理預約、查看借閱紀錄、發送訊息。 |

---

## 4. 功能需求總表

### 4.1 讀者端功能

| 編號 | 功能名稱 | 功能說明 | 優先度 |
|---|---|---|---|
| FR-R-01 | 讀者登入 | 讀者使用帳號密碼登入系統。 | 高 |
| FR-R-02 | 查詢館藏 | 依書名、作者、ISBN、出版社、分類查詢書籍。 | 高 |
| FR-R-03 | 查看書籍詳情 | 查看書籍基本資料、館藏總數、可借數量、目前可借館藏。 | 高 |
| FR-R-04 | 借閱書籍 | 當有可借館藏時，讀者可借閱一本實體館藏。 | 高 |
| FR-R-05 | 查看目前借閱 | 讀者查看自己尚未完成歸還的借閱紀錄。 | 高 |
| FR-R-06 | 申請歸還 | 讀者針對借閱紀錄提出歸還申請，等待管理員審核。 | 高 |
| FR-R-07 | 預約書籍 | 當書籍沒有可借館藏，讀者可預約該書目。 | 中 |
| FR-R-08 | 查看預約紀錄 | 讀者查看自己的預約狀態。 | 中 |
| FR-R-09 | 查看訊息 | 讀者查看系統訊息與通知。 | 高 |
| FR-R-10 | 標記訊息已讀 | 讀者可將訊息標記為已讀。 | 中 |
| FR-R-11 | 刪除訊息 | 讀者可刪除自己的訊息。 | 低 |
| FR-R-12 | 查看個人資料 | 讀者查看姓名、電話、Email、等級、點數等資訊。 | 中 |

### 4.2 管理員端功能

| 編號 | 功能名稱 | 功能說明 | 優先度 |
|---|---|---|---|
| FR-A-01 | 管理員登入 | 管理員使用帳號密碼登入後台。 | 高 |
| FR-A-02 | 新增書目 | 建立一本書的基本資料，例如 ISBN、書名、作者、出版社、分類。 | 高 |
| FR-A-03 | 編輯書目 | 修改書籍基本資料。 | 高 |
| FR-A-04 | 新增館藏冊本 | 在既有書目下新增一本或多本實體館藏。 | 高 |
| FR-A-05 | 編輯館藏冊本 | 修改館藏條碼、索書號、館藏狀態、位置等。 | 高 |
| FR-A-06 | 館藏狀態管理 | 可將實體館藏標記為可借、借出中、歸還待審核、毀損、遺失、下架。 | 高 |
| FR-A-07 | 讀者資料管理 | 新增、查詢、修改讀者資料。 | 高 |
| FR-A-08 | 借閱紀錄管理 | 查看全體借閱紀錄，依讀者、書籍、狀態篩選。 | 高 |
| FR-A-09 | 歸還審核 | 針對讀者歸還申請確認書況，並更新館藏狀態。 | 高 |
| FR-A-10 | 預約管理 | 查看預約清單，當有可借館藏時通知讀者。 | 中 |
| FR-A-11 | 訊息管理 | 發送、查看或管理系統訊息。 | 中 |
| FR-A-12 | Excel 匯入書籍 | 批次匯入書目與館藏資料。 | 低 |

---

## 5. 核心業務規則

### 5.1 書目與館藏規則

| 規則編號 | 規則內容 |
|---|---|
| BR-01 | `book_titles` 儲存書籍基本資料，例如 ISBN、書名、作者、出版社、出版年、分類。 |
| BR-02 | `book_copies` 儲存每一本實體館藏，每本實體館藏必須隸屬於一筆 `book_titles`。 |
| BR-03 | 同一本書若有 3 本館藏，應為 1 筆 `book_titles` + 3 筆 `book_copies`。 |
| BR-04 | 借閱、歸還、毀損、遺失等狀態必須更新在 `book_copies.copy_status`。 |
| BR-05 | 書籍查詢頁以 `book_titles` 為主，顯示該書的總館藏數與可借館藏數。 |

### 5.2 借閱規則

| 規則編號 | 規則內容 |
|---|---|
| BR-06 | 讀者借書時，系統必須檢查讀者是否存在且帳號狀態正常。 |
| BR-07 | 系統必須檢查讀者目前借閱數是否超過等級限制。 |
| BR-08 | 只有 `copy_status = AVAILABLE` 的館藏可以被借閱。 |
| BR-09 | 借閱成功後，建立一筆 `borrow_records`，並將該館藏狀態改為 `BORROWED`。 |
| BR-10 | 借閱成功後，系統可新增一筆 message 通知讀者借閱成功與到期日。 |
| BR-11 | 若同書目仍有其他可借館藏，其他讀者仍可借閱同一本書的其他冊本。 |
| BR-12 | 若同書目沒有任何可借館藏，讀者只能預約，不能借閱。 |

### 5.3 歸還規則

| 規則編號 | 規則內容 |
|---|---|
| BR-13 | 讀者只能對自己的借閱紀錄提出歸還申請。 |
| BR-14 | 讀者申請歸還後，`borrow_records.status` 改為 `RETURN_PENDING`，`book_copies.copy_status` 改為 `RETURN_PENDING`。 |
| BR-15 | 管理員審核歸還後，若書況正常，`book_copies.copy_status` 改為 `AVAILABLE`。 |
| BR-16 | 管理員審核歸還後，若書籍毀損或遺失，`book_copies.copy_status` 改為 `DAMAGED` 或 `LOST`。 |
| BR-17 | 歸還審核完成後，系統新增 message 通知讀者審核結果。 |

### 5.4 預約規則

| 規則編號 | 規則內容 |
|---|---|
| BR-18 | 預約建議綁定 `book_title_id`，而不是綁定單一本 `copy_id`。 |
| BR-19 | 同一位讀者不可重複預約同一本尚未完成的書目。 |
| BR-20 | 當館藏歸還並轉為可借時，系統可依預約順序通知第一位讀者。 |
| BR-21 | 預約通知可透過 `messages` 表建立通知。 |

### 5.5 訊息規則

| 規則編號 | 規則內容 |
|---|---|
| BR-22 | 訊息必須綁定讀者 `reader_id`。 |
| BR-23 | 訊息類型可包含借閱成功、歸還審核、預約通知、逾期提醒、系統公告。 |
| BR-24 | 讀者只能查看、標記、刪除自己的訊息。 |
| BR-25 | 管理員可由系統事件觸發訊息，或手動發送訊息。 |

---

## 6. 建議資料庫設計

### 6.1 資料表總覽

| 資料表 | 說明 | 是否新版保留 |
|---|---|---:|
| `readers` | 讀者資料 | 保留 |
| `admins` | 管理員帳號 | 新增建議 |
| `book_titles` | 書籍基本資料 | 新增，取代原 books 的書目部分 |
| `book_copies` | 實體館藏冊本 | 新增，支援同書多本借閱 |
| `borrow_records` | 借閱紀錄 | 保留並改欄位關聯 |
| `reservations` | 預約紀錄 | 保留並改為關聯書目 |
| `messages` | 訊息通知 | 保留 |
| `reviews` | 讀書心得 | 移除 |
| `review_drafts` | 心得草稿 | 移除 |

### 6.2 `readers` 讀者資料表

| 欄位 | 型別 | 說明 |
|---|---|---|
| `reader_id` | VARCHAR(30), PK | 讀者編號 |
| `reader_name` | VARCHAR(100) | 讀者姓名 |
| `national_id` | VARCHAR(20) | 身分證或識別欄位，可視需求保留 |
| `birthday` | DATE | 生日 |
| `address` | VARCHAR(255) | 地址 |
| `phone` | VARCHAR(30) | 電話 |
| `email` | VARCHAR(100) | Email |
| `online_username` | VARCHAR(50), UNIQUE | 登入帳號 |
| `online_password` | VARCHAR(255) | 密碼，正式系統應儲存加密雜湊值 |
| `reward_points` | INT | 借閱點數 |
| `reader_level` | INT | 讀者等級，影響可借數量 |
| `status` | VARCHAR(20) | ACTIVE、SUSPENDED |
| `created_at` | DATETIME | 建立時間 |
| `updated_at` | DATETIME | 更新時間 |

### 6.3 `admins` 管理員資料表

| 欄位 | 型別 | 說明 |
|---|---|---|
| `admin_id` | BIGINT, PK, AUTO_INCREMENT | 管理員流水號 |
| `username` | VARCHAR(50), UNIQUE | 管理員帳號 |
| `password` | VARCHAR(255) | 管理員密碼，正式系統應加密 |
| `admin_name` | VARCHAR(100) | 管理員姓名 |
| `role` | VARCHAR(30) | ADMIN、SUPER_ADMIN |
| `created_at` | DATETIME | 建立時間 |
| `updated_at` | DATETIME | 更新時間 |

### 6.4 `book_titles` 書目資料表

| 欄位 | 型別 | 說明 |
|---|---|---|
| `book_title_id` | BIGINT, PK, AUTO_INCREMENT | 書目 ID |
| `isbn` | VARCHAR(30) | ISBN，可允許重複或加條件唯一，依資料來源決定 |
| `title` | VARCHAR(255) | 書名 |
| `author` | VARCHAR(255) | 作者 |
| `publisher` | VARCHAR(255) | 出版社 |
| `publish_year` | VARCHAR(10) | 出版年 |
| `edition` | VARCHAR(50) | 版本 |
| `category` | VARCHAR(100) | 分類 |
| `cover_url` | VARCHAR(500) | 封面圖片路徑或網址，可選 |
| `description` | TEXT | 書籍簡介，可選 |
| `created_at` | DATETIME | 建立時間 |
| `updated_at` | DATETIME | 更新時間 |

### 6.5 `book_copies` 實體館藏表

| 欄位 | 型別 | 說明 |
|---|---|---|
| `copy_id` | BIGINT, PK, AUTO_INCREMENT | 實體館藏 ID |
| `book_title_id` | BIGINT, FK | 對應 `book_titles.book_title_id` |
| `book_barcode` | VARCHAR(50), UNIQUE | 每一本實體書的唯一條碼，例如 B00000001 |
| `call_number` | VARCHAR(100) | 索書號 |
| `location` | VARCHAR(100) | 館藏位置，例如 A 區 2 樓 |
| `price` | INT | 書籍價格，可用於遺失賠償參考 |
| `copy_status` | VARCHAR(30) | AVAILABLE、BORROWED、RETURN_PENDING、RESERVED、DAMAGED、LOST、REMOVED |
| `created_at` | DATETIME | 建立時間 |
| `updated_at` | DATETIME | 更新時間 |

#### `copy_status` 狀態說明

| 狀態 | 中文 | 可否被借閱 | 說明 |
|---|---|---:|---|
| AVAILABLE | 可借閱 | 是 | 館藏可被讀者借出。 |
| BORROWED | 借出中 | 否 | 已被借出。 |
| RETURN_PENDING | 歸還待審核 | 否 | 讀者已申請歸還，等待管理員確認。 |
| RESERVED | 預約保留 | 否 | 已保留給預約讀者取書，可視專案時間決定是否實作。 |
| DAMAGED | 毀損 | 否 | 館藏毀損，不可借。 |
| LOST | 遺失 | 否 | 館藏遺失，不可借。 |
| REMOVED | 下架 | 否 | 館藏不顯示於讀者端。 |

### 6.6 `borrow_records` 借閱紀錄表

| 欄位 | 型別 | 說明 |
|---|---|---|
| `borrow_id` | BIGINT, PK, AUTO_INCREMENT | 借閱紀錄 ID |
| `reader_id` | VARCHAR(30), FK | 讀者 ID |
| `copy_id` | BIGINT, FK | 實體館藏 ID |
| `borrow_date` | DATE | 借閱日期 |
| `due_date` | DATE | 到期日 |
| `return_request_date` | DATE | 讀者申請歸還日期 |
| `return_date` | DATE | 管理員確認歸還日期 |
| `status` | VARCHAR(30) | BORROWING、RETURN_PENDING、RETURNED、OVERDUE、LOST、DAMAGED |
| `admin_note` | VARCHAR(500) | 管理員備註 |
| `created_at` | DATETIME | 建立時間 |
| `updated_at` | DATETIME | 更新時間 |

### 6.7 `reservations` 預約紀錄表

| 欄位 | 型別 | 說明 |
|---|---|---|
| `reservation_id` | BIGINT, PK, AUTO_INCREMENT | 預約 ID |
| `reader_id` | VARCHAR(30), FK | 讀者 ID |
| `book_title_id` | BIGINT, FK | 書目 ID |
| `reserved_at` | DATETIME | 預約時間 |
| `expected_pickup_date` | DATE | 預計取書日期 |
| `status` | VARCHAR(30) | WAITING、NOTIFIED、CANCELLED、COMPLETED、EXPIRED |
| `created_at` | DATETIME | 建立時間 |
| `updated_at` | DATETIME | 更新時間 |

### 6.8 `messages` 訊息通知表

| 欄位 | 型別 | 說明 |
|---|---|---|
| `message_id` | BIGINT, PK, AUTO_INCREMENT | 訊息 ID |
| `reader_id` | VARCHAR(30), FK | 接收訊息的讀者 ID |
| `message_type` | VARCHAR(30) | BORROW_SUCCESS、RETURN_APPROVED、RESERVATION_NOTICE、OVERDUE_NOTICE、SYSTEM |
| `title` | VARCHAR(100) | 訊息標題 |
| `content` | TEXT | 訊息內容 |
| `is_read` | TINYINT(1) | 是否已讀 |
| `created_at` | DATETIME | 建立時間 |

---

## 7. 主要使用案例

### UC-01 讀者登入

| 項目 | 說明 |
|---|---|
| 主要角色 | 讀者 |
| 前置條件 | 讀者已建立帳號。 |
| 主要流程 | 1. 讀者輸入帳號密碼。<br>2. 前端呼叫登入 API。<br>3. 後端驗證帳號密碼。<br>4. 驗證成功後回傳讀者資訊與 token 或 session 資訊。<br>5. 前端導向讀者首頁。 |
| 替代流程 | 帳號密碼錯誤時，系統回傳錯誤訊息。 |
| 後置條件 | 讀者可操作借閱、預約、訊息等個人功能。 |

### UC-02 查詢館藏

| 項目 | 說明 |
|---|---|
| 主要角色 | 訪客、讀者 |
| 前置條件 | 無。 |
| 主要流程 | 1. 使用者輸入書名、作者、ISBN 或分類。<br>2. 前端呼叫查詢 API。<br>3. 後端查詢 `book_titles`，並統計每本書的總館藏數與可借數。<br>4. 前端顯示查詢結果。 |
| 替代流程 | 查無資料時顯示「無符合結果」。 |
| 後置條件 | 使用者可點入查看書籍詳情。 |

### UC-03 借閱書籍

| 項目 | 說明 |
|---|---|
| 主要角色 | 讀者 |
| 前置條件 | 讀者已登入，且該書有 `AVAILABLE` 館藏。 |
| 主要流程 | 1. 讀者在書籍詳情頁點選借閱。<br>2. 系統檢查讀者借閱上限。<br>3. 系統尋找該書目下第一本 `AVAILABLE` 的 `book_copy`。<br>4. 系統建立 `borrow_records`。<br>5. 系統將該 `book_copy.copy_status` 改為 `BORROWED`。<br>6. 系統建立借閱成功 message。<br>7. 前端顯示借閱成功與到期日。 |
| 替代流程 A | 無可借館藏時，顯示可預約。 |
| 替代流程 B | 已達借閱上限時，拒絕借閱並顯示原因。 |
| 後置條件 | 該實體館藏不可再被其他讀者借閱。 |

### UC-04 申請歸還

| 項目 | 說明 |
|---|---|
| 主要角色 | 讀者 |
| 前置條件 | 讀者已有 `BORROWING` 狀態借閱紀錄。 |
| 主要流程 | 1. 讀者進入目前借閱頁。<br>2. 選擇要歸還的借閱紀錄。<br>3. 系統確認該借閱紀錄屬於目前登入讀者。<br>4. 系統將 `borrow_records.status` 改為 `RETURN_PENDING`。<br>5. 系統將 `book_copies.copy_status` 改為 `RETURN_PENDING`。<br>6. 前端顯示等待管理員審核。 |
| 替代流程 | 非本人借閱紀錄不可操作。 |
| 後置條件 | 管理員可在後台看到歸還待審核清單。 |

### UC-05 管理員審核歸還

| 項目 | 說明 |
|---|---|
| 主要角色 | 管理員 |
| 前置條件 | 存在 `RETURN_PENDING` 的借閱紀錄。 |
| 主要流程 | 1. 管理員進入歸還審核頁。<br>2. 查看待審核清單。<br>3. 選擇一筆紀錄並輸入書況。<br>4. 若書況正常，系統將借閱紀錄改為 `RETURNED`，館藏改為 `AVAILABLE`。<br>5. 若毀損或遺失，系統將借閱紀錄改為 `DAMAGED` 或 `LOST`，館藏改為 `DAMAGED` 或 `LOST`。<br>6. 系統建立歸還審核結果 message 給讀者。 |
| 替代流程 | 找不到紀錄時回傳錯誤。 |
| 後置條件 | 借閱流程完成，或進入異常書況紀錄。 |

### UC-06 預約書籍

| 項目 | 說明 |
|---|---|
| 主要角色 | 讀者 |
| 前置條件 | 讀者已登入。 |
| 主要流程 | 1. 讀者在書籍詳情頁點選預約。<br>2. 系統檢查是否已有同書目未完成預約。<br>3. 系統建立 `reservations`，狀態為 `WAITING`。<br>4. 前端顯示預約成功。 |
| 替代流程 | 若已有未完成預約，系統拒絕重複預約。 |
| 後置條件 | 當該書有可借館藏時，可通知讀者取書或借閱。 |

### UC-07 查看與管理訊息

| 項目 | 說明 |
|---|---|
| 主要角色 | 讀者 |
| 前置條件 | 讀者已登入。 |
| 主要流程 | 1. 讀者進入訊息中心。<br>2. 系統查詢該讀者的 message 清單。<br>3. 讀者可查看內容、標記已讀或刪除。 |
| 替代流程 | 無訊息時顯示空狀態。 |
| 後置條件 | 訊息讀取狀態被更新。 |

---

## 8. API 規格草案

> 建議後端統一使用 `/api` 作為 REST API 前綴。  
> 需要登入的 API 應在 Header 傳入：`Authorization: Bearer <token>`。  
> 若尚未實作 JWT，可先以 Session 或簡化 token 處理，但文件仍保留 Header 欄位，方便後續擴充。

### 8.1 DTO 使用原則與前端 hidden 欄位原則

> 本專案不採用「每個 API 都一定建立一組 Request DTO / Response DTO」的方式。DTO 的目的應是降低耦合、保護欄位與整理跨表回傳資料，而不是增加 class 數量。

#### 8.1.1 必須使用 DTO 的情境

| 情境 | 是否使用 DTO | 說明 | 範例 |
|---|---:|---|---|
| 登入 | 必須 | 登入欄位與 Entity 不完全一致，且不能直接暴露密碼欄位。 | `LoginRequest`、`LoginResponse` |
| 回傳目前登入者資訊 | 建議 | 避免回傳密碼、內部狀態等敏感欄位。 | `ReaderInfoResponse`、`AdminInfoResponse` |
| 書籍查詢清單 | 必須 | 查詢結果需組合 `book_titles` 與 `book_copies` 統計資料。 | `BookSearchResponse`，含 totalCopies、availableCopies |
| 書籍詳情 | 必須 | 需要同時回傳書目、館藏統計、可借館藏清單。 | `BookDetailResponse` |
| 借閱結果 | 建議 | 借閱成功後需要回傳借閱紀錄、到期日、書名、館藏條碼等跨表資料。 | `BorrowResultResponse` |
| 批次新增館藏 | 必須 | 一次送入多筆 copies，直接用 Entity 不直覺。 | `BookCopyBatchCreateRequest` |
| 分頁清單 | 建議 | 統一回傳 items、page、size、totalElements。 | `PageResponse<T>` |
| 錯誤與成功格式 | 必須 | 統一 API 回應格式。 | `ApiResponse<T>` |

#### 8.1.2 不一定要新增 DTO 的情境

| 情境 | 建議做法 | 說明 |
|---|---|---|
| 新增單一本館藏 | 可直接接收 `BookCopy` 或簡化 `Map<String,Object>` / 表單欄位 | 若欄位與 Entity 幾乎一致，不必額外建立 `BookCopyRequest`。 |
| 修改單一本館藏 | 可直接接收 `BookCopy`，由 path variable 指定 `copyId` | 前端 hidden 欄位可帶 `copyId`，後端仍以 path 為主。 |
| 新增書目 | 可直接接收 `BookTitle` | 若沒有特殊轉換或欄位保護，可不建立 `BookTitleRequest`。 |
| 修改讀者基本資料 | 可直接接收允許修改欄位，或用 `Map` 接收 | 若只是 phone、email、address，沒有必要新增多個 DTO。 |
| 標記訊息已讀 | 不需要 DTO | `messageId` 由 path 傳入即可。 |
| 取消預約 | 不需要 DTO | `reservationId` 由 path 傳入即可。 |
| 申請歸還 | 不一定需要 DTO | 若只有 `borrowId` 與可選 note，可使用 path + request param / hidden 欄位。 |

#### 8.1.3 前端 hidden 欄位使用規則

| 欄位 | 使用位置 | 用途 | 注意事項 |
|---|---|---|---|
| `bookTitleId` | 書籍詳情頁、借閱按鈕、預約按鈕 | 告訴後端目前操作哪一本書目 | 後端仍需查 DB 確認存在。 |
| `copyId` | 管理員館藏編輯頁 | 指定要修改哪一本實體館藏 | 修改時建議同時放在 URL path。 |
| `borrowId` | 我的借閱頁、歸還申請按鈕 | 指定要申請歸還的借閱紀錄 | 後端必須驗證該紀錄屬於目前登入讀者。 |
| `reservationId` | 我的預約頁、取消預約按鈕 | 指定要取消的預約紀錄 | 後端必須驗證該預約屬於目前登入讀者。 |
| `messageId` | 訊息中心 | 指定要標記已讀或刪除的訊息 | 後端必須驗證該訊息屬於目前登入讀者。 |

> hidden 欄位只能減少前後端表單傳值的複雜度，不能取代後端權限驗證。任何 readerId、borrowId、messageId 都不能因為是前端傳來就直接信任。

### 8.2 Auth API

| 方法 | 路徑 | 權限 | 說明 | 傳入值 | 回傳值 |
|---|---|---|---|---|---|
| POST | `/api/auth/reader/login` | 無 | 讀者登入 | body: username, password | token, readerInfo |
| POST | `/api/auth/admin/login` | 無 | 管理員登入 | body: username, password | token, adminInfo |
| POST | `/api/auth/logout` | 已登入 | 登出 | Header Authorization | success message |

### 8.3 Book Title API

| 方法 | 路徑 | 權限 | 說明 | 傳入值 | 回傳值 |
|---|---|---|---|---|---|
| GET | `/api/books` | 無 | 查詢書目清單 | query: keyword, category, page, size | 書目清單、總筆數、可借數 |
| GET | `/api/books/{bookTitleId}` | 無 | 查詢書目詳情 | path: bookTitleId | 書目詳情、館藏統計、館藏清單 |
| POST | `/api/admin/books` | 管理員 | 新增書目 | Header Authorization; body: isbn, title, author, publisher, publishYear, edition, category | 新增後書目 |
| PUT | `/api/admin/books/{bookTitleId}` | 管理員 | 修改書目 | Header Authorization; path: bookTitleId; body: 書目欄位 | 修改後書目 |
| DELETE | `/api/admin/books/{bookTitleId}` | 管理員 | 下架或刪除書目 | Header Authorization; path: bookTitleId | success message |

### 8.4 Book Copy API

| 方法 | 路徑 | 權限 | 說明 | 傳入值 | 回傳值 |
|---|---|---|---|---|---|
| GET | `/api/admin/book-copies` | 管理員 | 查詢館藏冊本 | Header Authorization; query: bookTitleId, status, keyword | 館藏冊本清單 |
| GET | `/api/admin/book-copies/{copyId}` | 管理員 | 查詢單一館藏 | Header Authorization; path: copyId | 館藏詳情 |
| POST | `/api/admin/books/{bookTitleId}/copies` | 管理員 | 在書目下新增館藏 | Header Authorization; path 或 hidden 欄位: bookTitleId; body 可直接對應 BookCopy 欄位 | 新增後館藏；欄位單純時不必新增 BookCopyRequest |
| POST | `/api/admin/books/{bookTitleId}/copies/batch` | 管理員 | 批次新增多本館藏 | Header Authorization; path: bookTitleId; body: copies[] | 新增結果 |
| PUT | `/api/admin/book-copies/{copyId}` | 管理員 | 修改館藏資訊 | Header Authorization; path 或 hidden 欄位: copyId; body 可直接對應 BookCopy 欄位 | 修改後館藏；欄位單純時不必新增 BookCopyRequest |
| PATCH | `/api/admin/book-copies/{copyId}/status` | 管理員 | 修改館藏狀態 | Header Authorization; path: copyId; body: copyStatus | 修改後館藏 |

### 8.5 Borrow API

| 方法 | 路徑 | 權限 | 說明 | 傳入值 | 回傳值 |
|---|---|---|---|---|---|
| POST | `/api/borrows` | 讀者 | 借閱書籍 | Header Authorization; body 或 hidden 欄位: bookTitleId | 借閱結果；建議用 `BorrowResultResponse`，因為需組合書名、條碼、到期日 |
| GET | `/api/borrows/me/current` | 讀者 | 查詢目前借閱 | Header Authorization | 借閱中與歸還待審核清單 |
| GET | `/api/borrows/me/history` | 讀者 | 查詢個人借閱歷史 | Header Authorization; query: status, page, size | 借閱歷史 |
| POST | `/api/borrows/{borrowId}/return-request` | 讀者 | 申請歸還 | Header Authorization; path: borrowId；可選 request param/body: note | success message 或更新後借閱狀態；不一定需要 ReturnRequest DTO |
| GET | `/api/admin/borrows` | 管理員 | 查詢全部借閱紀錄 | Header Authorization; query: readerId, status, keyword, page, size | 借閱紀錄清單 |
| GET | `/api/admin/borrows/return-pending` | 管理員 | 查詢歸還待審核 | Header Authorization | 待審核清單 |
| POST | `/api/admin/borrows/{borrowId}/approve-return` | 管理員 | 審核歸還 | Header Authorization; path: borrowId; body: resultStatus, adminNote | 審核後結果 |

### 8.6 Reservation API

| 方法 | 路徑 | 權限 | 說明 | 傳入值 | 回傳值 |
|---|---|---|---|---|---|
| POST | `/api/reservations` | 讀者 | 預約書籍 | Header Authorization; body: bookTitleId | 預約紀錄 |
| GET | `/api/reservations/me` | 讀者 | 查詢我的預約 | Header Authorization | 預約清單 |
| PATCH | `/api/reservations/{reservationId}/cancel` | 讀者 | 取消預約 | Header Authorization; path 或 hidden 欄位: reservationId | success message；不需 DTO |
| GET | `/api/admin/reservations` | 管理員 | 查詢所有預約 | Header Authorization; query: status, bookTitleId, readerId | 預約清單 |
| PATCH | `/api/admin/reservations/{reservationId}/notify` | 管理員 | 通知讀者可取書 | Header Authorization; path: reservationId | message 與預約狀態 |

### 8.7 Message API

| 方法 | 路徑 | 權限 | 說明 | 傳入值 | 回傳值 |
|---|---|---|---|---|---|
| GET | `/api/messages/me` | 讀者 | 查詢我的訊息 | Header Authorization; query: read, page, size | 訊息清單 |
| GET | `/api/messages/me/unread-count` | 讀者 | 查詢未讀數 | Header Authorization | unreadCount |
| PATCH | `/api/messages/{messageId}/read` | 讀者 | 標記已讀 | Header Authorization; path 或 hidden 欄位: messageId | success message；不需 DTO |
| DELETE | `/api/messages/{messageId}` | 讀者 | 刪除訊息 | Header Authorization; path 或 hidden 欄位: messageId | success message；不需 DTO |
| POST | `/api/admin/messages` | 管理員 | 管理員發送訊息 | Header Authorization; body: readerId, title, content, messageType | message |

### 8.8 Reader API

| 方法 | 路徑 | 權限 | 說明 | 傳入值 | 回傳值 |
|---|---|---|---|---|---|
| GET | `/api/readers/me` | 讀者 | 查看個人資料 | Header Authorization | readerInfo |
| PUT | `/api/readers/me` | 讀者 | 修改部分個人資料 | Header Authorization; body: phone, email, address | 更新後 readerInfo |
| GET | `/api/admin/readers` | 管理員 | 查詢讀者清單 | Header Authorization; query: keyword, status, page, size | 讀者清單 |
| POST | `/api/admin/readers` | 管理員 | 新增讀者 | Header Authorization; body: reader fields | 新增後讀者 |
| PUT | `/api/admin/readers/{readerId}` | 管理員 | 修改讀者 | Header Authorization; path: readerId; body: reader fields | 修改後讀者 |

---

## 9. 前端頁面規劃

### 9.1 Angular 模組建議

| 模組 | 說明 | 主要頁面 |
|---|---|---|
| `auth` | 登入與登出 | reader-login、admin-login |
| `reader` | 讀者端功能 | reader-home、book-search、book-detail、my-borrows、my-reservations、message-center、profile |
| `admin` | 管理員端功能 | admin-dashboard、book-title-manage、book-copy-manage、reader-manage、borrow-manage、return-review、reservation-manage、message-manage |
| `shared` | 共用元件 | navbar、sidebar、pagination、alert、confirm-dialog |
| `core` | 核心服務 | auth.service、api.service、token.interceptor、auth.guard |

### 9.2 讀者端頁面

| 頁面 | 路由 | 功能 |
|---|---|---|
| 讀者登入頁 | `/reader/login` | 讀者登入。 |
| 讀者首頁 | `/reader/home` | 顯示借閱摘要、未讀訊息、推薦入口。 |
| 館藏查詢頁 | `/books` | 查詢書籍、顯示可借數。 |
| 書籍詳情頁 | `/books/:id` | 顯示書目資料與館藏狀態，可借閱或預約。 |
| 我的借閱頁 | `/reader/borrows` | 查看目前借閱、申請歸還。 |
| 我的預約頁 | `/reader/reservations` | 查看與取消預約。 |
| 訊息中心 | `/reader/messages` | 查看、標記已讀、刪除訊息。 |
| 個人資料頁 | `/reader/profile` | 查看與修改基本資料。 |

### 9.3 管理員端頁面

| 頁面 | 路由 | 功能 |
|---|---|---|
| 管理員登入頁 | `/admin/login` | 管理員登入。 |
| 後台首頁 | `/admin/dashboard` | 顯示今日借閱、待審核歸還、預約通知等摘要。 |
| 書目管理頁 | `/admin/books` | 新增、編輯、查詢書目。 |
| 館藏冊本管理頁 | `/admin/book-copies` | 新增、批次新增、編輯實體館藏。 |
| 讀者管理頁 | `/admin/readers` | 新增、編輯、查詢讀者。 |
| 借閱紀錄管理頁 | `/admin/borrows` | 查看全部借閱紀錄。 |
| 歸還審核頁 | `/admin/return-review` | 審核讀者歸還申請。 |
| 預約管理頁 | `/admin/reservations` | 查看預約清單與通知讀者。 |
| 訊息管理頁 | `/admin/messages` | 發送或查看系統訊息。 |

### 9.4 jQuery 使用範圍建議

| 可使用情境 | 說明 |
|---|---|
| 舊版日期套件 | 若 Angular 6+ 不方便整合某些日期選擇套件，可少量使用 jQuery。 |
| 表格外掛 | 若使用 DataTables 類型套件，可由 jQuery 處理，但要注意與 Angular 生命週期整合。 |
| DOM 動畫 | 簡單展開、收合、提示效果可少量使用。 |

> 注意：主要資料綁定、API 呼叫、路由切換與表單驗證仍建議由 Angular 負責，不建議大量使用 jQuery 直接操作畫面狀態，避免資料不同步。

---

## 10. 後端架構規劃

### 10.1 Spring Boot 分層架構

| 層級 | 套件範例 | 職責 | 本專案使用原則 |
|---|---|---|---|
| Controller | `com.library.controller` | 接收 HTTP request，回傳 API response。 | 儘量保持薄 Controller，業務規則交給 Service。 |
| Service | `com.library.service` | 處理借閱上限、館藏狀態轉換、預約與訊息建立。 | 核心規則集中於 Service，不放在前端或 Controller。 |
| Repository | `com.library.repository` | 使用 Spring Data JPA 存取資料庫。 | 以 JPA derived query 或 JPQL 查詢統計資料。 |
| Entity | `com.library.entity` | 對應資料表。 | 單純 CRUD 可直接接收 Entity 或部分欄位，不必硬建 DTO。 |
| DTO | `com.library.dto` | 定義特殊 request / response 格式。 | 僅在必要時建立，避免 class 過多。 |
| Mapper | `com.library.mapper` | Entity 與 DTO 轉換。 | 只有 DTO 結構較複雜時才建立 Mapper；簡單欄位可在 Service 內組裝。 |
| Exception | `com.library.exception` | 統一錯誤處理。 | 建議保留 `BusinessException` 與 `GlobalExceptionHandler`。 |
| Config | `com.library.config` | CORS、安全設定、攔截器等。 | 保留 CORS 與登入驗證設定。 |

### 10.2 DTO 最小化後端 Class 清單

| 模組 | Entity | Repository | Service | Controller | 必要 DTO / 可省略 DTO |
|---|---|---|---|---|---|
| Common | 無 | 無 | 無 | `GlobalExceptionHandler` | 必要：`ApiResponse<T>`、可選：`PageResponse<T>` |
| Auth | `Reader`、`Admin` | `ReaderRepository`、`AdminRepository` | `AuthService` | `AuthController` | 必要：`LoginRequest`、`LoginResponse`。不可直接用 Entity 接密碼登入。 |
| Reader | `Reader` | `ReaderRepository` | `ReaderService` | `ReaderController`、`AdminReaderController` | 建議：`ReaderInfoResponse`，避免回傳 password。新增/修改讀者可直接接收 `Reader` 或允許欄位。 |
| Book Title | `BookTitle` | `BookTitleRepository` | `BookTitleService` | `BookController`、`AdminBookController` | 必要：`BookSearchResponse`、`BookDetailResponse`。新增/修改書目可直接接收 `BookTitle`。 |
| Book Copy | `BookCopy` | `BookCopyRepository` | `BookCopyService` | `AdminBookCopyController` | 必要：`BookCopyBatchCreateRequest`。單筆新增/修改可直接接收 `BookCopy`，不必硬建 `BookCopyRequest`。 |
| Borrow | `BorrowRecord` | `BorrowRecordRepository` | `BorrowService` | `BorrowController`、`AdminBorrowController` | 建議：`BorrowResultResponse`。借閱只需 `bookTitleId`，可由 hidden 欄位或 request param 傳入；申請歸還不一定需要 `ReturnRequest`。 |
| Reservation | `Reservation` | `ReservationRepository` | `ReservationService` | `ReservationController`、`AdminReservationController` | 預約只需 `bookTitleId`，可不建 `ReservationRequest`；清單若需顯示書名可建 `ReservationListResponse`。 |
| Message | `Message` | `MessageRepository` | `MessageService` | `MessageController`、`AdminMessageController` | 查詢回傳建議用 `MessageResponse` 避免多餘欄位；標記已讀、刪除不需 DTO。管理員發送訊息可直接接收 `Message` 或簡化欄位。 |

### 10.3 前端 hidden 欄位與後端接收方式

| 操作 | 前端可傳欄位 | 後端接收方式 | 是否需要 DTO |
|---|---|---|---:|
| 借閱書籍 | hidden: `bookTitleId` | `@RequestParam Long bookTitleId` 或簡單 JSON body | 否 |
| 預約書籍 | hidden: `bookTitleId` | `@RequestParam Long bookTitleId` 或簡單 JSON body | 否 |
| 申請歸還 | hidden: `borrowId`、可選 `note` | `@PathVariable Long borrowId` + `@RequestParam(required=false) String note` | 否 |
| 取消預約 | hidden: `reservationId` | `@PathVariable Long reservationId` | 否 |
| 標記訊息已讀 | hidden: `messageId` | `@PathVariable Long messageId` | 否 |
| 新增單本館藏 | hidden: `bookTitleId` + 表單欄位 | `@PathVariable Long bookTitleId` + `@RequestBody BookCopy bookCopy` | 否 |
| 批次新增館藏 | `bookTitleId` + `copies[]` | `@RequestBody BookCopyBatchCreateRequest` | 是 |

> 實作時建議優先使用 `@PathVariable` 表示主要資源 ID；hidden 欄位可作為 Angular 表單狀態保存方式，但送出 API 時仍可組成 path 或 query param。

---

## 11. 重要流程設計

### 11.1 借閱流程

```text
讀者點選借閱
→ 前端送出 bookTitleId
→ 後端驗證讀者登入狀態
→ 檢查讀者借閱上限
→ 查詢該 bookTitleId 下是否有 AVAILABLE 的 book_copy
→ 若有，建立 borrow_record
→ 更新 book_copy 狀態為 BORROWED
→ 建立 message：借閱成功
→ 回傳借閱紀錄與 dueDate
```

### 11.2 歸還流程

```text
讀者點選申請歸還
→ 後端確認 borrow_record 屬於該讀者
→ borrow_record.status = RETURN_PENDING
→ book_copy.copy_status = RETURN_PENDING
→ 管理員進入歸還審核頁
→ 管理員確認書況
→ 正常：borrow_record.status = RETURNED，book_copy.copy_status = AVAILABLE
→ 毀損：borrow_record.status = DAMAGED，book_copy.copy_status = DAMAGED
→ 遺失：borrow_record.status = LOST，book_copy.copy_status = LOST
→ 建立 message：歸還審核結果
```

### 11.3 預約通知流程

```text
讀者預約書目
→ reservations.status = WAITING
→ 某一本 book_copy 歸還並變為 AVAILABLE
→ 系統查詢該 book_title 的最早 WAITING 預約
→ 將預約狀態改為 NOTIFIED
→ 建立 message：可取書或可借閱通知
→ 若專案時間足夠，可將 book_copy 狀態暫改為 RESERVED
```

---

## 12. 驗收標準

| 編號 | 驗收項目 | 驗收條件 |
|---|---|---|
| AC-01 | 書目與館藏分離 | 同一本書可建立多本 `book_copies`，且每本有不同條碼。 |
| AC-02 | 查詢書籍可顯示可借數 | 書籍清單能顯示總館藏數與可借數。 |
| AC-03 | 借閱只影響一本實體館藏 | 借出一本 copy 後，同書其他 AVAILABLE copy 仍可借。 |
| AC-04 | 無可借館藏不能借閱 | 若所有 copy 都非 AVAILABLE，借閱 API 需拒絕並提示可預約。 |
| AC-05 | 歸還需管理員審核 | 讀者申請歸還後，館藏狀態為 RETURN_PENDING，不會直接變 AVAILABLE。 |
| AC-06 | 管理員可完成歸還審核 | 管理員審核後可將館藏改回 AVAILABLE 或改為 DAMAGED / LOST。 |
| AC-07 | 訊息功能正常 | 借閱成功、歸還審核結果可產生 message，讀者可查看。 |
| AC-08 | 讀書心得完全移除 | 前端無心得頁面，後端無 Review 相關 API，資料表不建立 reviews / review_drafts。 |
| AC-09 | 權限區分 | 讀者不可呼叫管理員 API，讀者只能查自己的借閱、預約、訊息。 |
| AC-10 | 前後端可串接 | Angular 可透過 REST API 完成查詢、借閱、歸還、訊息流程。 |

---

## 13. 第一階段開發優先順序

| 階段 | 目標 | 功能 |
|---|---|---|
| 第一階段 | 完成核心借閱閉環 | 登入、書籍查詢、book_titles、book_copies、借閱、申請歸還、歸還審核、message。 |
| 第二階段 | 補齊管理功能 | 讀者管理、館藏批次新增、預約管理、借閱歷史查詢。 |
| 第三階段 | 優化與擴充 | Excel 匯入、逾期提醒、自動通知、儀表板統計。 |

---

## 14. 從舊系統到新系統的改寫對照

| 舊系統項目 | 新系統對應 | 處理方式 |
|---|---|---|
| Swing UI Controller | Angular Component | 原 UI 邏輯改為前端頁面與服務。 |
| DAO / JDBC | Spring Data JPA Repository | 改用 Entity + Repository。 |
| ServiceImpl | Spring Boot Service | 保留業務邏輯概念，改為 Spring Bean。 |
| Books | BookTitle + BookCopy | 拆分書目與實體館藏。 |
| BorrowRecords | BorrowRecord | 保留，但關聯改為 copyId。 |
| Messages | Message | 保留並強化 messageType、title。 |
| Reviews / ReviewDrafts | 無 | 完全移除。 |
| AppException | Custom BusinessException | 改由 GlobalExceptionHandler 統一回應。 |

---

## 15. 給組員的開發注意事項

1. 不要再把「一本書」與「一本實體館藏」混在同一張表處理。
2. 借閱時不要只更新 `book_titles`，必須更新特定 `book_copies`。
3. 讀者端所有個人資料 API 都必須依登入者身分查詢，不應由前端任意傳 readerId 後直接信任。hidden 欄位可用於保存 `bookTitleId`、`borrowId` 等頁面狀態，但後端仍必須重新驗證權限。
4. 管理員 API 路徑建議統一放在 `/api/admin/**`，方便權限控管。
5. 前端 Angular 使用 Service 統一呼叫 API，不建議在 Component 內直接寫大量 HTTP 邏輯。
6. `message` 功能不要移除，應作為借閱、歸還、預約流程的通知中心。
7. 讀書心得相關資料表、頁面、API、Service、Repository 均不納入新版開發。
8. DTO 不要為了「每個功能都有一個 class」而建立；只有輸入輸出與 Entity 不一致、需要隱藏欄位、跨表查詢、批次操作、或統一回應格式時才新增。

---

## 16. 建議專案目錄

### 16.1 後端 Spring Boot

```text
library-backend/
└── src/main/java/com/library/
    ├── LibraryApplication.java
    ├── config/
    ├── controller/
    │   ├── AuthController.java
    │   ├── BookController.java
    │   ├── BorrowController.java
    │   ├── MessageController.java
    │   ├── ReservationController.java
    │   └── admin/
    │       ├── AdminBookController.java
    │       ├── AdminBookCopyController.java
    │       ├── AdminBorrowController.java
    │       ├── AdminReaderController.java
    │       ├── AdminReservationController.java
    │       └── AdminMessageController.java
    ├── dto/          # 僅放必要 DTO，例如 LoginRequest、BookSearchResponse、ApiResponse
    ├── entity/
    ├── exception/
    ├── mapper/       # 僅複雜 DTO 轉換才建立，不強制每個 Entity 都要 Mapper
    ├── repository/
    └── service/
```

### 16.2 前端 Angular

```text
library-frontend/
└── src/app/
    ├── core/
    │   ├── services/
    │   ├── guards/
    │   └── interceptors/
    ├── shared/
    │   └── components/
    ├── auth/
    ├── reader/
    │   ├── book-search/
    │   ├── book-detail/
    │   ├── my-borrows/
    │   ├── my-reservations/
    │   ├── message-center/
    │   └── profile/
    └── admin/
        ├── dashboard/
        ├── book-title-manage/
        ├── book-copy-manage/
        ├── reader-manage/
        ├── borrow-manage/
        ├── return-review/
        ├── reservation-manage/
        └── message-manage/
```
