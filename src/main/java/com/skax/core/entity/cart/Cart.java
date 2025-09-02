package com.skax.core.entity.cart;

import com.skax.core.entity.BaseEntity;
import com.skax.core.entity.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 장바구니 정보를 나타내는 엔티티 클래스
 * 
 * <p>회원의 장바구니를 관리합니다. 다음과 같은 정보를 포함합니다:</p>
 * <ul>
 *   <li>장바구니 고유 번호 (cno)</li>
 *   <li>장바구니 소유자 (owner)</li>
 * </ul>
 * 
 * <p>각 회원은 하나의 장바구니만 가질 수 있으며, 장바구니에는 여러 상품 아이템이 포함될 수 있습니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "owner")
@Table(name = "tbl_cart", indexes = { @Index(name = "idx_cart_email", columnList = "member_owner") })
@EntityListeners(AuditingEntityListener.class)
public class Cart extends BaseEntity {

    /**
     * 장바구니 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    /**
     * 장바구니 소유자
     * 이 장바구니를 소유한 회원 정보입니다.
     * 일대일 관계로 설정되어 각 회원은 하나의 장바구니만 가집니다.
     */
    @OneToOne
    @JoinColumn(name = "member_owner")
    private Member owner;

}
