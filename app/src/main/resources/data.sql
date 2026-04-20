-- 初期データ: 顧客マスタ
INSERT INTO customers (first_name, last_name, email, phone, address, policy_number, policy_type, policy_status, premium_amount, policy_start_date, policy_end_date, agent_name, agent_email, created_at, updated_at) VALUES
('太郎', '田中', 'tanaka@example.com', '03-1234-5678', '東京都千代田区丸の内1-1-1', 'ZL-2024-001', 'LIFE', 1, 12000, '2024-01-01', '2054-01-01', '佐藤花子', 'sato@zurich.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('花子', '鈴木', 'suzuki@example.com', '03-2345-6789', '東京都港区六本木2-2-2', 'ZM-2024-002', 'MEDICAL', 1, 8500, '2024-03-15', '2025-03-15', '佐藤花子', 'sato@zurich.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('一郎', '佐藤', 'isato@example.com', '06-3456-7890', '大阪府大阪市中央区3-3-3', 'ZA-2024-003', 'AUTO', 1, 15000, '2024-06-01', '2025-06-01', '高橋次郎', 'takahashi@zurich.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('美咲', '高橋', 'takahashi.m@example.com', '052-4567-8901', '愛知県名古屋市中区4-4-4', 'ZL-2024-004', 'LIFE', 0, 0, NULL, NULL, '高橋次郎', 'takahashi@zurich.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('健太', '渡辺', 'watanabe@example.com', '03-5678-9012', '東京都渋谷区神宮前5-5-5', 'ZM-2023-005', 'MEDICAL', 2, 6000, '2023-01-01', '2024-01-01', '佐藤花子', 'sato@zurich.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('由美', '伊藤', NULL, '078-6789-0123', '兵庫県神戸市中央区6-6-6', 'ZF-2024-006', 'FIRE', 1, 4500, '2024-04-01', '2025-04-01', '山田三郎', 'yamada@zurich.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('大輔', '山本', 'yamamoto@example.com', NULL, '北海道札幌市中央区7-7-7', 'ZL-2024-007', 'LIFE', 3, 10000, '2024-02-01', '2054-02-01', '山田三郎', 'yamada@zurich.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
