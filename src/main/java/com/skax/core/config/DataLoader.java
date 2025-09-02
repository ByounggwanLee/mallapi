package com.skax.core.config;

import com.skax.core.entity.cart.Cart;
import com.skax.core.entity.cart.CartItem;
import com.skax.core.entity.member.Member;
import com.skax.core.entity.member.MemberRole;
import com.skax.core.entity.product.Product;
import com.skax.core.entity.todo.Todo;
import com.skax.core.repository.cart.CartItemRepository;
import com.skax.core.repository.cart.CartRepository;
import com.skax.core.repository.member.MemberRepository;
import com.skax.core.repository.product.ProductRepository;
import com.skax.core.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 애플리케이션 시작 시 테스트 데이터를 생성하는 DataLoader
 * 
 * <p>프로젝트 시작 시 모든 엔티티별로 테스트 데이터 100건을 자동으로 생성합니다.</p>
 * <ul>
 *   <li>Member: 100건 (일반 회원 + 소셜 로그인 회원)</li>
 *   <li>Product: 100건 (다양한 카테고리, 가격대, BaseEntity 기반 감사 정보 포함)</li>
 *   <li>Cart: 회원별 장바구니 자동 생성</li>
 *   <li>CartItem: 100건 (활성 상품만 사용하여 장바구니 아이템 생성)</li>
 *   <li>Todo: 100건 (기존 50건에서 확장)</li>
 * </ul>
 * 
 * <p><strong>주요 특징:</strong></p>
 * <ul>
 *   <li>BaseEntity 기반 통합 감사 시스템 활용</li>
 *   <li>논리적 삭제(soft delete) 기능 테스트를 위한 일부 데이터 삭제 처리</li>
 *   <li>다양한 사용자 컨텍스트로 생성자/수정자 정보 다양화</li>
 *   <li>활성 상품만을 사용한 장바구니 아이템 생성으로 데이터 무결성 보장</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 3.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final Random random = new Random();

    /**
     * 할일 제목 템플릿 목록
     */
    private static final String[] TODO_TITLES = {
        "Spring Boot 프로젝트 설정하기",
        "데이터베이스 연결 확인하기",
        "REST API 엔드포인트 작성하기",
        "JPA 엔티티 매핑 완료하기",
        "단위 테스트 작성하기",
        "통합 테스트 구현하기",
        "로깅 설정 최적화하기",
        "보안 설정 강화하기",
        "예외 처리 로직 개선하기",
        "API 문서화 완료하기",
        "코드 리뷰 진행하기",
        "성능 최적화 작업하기",
        "배포 스크립트 작성하기",
        "모니터링 설정하기",
        "백업 전략 수립하기",
        "사용자 인터페이스 개선하기",
        "검색 기능 구현하기",
        "페이징 처리 최적화하기",
        "캐싱 전략 적용하기",
        "오류 로그 분석하기",
        "코드 리팩토링 진행하기",
        "새로운 기능 설계하기",
        "데이터 마이그레이션 계획하기",
        "API 버전 관리 설정하기",
        "CI/CD 파이프라인 구축하기",
        "Docker 컨테이너화 작업하기",
        "Kubernetes 배포 준비하기",
        "로드 밸런싱 설정하기",
        "SSL 인증서 갱신하기",
        "데이터베이스 인덱스 최적화하기",
        "메모리 사용량 분석하기",
        "네트워크 성능 최적화하기",
        "사용자 피드백 분석하기",
        "새로운 라이브러리 조사하기",
        "보안 취약점 점검하기",
        "코딩 컨벤션 정리하기",
        "문서화 업데이트하기",
        "버그 수정 작업하기",
        "기능 요구사항 분석하기",
        "시스템 아키텍처 설계하기",
        "프로토타입 개발하기",
        "사용자 스토리 작성하기",
        "테스트 케이스 설계하기",
        "품질 보증 프로세스 개선하기",
        "개발 환경 설정하기",
        "프로젝트 일정 관리하기",
        "팀 미팅 준비하기",
        "기술 스택 검토하기",
        "개발 도구 업데이트하기",
        "프로젝트 마무리 작업하기"
    };

    /**
     * 작성자 목록
     */
    private static final String[] AUTHORS = {
        "김개발", "이프로", "박코더", "최엔지", "정데브",
        "한테크", "조프로그래머", "윤시스템", "장솔루션", "임아키텍트",
        "서풀스택", "권백엔드", "문프론트", "양데이터", "오클라우드",
        "배데브옵스", "성보안", "전AI", "류모바일", "신웹개발자"
    };

    /**
     * 회원 이메일 도메인 목록
     */
    private static final String[] EMAIL_DOMAINS = {
        "@gmail.com", "@naver.com", "@kakao.com", "@daum.net", "@outlook.com",
        "@yahoo.com", "@hotmail.com", "@icloud.com", "@company.co.kr", "@tech.net"
    };

    /**
     * 회원 닉네임 접두사
     */
    private static final String[] NICKNAME_PREFIXES = {
        "Cool", "Smart", "Happy", "Super", "Bright", "Quick", "Fast", "Strong",
        "Clever", "Nice", "Sweet", "Kind", "Funny", "Lucky", "Brave", "Gentle",
        "Active", "Creative", "Dynamic", "Expert", "Master", "Pro", "Elite", "Star"
    };

    /**
     * 소셜 로그인 제공자
     */
    private static final String[] SOCIAL_PROVIDERS = {
        "google", "kakao", "naver", "github", "facebook"
    };

    /**
     * 상품 카테고리 목록
     */
    private static final String[] PRODUCT_CATEGORIES = {
        "전자기기", "의류", "도서", "스포츠", "가구", "화장품", "식품", "완구",
        "자동차용품", "건강", "반려동물", "문구", "악기", "원예", "주방용품",
        "인테리어", "컴퓨터", "휴대폰", "카메라", "게임"
    };

    /**
     * 상품명 접두사
     */
    private static final String[] PRODUCT_NAME_PREFIXES = {
        "프리미엄", "신상", "베스트", "인기", "추천", "특가", "한정판", "고급",
        "실용적인", "편리한", "스마트", "모던", "클래식", "트렌디", "심플",
        "럭셔리", "컴팩트", "멀티", "프로페셔널", "에코"
    };

    /**
     * 상품명 본체
     */
    private static final String[] PRODUCT_NAMES = {
        "무선 이어폰", "스마트 워치", "블루투스 스피커", "노트북", "마우스패드",
        "키보드", "모니터", "충전기", "케이블", "스마트폰 케이스",
        "티셔츠", "청바지", "운동화", "가방", "모자", "선글라스", "벨트", "지갑",
        "책상", "의자", "조명", "쿠션", "러그", "액자", "화분", "시계",
        "컵", "접시", "수저", "팬", "냄비", "정리함", "휴지통", "세제"
    };

    @Override
    public void run(String... args) throws Exception {
        log.info("=== 엔티티별 테스트 데이터 로딩 시작 ===");
        
        // 시스템 사용자로 SecurityContext 설정 (audit 정보 자동 입력을 위함)
        setupSystemUserContext();
        
        try {
            // 각 엔티티별로 데이터 생성
            createMemberTestData();
            createProductTestData();
            createCartTestData();
            createCartItemTestData();
            createTodoTestData();
        } finally {
            // SecurityContext 정리
            SecurityContextHolder.clearContext();
        }
        
        log.info("=== 모든 엔티티 테스트 데이터 로딩 완료 ===");
    }

    /**
     * 시스템 사용자 인증 컨텍스트를 설정합니다.
     * BaseEntity의 audit 정보가 자동으로 입력되도록 합니다.
     */
    private void setupSystemUserContext() {
        // 시스템 사용자 확인 또는 생성
        Member systemUser = memberRepository.findByEmail("system@admin.com")
                .orElseGet(() -> {
                    log.info("시스템 사용자 생성 중...");
                    Member newSystemUser = Member.builder()
                            .email("system@admin.com")
                            .nickname("시스템관리자")
                            .pw(passwordEncoder.encode("system123!"))
                            .social(false)
                            .isActive(true)
                            .build();
                    newSystemUser.addRole(MemberRole.ADMIN);
                    return memberRepository.save(newSystemUser);
                });

        // SecurityContext에 시스템 사용자 설정
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                systemUser.getEmail(), 
                null, 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        log.info("시스템 사용자 인증 컨텍스트 설정 완료: {}", systemUser.getEmail());
    }

    /**
     * Member 테스트 데이터 100건을 생성합니다.
     */
    private void createMemberTestData() {
        long existingCount = memberRepository.count();
        log.info("기존 Member 데이터 개수: {}", existingCount);
        
        if (existingCount > 0) {
            log.info("기존 Member 데이터가 존재하여 생성을 건너뜁니다.");
            return;
        }

        log.info("Member 테스트 데이터 100건 생성 시작...");
        List<Member> members = new ArrayList<>();
        
        for (int i = 0; i < 100; i++) {
            String nickname = NICKNAME_PREFIXES[random.nextInt(NICKNAME_PREFIXES.length)] + 
                             AUTHORS[random.nextInt(AUTHORS.length)] + (i + 1);
            String email = "testuser" + (i + 1) + EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];
            
            Member member;
            
            // 30% 소셜 로그인 회원, 70% 일반 회원
            if (random.nextDouble() < 0.3) {
                // 소셜 로그인 회원
                String provider = SOCIAL_PROVIDERS[random.nextInt(SOCIAL_PROVIDERS.length)];
                member = Member.builder()
                        .email(email)
                        .nickname(nickname)
                        .social(true)
                        .provider(provider)
                        .providerId(provider + "_" + (i + 1))
                        .isActive(random.nextDouble() < 0.9) // 90% 활성 회원
                        .build();
            } else {
                // 일반 회원
                String encodedPassword = passwordEncoder.encode("password123");
                member = Member.builder()
                        .email(email)
                        .nickname(nickname)
                        .pw(encodedPassword)
                        .social(false)
                        .isActive(random.nextDouble() < 0.95) // 95% 활성 회원
                        .build();
                
                // 랜덤하게 권한 추가
                if (random.nextDouble() < 0.1) {
                    member.addRole(MemberRole.ADMIN);
                } else {
                    member.addRole(MemberRole.USER);
                }
            }
            
            members.add(member);
        }
        
        List<Member> savedMembers = memberRepository.saveAll(members);
        log.info("총 {}건의 Member 데이터가 생성되었습니다.", savedMembers.size());
        
        // 통계 출력
        long socialCount = savedMembers.stream().mapToLong(m -> m.isSocial() ? 1 : 0).sum();
        long activeCount = savedMembers.stream().mapToLong(m -> m.getIsActive() ? 1 : 0).sum();
        log.info("소셜 로그인 회원: {}건, 일반 회원: {}건", socialCount, savedMembers.size() - socialCount);
        log.info("활성 회원: {}건, 비활성 회원: {}건", activeCount, savedMembers.size() - activeCount);
    }

    /**
     * 다양한 사용자로 인증 컨텍스트를 변경합니다.
     * BaseEntity의 audit 정보에 다양한 생성자/수정자가 기록되도록 합니다.
     */
    private void changeAuthenticationContext() {
        List<Member> activeMembers = memberRepository.findAll().stream()
                .filter(Member::getIsActive)
                .filter(m -> !m.isSocial()) // 소셜 로그인 사용자 제외
                .toList();
        
        if (!activeMembers.isEmpty()) {
            Member randomMember = activeMembers.get(random.nextInt(activeMembers.size()));
            
            // SecurityContext에 랜덤 사용자 설정
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    randomMember.getEmail(), 
                    null, 
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            log.debug("인증 컨텍스트 변경: {}", randomMember.getEmail());
        }
    }

    /**
     * Product 테스트 데이터 100건을 생성합니다.
     * 
     * <p>BaseEntity 기반 감사 시스템을 활용하여 다양한 생성자/수정자 정보가 기록되도록 합니다.</p>
     * <ul>
     *   <li>다양한 카테고리와 가격대의 상품 생성</li>
     *   <li>각 상품마다 1~3개의 이미지 파일 추가</li>
     *   <li>10% 확률로 논리적 삭제 처리 (BaseEntity.deleted = true)</li>
     *   <li>생성자/수정자 정보 다양화를 위한 인증 컨텍스트 변경</li>
     * </ul>
     */
    private void createProductTestData() {
        long existingCount = productRepository.count();
        log.info("기존 Product 데이터 개수: {}", existingCount);
        
        if (existingCount > 0) {
            log.info("기존 Product 데이터가 존재하여 생성을 건너뜁니다.");
            return;
        }

        log.info("Product 테스트 데이터 100건 생성 시작...");
        List<Product> products = new ArrayList<>();
        
        for (int i = 0; i < 100; i++) {
            // 각 상품마다 다른 사용자가 생성자로 설정되도록 인증 컨텍스트 변경
            if (i % 10 == 0) {
                changeAuthenticationContext();
            }
            
            String category = PRODUCT_CATEGORIES[random.nextInt(PRODUCT_CATEGORIES.length)];
            String prefix = PRODUCT_NAME_PREFIXES[random.nextInt(PRODUCT_NAME_PREFIXES.length)];
            String name = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];
            String productName = prefix + " " + name + " #" + (i + 1);
            
            // 가격 범위: 1,000원 ~ 500,000원
            int price = (random.nextInt(499) + 1) * 1000;
            
            String description = String.format("%s 카테고리의 %s입니다. 고품질 소재와 뛰어난 기능성을 자랑합니다.", 
                                               category, productName);
            
            Product product = Product.builder()
                    .pname(productName)
                    .price(price)
                    .pdesc(description)
                    .category(category)
                    .build();
            
            // 랜덤하게 1~3개의 상품 이미지 추가
            int imageCount = random.nextInt(3) + 1;
            for (int j = 0; j < imageCount; j++) {
                product.addImageString("product_" + (i + 1) + "_image_" + (j + 1) + ".jpg");
            }
            
            products.add(product);
        }
        
        // Product 일괄 저장
        List<Product> savedProducts = productRepository.saveAll(products);
        log.info("총 {}건의 Product 데이터가 생성되었습니다.", savedProducts.size());
        
        // 일부 상품을 논리적 삭제 처리 (BaseEntity.deleted 사용)
        List<Product> productsToDelete = new ArrayList<>();
        for (Product product : savedProducts) {
            // 10% 확률로 논리적 삭제 처리
            if (random.nextDouble() < 0.1) {
                changeAuthenticationContext(); // 삭제자 정보를 위한 인증 컨텍스트 변경
                product.softDelete(); // BaseEntity의 softDelete() 메서드 사용
                productsToDelete.add(product);
            }
        }
        
        if (!productsToDelete.isEmpty()) {
            productRepository.saveAll(productsToDelete);
            log.info("{}건의 Product가 논리적 삭제 처리되었습니다.", productsToDelete.size());
        }
        log.info("총 {}건의 Product 데이터가 생성되었습니다.", savedProducts.size());
        
        // 활성 상품 기준 카테고리별 통계
        List<Product> activeProducts = savedProducts.stream()
                .filter(p -> !p.isDeleted()) // BaseEntity.deleted 필드 사용
                .toList();
        
        activeProducts.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Product::getCategory,
                        java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> log.info("  - 활성 상품 카테고리 {}: {}건", entry.getKey(), entry.getValue()));
        
        // 활성 상품 기준 가격대별 통계
        long under10k = activeProducts.stream().mapToLong(p -> p.getPrice() < 10000 ? 1 : 0).sum();
        long under50k = activeProducts.stream().mapToLong(p -> p.getPrice() >= 10000 && p.getPrice() < 50000 ? 1 : 0).sum();
        long under100k = activeProducts.stream().mapToLong(p -> p.getPrice() >= 50000 && p.getPrice() < 100000 ? 1 : 0).sum();
        long over100k = activeProducts.stream().mapToLong(p -> p.getPrice() >= 100000 ? 1 : 0).sum();
        
        log.info("활성 상품 가격대별 분포: 1만원 미만({}건), 1-5만원({}건), 5-10만원({}건), 10만원 이상({}건)", 
                 under10k, under50k, under100k, over100k);
        
        // 전체 통계 요약
        long deletedCount = savedProducts.stream().mapToLong(p -> p.isDeleted() ? 1 : 0).sum();
        log.info("상품 생성 완료 - 전체: {}건, 활성: {}건, 삭제: {}건", 
                 savedProducts.size(), activeProducts.size(), deletedCount);
    }

    /**
     * Cart 테스트 데이터를 생성합니다 (회원별로 자동 생성).
     */
    private void createCartTestData() {
        long existingCount = cartRepository.count();
        log.info("기존 Cart 데이터 개수: {}", existingCount);
        
        if (existingCount > 0) {
            log.info("기존 Cart 데이터가 존재하여 생성을 건너뜁니다.");
            return;
        }

        // 활성 회원들에 대해서만 장바구니 생성
        List<Member> allMembers = memberRepository.findAll();
        List<Member> activeMembers = allMembers.stream()
                .filter(Member::getIsActive)
                .collect(java.util.stream.Collectors.toList());
        if (activeMembers.isEmpty()) {
            log.warn("활성 회원이 없어 Cart 데이터를 생성할 수 없습니다.");
            return;
        }

        log.info("Cart 테스트 데이터 생성 시작... (활성 회원 {}명)", activeMembers.size());
        List<Cart> carts = new ArrayList<>();
        
        // 활성 회원의 70%에게만 장바구니 생성
        int memberIndex = 0;
        for (Member member : activeMembers) {
            // 일정 간격으로 인증 컨텍스트 변경
            if (memberIndex % 8 == 0) {
                changeAuthenticationContext();
            }
            
            if (random.nextDouble() < 0.7) {
                Cart cart = Cart.builder()
                        .owner(member)
                        .build();
                carts.add(cart);
            }
            memberIndex++;
        }
        
        List<Cart> savedCarts = cartRepository.saveAll(carts);
        log.info("총 {}건의 Cart 데이터가 생성되었습니다.", savedCarts.size());
    }

    /**
     * CartItem 테스트 데이터 100건을 생성합니다.
     */
    private void createCartItemTestData() {
        long existingCount = cartItemRepository.count();
        log.info("기존 CartItem 데이터 개수: {}", existingCount);
        
        if (existingCount > 0) {
            log.info("기존 CartItem 데이터가 존재하여 생성을 건너뜁니다.");
            return;
        }

        List<Cart> carts = cartRepository.findAll();
        // BaseEntity.deleted = false인 활성 상품만 조회 (논리적 삭제된 상품 제외)
        List<Product> activeProducts = productRepository.findByDeletedFalse();
        
        if (carts.isEmpty() || activeProducts.isEmpty()) {
            log.warn("Cart({})나 활성 Product({})가 없어 CartItem 데이터를 생성할 수 없습니다.", 
                     carts.size(), activeProducts.size());
            return;
        }

        log.info("CartItem 테스트 데이터 100건 생성 시작...");
        List<CartItem> cartItems = new ArrayList<>();
        
        for (int i = 0; i < 100; i++) {
            // 장바구니 아이템마다 다른 사용자가 생성자로 설정되도록 인증 컨텍스트 변경
            if (i % 7 == 0) {
                changeAuthenticationContext();
            }
            
            Cart cart = carts.get(random.nextInt(carts.size()));
            Product product = activeProducts.get(random.nextInt(activeProducts.size()));
            int quantity = random.nextInt(5) + 1; // 1~5개
            
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .qty(quantity)
                    .build();
            
            cartItems.add(cartItem);
        }
        
        List<CartItem> savedCartItems = cartItemRepository.saveAll(cartItems);
        log.info("총 {}건의 CartItem 데이터가 생성되었습니다.", savedCartItems.size());
        
        // 장바구니별 아이템 수 통계
        savedCartItems.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        item -> item.getCart().getCno(),
                        java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted(java.util.Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> log.info("  - Cart #{}: {}개 상품", entry.getKey(), entry.getValue()));
    }

    /**
     * TODO 테스트 데이터 100건을 생성합니다.
     */
    private void createTodoTestData() {
        long existingCount = todoRepository.count();
        log.info("기존 TODO 데이터 개수: {}", existingCount);
        
        if (existingCount > 0) {
            log.info("기존 TODO 데이터가 존재하여 생성을 건너뜁니다.");
            return;
        }

        log.info("TODO 테스트 데이터 100건 생성 시작...");
        List<Todo> todos = new ArrayList<>();
        
        for (int i = 0; i < 100; i++) {
            // 각 할일마다 다른 사용자가 생성자로 설정되도록 인증 컨텍스트 변경
            if (i % 5 == 0) {
                changeAuthenticationContext();
            }
            
            String title = TODO_TITLES[i % TODO_TITLES.length];
            String author = AUTHORS[random.nextInt(AUTHORS.length)];
            boolean isComplete = random.nextBoolean(); // 랜덤하게 완료/미완료 설정
            
            // 제목에 번호를 추가하여 중복 방지
            if (i >= TODO_TITLES.length) {
                title = title + " #" + (i + 1);
            }
            
            Todo todo = Todo.builder()
                    .title(title)
                    .writer(author)
                    .complete(isComplete)
                    .build();
            
            todos.add(todo);
            
            if (i < 10) { // 처음 10건만 로그 출력
                log.debug("TODO 생성: {} - {} (완료: {})", title, author, isComplete);
            }
        }
        
        // 배치로 저장하여 성능 최적화
        List<Todo> savedTodos = todoRepository.saveAll(todos);
        
        log.info("총 {}건의 TODO 데이터가 생성되었습니다.", savedTodos.size());
        
        // 통계 정보 출력
        long completedCount = savedTodos.stream()
                .mapToLong(todo -> todo.getComplete() ? 1 : 0)
                .sum();
        long incompleteCount = savedTodos.size() - completedCount;
        
        log.info("완료된 할일: {}건, 미완료 할일: {}건", completedCount, incompleteCount);
        
        // 작성자별 상위 5명 통계
        savedTodos.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Todo::getWriter,
                        java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> log.info("  - {}: {}건", entry.getKey(), entry.getValue()));
    }
}
