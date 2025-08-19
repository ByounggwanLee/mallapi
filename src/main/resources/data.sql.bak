-- 기본 역할 데이터 삽입
INSERT INTO roles (role_name, description, is_active, created_at, updated_at) VALUES
('ROLE_USER', '일반 사용자', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROLE_ADMIN', '관리자', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROLE_MANAGER', '매니저', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROLE_SELLER', '판매자', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 테스트용 회원 데이터 삽입 (개발 환경에서만)
INSERT INTO members (email, name, picture_url, provider, provider_id, is_active, created_at, updated_at) VALUES
('admin@mallapi.com', '관리자', null, 'local', 'admin', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user@mallapi.com', '일반사용자', null, 'local', 'user', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 회원-역할 매핑 (개발 환경에서만)
INSERT INTO member_roles (member_id, role_id, is_active, created_at, updated_at) VALUES
(1, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- admin@mallapi.com -> ROLE_ADMIN
(1, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- admin@mallapi.com -> ROLE_USER
(2, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- user@mallapi.com -> ROLE_USER

-- 테스트용 상품 데이터 삽입 (개발 환경에서만)
INSERT INTO products (product_name, description, price, category, stock_quantity, image_url, is_available, status, member_id, created_at, updated_at) VALUES
('맥북 프로 14인치', 'Apple M2 Pro 칩이 탑재된 맥북 프로', 2590000.00, 'Electronics', 10, 'https://example.com/macbook-pro.jpg', true, 'SALE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('아이폰 15 Pro', '티타늄 디자인의 아이폰 15 Pro', 1550000.00, 'Electronics', 15, 'https://example.com/iphone-15-pro.jpg', true, 'SALE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('에어팟 프로 2세대', '공간 음향 기술이 적용된 에어팟', 329000.00, 'Electronics', 25, 'https://example.com/airpods-pro.jpg', true, 'SALE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('나이키 에어맥스', '편안한 운동화', 159000.00, 'Fashion', 50, 'https://example.com/nike-airmax.jpg', true, 'SALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('삼성 갤럭시 S24', '최신 안드로이드 스마트폰', 1200000.00, 'Electronics', 8, 'https://example.com/galaxy-s24.jpg', true, 'SALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 테스트용 할일 데이터 삽입 (개발 환경에서만)
INSERT INTO todos (title, description, is_completed, member_id, created_at, updated_at) VALUES
('프로젝트 설계 완료하기', 'Mall API 프로젝트의 전체적인 설계를 완료해야 합니다.', false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('API 문서 작성', 'Swagger를 이용한 API 문서를 작성합니다.', false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('단위 테스트 작성', '모든 서비스 계층에 대한 단위 테스트를 작성합니다.', false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('코드 리뷰 완료', '팀원들과 코드 리뷰를 진행합니다.', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('사용자 매뉴얼 작성', '최종 사용자를 위한 매뉴얼을 작성합니다.', false, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('배포 환경 설정', '운영 서버 배포를 위한 환경을 설정합니다.', true, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
