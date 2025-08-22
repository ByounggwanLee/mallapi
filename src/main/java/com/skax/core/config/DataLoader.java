package com.skax.core.config;

import com.skax.core.entity.todo.Todo;
import com.skax.core.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 애플리케이션 시작 시 테스트 데이터를 생성하는 DataLoader
 * 
 * <p>프로젝트 시작 시 TODO 테스트 데이터 50건을 자동으로 생성합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-21
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TodoRepository todoRepository;
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

    @Override
    public void run(String... args) throws Exception {
        log.info("=== 데이터 로딩 시작 ===");
        
        // 기존 데이터 확인
        long existingCount = todoRepository.count();
        log.info("기존 TODO 데이터 개수: {}", existingCount);
        
        if (existingCount == 0) {
            log.info("TODO 테스트 데이터 50건 생성 시작...");
            createTodoTestData();
            log.info("TODO 테스트 데이터 생성 완료!");
        } else {
            log.info("기존 TODO 데이터가 존재하여 테스트 데이터 생성을 건너뜁니다.");
        }
        
        log.info("=== 데이터 로딩 완료 ===");
    }

    /**
     * TODO 테스트 데이터 50건을 생성합니다.
     */
    private void createTodoTestData() {
        List<Todo> todos = new ArrayList<>();
        
        for (int i = 0; i < 50; i++) {
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
            
            log.debug("TODO 생성: {} - {} (완료: {})", title, author, isComplete);
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
        log.info("작성자별 분포:");
        
        // 작성자별 통계
        savedTodos.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Todo::getWriter,
                        java.util.stream.Collectors.counting()))
                .forEach((writer, count) -> 
                        log.info("  - {}: {}건", writer, count));
    }
}
