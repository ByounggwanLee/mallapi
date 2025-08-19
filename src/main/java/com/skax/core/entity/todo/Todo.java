package com.skax.core.entity.todo;

import com.skax.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 할일 정보를 나타내는 엔티티 클래스
 * 
 * <p>사용자의 할일 목록을 관리합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@Table(name = "todos", indexes = {
    @Index(name = "idx_todo_writer", columnList = "writer"),
    @Index(name = "idx_todo_complete", columnList = "complete"),
    @Index(name = "idx_todo_writer_complete", columnList = "writer, complete")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo extends BaseEntity {

    /**
     * 할일 번호 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tno")
    private Long tno;

    /**
     * 제목
     */
    @Column(name = "title", nullable = false, length = 256)
    private String title;

    /**
     * 작성자
     */
    @Column(name = "writer", nullable = false, length = 256)
    private String writer;

    /**
     * 완료 여부
     */
    @Column(name = "complete", nullable = false)
    @Builder.Default
    private Boolean complete = false;

    /**
     * 할일 완료 처리
     */
    public void markAsCompleted() {
        this.complete = true;
    }

    /**
     * 할일 미완료 처리
     */
    public void markAsIncomplete() {
        this.complete = false;
    }

    /**
     * 할일 제목 수정
     * 
     * @param title 새로운 제목
     */
    public void updateTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title.trim();
        }
    }

    /**
     * 작성자 수정
     * 
     * @param writer 새로운 작성자
     */
    public void updateWriter(String writer) {
        if (writer != null && !writer.trim().isEmpty()) {
            this.writer = writer.trim();
        }
    }

    /**
     * 완료 상태 토글
     */
    public void toggleComplete() {
        this.complete = !this.complete;
    }

    /**
     * 할일이 완료되었는지 확인
     * 
     * @return 완료 여부
     */
    public boolean isCompleted() {
        return Boolean.TRUE.equals(this.complete);
    }

    /**
     * 할일 전체 정보 업데이트
     * 
     * @param title 제목
     * @param writer 작성자
     * @param complete 완료 여부
     */
    public void updateTodo(String title, String writer, Boolean complete) {
        updateTitle(title);
        updateWriter(writer);
        if (complete != null) {
            this.complete = complete;
        }
    }
}
