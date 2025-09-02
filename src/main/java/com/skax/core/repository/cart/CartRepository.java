package com.skax.core.repository.cart;

import com.skax.core.entity.cart.Cart;
import com.skax.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Cart 엔티티에 대한 데이터 액세스 계층 인터페이스
 * 
 * <p>장바구니 데이터의 생성, 조회, 수정, 삭제 등의 데이터베이스 작업을 처리합니다.</p>
 * 
 * <p>제공하는 주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업</li>
 *   <li>회원별 장바구니 조회</li>
 *   <li>장바구니 소유자 확인</li>
 *   <li>장바구니 존재 여부 확인</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * 특정 회원의 장바구니를 조회합니다.
     * 
     * @param owner 장바구니 소유자 (회원)
     * @return 해당 회원의 장바구니
     */
    Optional<Cart> findByOwner(Member owner);

    /**
     * 회원 이메일로 장바구니를 조회합니다.
     * 
     * @param email 회원 이메일
     * @return 해당 회원의 장바구니
     */
    @Query("SELECT c FROM Cart c WHERE c.owner.email = :email")
    Optional<Cart> findByOwnerEmail(@Param("email") String email);

    /**
     * 특정 회원이 장바구니를 가지고 있는지 확인합니다.
     * 
     * @param owner 장바구니 소유자 (회원)
     * @return 장바구니 존재 여부
     */
    boolean existsByOwner(Member owner);

    /**
     * 회원 이메일로 장바구니 존재 여부를 확인합니다.
     * 
     * @param email 회원 이메일
     * @return 장바구니 존재 여부
     */
    @Query("SELECT COUNT(c) > 0 FROM Cart c WHERE c.owner.email = :email")
    boolean existsByOwnerEmail(@Param("email") String email);

    /**
     * 특정 회원의 장바구니를 삭제합니다.
     * 
     * @param owner 장바구니 소유자 (회원)
     * @return 삭제된 장바구니 수
     */
    long deleteByOwner(Member owner);

    /**
     * 회원 이메일로 장바구니를 삭제합니다.
     * 
     * @param email 회원 이메일
     * @return 삭제된 장바구니 수
     */
    @Query("DELETE FROM Cart c WHERE c.owner.email = :email")
    long deleteByOwnerEmail(@Param("email") String email);
}
