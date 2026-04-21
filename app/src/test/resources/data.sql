-- テスト用初期データ
-- 30日以内に更新日が来る有効契約（agentName=佐藤花子）
INSERT INTO customers (first_name, last_name, email, phone, address, policy_number, policy_type, policy_status, premium_amount, policy_start_date, policy_end_date, agent_name, agent_email, created_at, updated_at) VALUES
('花子', '鈴木', 'suzuki@example.com', '03-2345-6789', '東京都港区六本木2-2-2', 'ZM-2026-001', 'MEDICAL', 1, 8500, CURRENT_DATE, DATEADD('DAY', 15, CURRENT_DATE), '佐藤花子', 'sato@example.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 30日以内に更新日が来る有効契約（agentName=高橋次郎）
('一郎', '佐藤', 'isato@example.com', '06-3456-7890', '大阪府大阪市中央区3-3-3', 'ZA-2026-002', 'AUTO', 1, 15000, CURRENT_DATE, DATEADD('DAY', 25, CURRENT_DATE), '高橋次郎', 'takahashi@example.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 60日以内（30日超）に更新日が来る有効契約（agentName=佐藤花子）
('由美', '伊藤', 'ito@example.com', '078-6789-0123', '兵庫県神戸市中央区6-6-6', 'ZF-2026-003', 'FIRE', 1, 4500, CURRENT_DATE, DATEADD('DAY', 50, CURRENT_DATE), '佐藤花子', 'sato@example.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 有効だが更新日がnull
('美咲', '高橋', 'takahashi@example.com', '052-4567-8901', '愛知県名古屋市中区4-4-4', 'ZL-2026-004', 'LIFE', 1, 12000, CURRENT_DATE, NULL, '高橋次郎', 'takahashi@example.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 無効契約（policyStatus=2: 失効）
('健太', '渡辺', 'watanabe@example.com', '03-5678-9012', '東京都渋谷区神宮前5-5-5', 'ZM-2023-005', 'MEDICAL', 2, 6000, CURRENT_DATE, DATEADD('DAY', 10, CURRENT_DATE), '佐藤花子', 'sato@example.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 有効契約だが更新日が遠い（365日超）
('太郎', '田中', 'tanaka@example.com', '03-1234-5678', '東京都千代田区丸の内1-1-1', 'ZL-2026-006', 'LIFE', 1, 12000, CURRENT_DATE, DATEADD('DAY', 400, CURRENT_DATE), '佐藤花子', 'sato@example.co.jp', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
