package com.skax.core.repository.todo;

import com.skax.core.entity.todo.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Todo 엔티티에 대한 데이터 액세스 계층 인터페이스
 * 
 * <p>할일 데이터의 생성, 조회, 수정, 삭제 등의 데이터베이스 작업을 처리합니다.</p>
 * 
 * <p>제공하는 주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업</li>
 *   <li>작성자별 할일 조회</li>
 *   <li>완료 상태별 할일 조회</li>
 *   <li>제목 검색 기능</li>
 *   <li>복합 조건 검색</li>
 *   <li>페이징 처리</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * 작성자별 할일 목록을 페이징으로 조회합니다.
     * 
     * @param writer 작성자
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    Page<Todo> findByWriter(String writer, Pageable pageable);

    /**
     * 완료 상태별 할일 목록을 페이징으로 조회합니다.
     * 
     * @param complete 완료 여부
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    Page<Todo> findByComplete(Boolean complete, Pageable pageable);

    /**
     * 작성자와 완료 상태로 할일 목록을 조회합니다.
     * 
     * @param writer 작성자
     * @param complete 완료 여부
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    Page<Todo> findByWriterAndComplete(String writer, Boolean complete, Pageable pageable);

    /**
     * 제목에 특정 키워드가 포함된 할일 목록을 조회합니다.
     * 
     * @param title 제목 키워드
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    Page<Todo> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * 작성자별 완료된 할일 개수를 조회합니다.
     * 
     * @param writer 작성자
     * @return 완료된 할일 개수
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.writer = :writer AND t.complete = true")
    long countCompletedByWriter(@Param("writer") String writer);

    /**
     * 작성자별 미완료된 할일 개수를 조회합니다.
     * 
     * @param writer 작성자
     * @return 미완료된 할일 개수
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.writer = :writer AND t.complete = false")
    long countIncompleteByWriter(@Param("writer") String writer);

    /**
     * 작성자의 모든 할일 목록을 조회합니다.
     * 
     * @param writer 작성자
     * @return 할일 목록
     */
    List<Todo> findByWriterOrderByCreatedAtDesc(String writer);

    /**
     * 최근 완료된 할일 목록을 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 최근 완료된 할일 목록
     */
    @Query("SELECT t FROM Todo t WHERE t.complete = true ORDER BY t.updatedAt DESC")
    Page<Todo> findRecentlyCompleted(Pageable pageable);

    /**
     * 제목과 작성자로 할일을 검색합니다.
     * 
     * @param title 제목 키워드
     * @param writer 작성자
     * @param pageable 페이징 정보
     * @return 검색된 할일 목록
     */
    @Query("SELECT t FROM Todo t WHERE " +
           "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:writer IS NULL OR t.writer = :writer)")
    Page<Todo> searchTodos(@Param("title") String title, 
                          @Param("writer") String writer, 
                          Pageable pageable);

    /**
     * 할일 번호로 조회합니다.
     * 
     * @param tno 할일 번호
     * @return 할일 정보
     */
    Optional<Todo> findByTno(Long tno);

    /**
     * 할일 번호와 작성자로 조회합니다 (권한 확인용).
     * 
     * @param tno 할일 번호
     * @param writer 작성자
     * @return 할일 정보
     */
    Optional<Todo> findByTnoAndWriter(Long tno, String writer);

    /**
     * 작성자별 할일 존재 여부를 확인합니다.
     * 
     * @param writer 작성자
     * @return 존재 여부
     */
    boolean existsByWriter(String writer);

    /**
     * 작성자별 할일 개수를 조회합니다.
     * 
     * @param writer 작성자
     * @return 작성자별 할일 개수
     */
    long countByWriter(String writer);

    /**
     * 완료 상태별 할일 개수를 조회합니다.
     * 
     * @param complete 완료 상태
     * @return 완료 상태별 할일 개수
     */
    long countByComplete(Boolean complete);

    /**
     * 작성자별 모든 할일을 삭제합니다.
     * 
     * @param writer 작성자
     */
    void deleteByWriter(String writer);
}
