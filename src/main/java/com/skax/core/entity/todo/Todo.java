package com.skax.core.entity.todo;

import java.time.LocalDate;

import com.skax.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 할일 정보를 나타내는 엔티티 클래스
 * 
 * <p>사용자의 할일 목록을 관리하며, 다음과 같은 정보를 포함합니다:</p>
 * <ul>
 *   <li>할일 고유 번호 (tno)</li>
 *   <li>할일 제목 (title)</li>
 *   <li>작성자 (writer)</li>
 *   <li>완료 여부 (complete)</li>
 *   <li>마감일 (dueDate)</li>
 * </ul>
 * 
 * <p>할일의 생성, 수정, 완료 처리 등의 기능을 제공합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@Table(name = "tbl_todo")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Todo extends BaseEntity {

    /**
     * 할일 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    /**
     * 할일 제목
     * 사용자가 입력한 할일의 제목을 나타냅니다.
     */
    private String title;

    /**
     * 할일 작성자
     * 할일을 생성한 사용자의 이름 또는 식별자를 나타냅니다.
     */
    private String writer;

    /**
     * 할일 완료 여부
     * true: 완료, false: 미완료를 나타냅니다.
     */
    private boolean complete;

    /**
     * 할일 마감일
     * 할일을 완료해야 하는 날짜를 나타냅니다.
     */
    private LocalDate dueDate;

    /**
     * 할일 제목을 변경합니다.
     * 
     * @param title 새로운 할일 제목
     * @throws IllegalArgumentException title이 null이거나 빈 문자열인 경우
     */
    public void changeTitle(String title) {
        this.title = title;
    }

    /**
     * 할일 완료 상태를 변경합니다.
     * 
     * @param complete 완료 여부 (true: 완료, false: 미완료)
     */
    public void changeComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * 할일 마감일을 변경합니다.
     * 
     * @param dueDate 새로운 마감일
     */
    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * 할일 정보를 업데이트합니다.
     * 
     * @param title 새로운 제목
     * @param writer 새로운 작성자
     * @param complete 새로운 완료 상태
     */
    public void updateTodo(String title, String writer, Boolean complete) {
        if (title != null) {
            this.title = title;
        }
        if (writer != null) {
            this.writer = writer;
        }
        if (complete != null) {
            this.complete = complete;
        }
    }

    /**
     * 할일 완료 상태를 토글합니다.
     */
    public void toggleComplete() {
        this.complete = !this.complete;
    }

    /**
     * 할일 완료 상태를 반환합니다.
     * 
     * @return 완료 상태 (true: 완료, false: 미완료)
     */
    public Boolean getComplete() {
        return this.complete;
    }

}
