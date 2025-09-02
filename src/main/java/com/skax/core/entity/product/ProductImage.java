package com.skax.core.entity.product;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 상품 이미지 정보를 나타내는 임베디드 클래스
 * 
 * <p>상품과 연관된 이미지 파일의 정보를 저장합니다. 다음과 같은 정보를 포함합니다:</p>
 * <ul>
 *   <li>이미지 파일명 (fileName)</li>
 *   <li>이미지 순서 (ord)</li>
 * </ul>
 * 
 * <p>@Embeddable로 선언되어 Product 엔티티에 포함되어 저장됩니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    /**
     * 이미지 파일명
     * 서버에 저장된 이미지 파일의 이름입니다.
     */
    private String fileName;

    /**
     * 이미지 순서
     * 상품 이미지들의 표시 순서를 나타냅니다.
     * 0부터 시작하여 순차적으로 증가합니다.
     */
    private int ord;

    /**
     * 이미지의 순서를 설정합니다.
     * 
     * @param ord 이미지 순서 (0 이상의 값)
     * @throws IllegalArgumentException ord가 음수인 경우
     */
    public void setOrd(int ord) {
        this.ord = ord;
    }

}
