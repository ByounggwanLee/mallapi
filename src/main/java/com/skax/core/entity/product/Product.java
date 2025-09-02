package com.skax.core.entity.product;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 상품 정보를 나타내는 엔티티 클래스
 * 
 * <p>쇼핑몰에서 판매되는 상품의 기본 정보를 관리합니다. 다음과 같은 정보를 포함합니다:</p>
 * <ul>
 *   <li>상품 고유 번호 (pno)</li>
 *   <li>상품명 (pname)</li>
 *   <li>가격 (price)</li>
 *   <li>상품 설명 (pdesc)</li>
 *   <li>삭제 플래그 (delFlag)</li>
 *   <li>상품 이미지 목록 (imageList)</li>
 * </ul>
 * 
 * <p>상품의 가격 변경, 삭제 상태 변경 등의 비즈니스 로직을 포함합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@Table(name = "tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    /**
     * 상품 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    /**
     * 상품명
     * 고객에게 표시되는 상품의 이름입니다.
     */
    private String pname;

    /**
     * 상품 가격
     * 원(KRW) 단위의 상품 판매가격입니다.
     */
    private int price;

    /**
     * 상품 설명
     * 상품에 대한 상세한 설명입니다.
     */
    private String pdesc;

    /**
     * 상품 카테고리
     * 상품이 속한 카테고리 정보입니다.
     */
    private String category;

    /**
     * 삭제 플래그
     * true: 삭제된 상품, false: 활성 상품
     * 실제 데이터는 삭제하지 않고 논리적 삭제를 위해 사용됩니다.
     */
    private boolean delFlag;

    /**
     * 상품의 삭제 상태를 변경합니다.
     * 
     * @param delFlag 삭제 플래그 (true: 삭제, false: 활성)
     */
    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 상품 이미지 목록
     * 상품과 연관된 이미지들의 정보를 저장합니다.
     */
    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    /**
     * 상품의 가격을 변경합니다.
     * 
     * @param price 새로운 가격 (0 이상의 값)
     * @throws IllegalArgumentException price가 음수인 경우
     */
    public void changePrice(int price) {
        this.price = price;
    }

    /**
     * 상품의 설명을 변경합니다.
     * 
     * @param desc 새로운 상품 설명
     */
    public void changeDesc(String desc) {
        this.pdesc = desc;
    }

    /**
     * 상품명을 변경합니다.
     * 
     * @param name 새로운 상품명
     * @throws IllegalArgumentException name이 null이거나 빈 문자열인 경우
     */
    public void changeName(String name) {
        this.pname = name;
    }

    /**
     * 상품 카테고리를 변경합니다.
     * 
     * @param category 새로운 카테고리
     */
    public void changeCategory(String category) {
        this.category = category;
    }

    /**
     * 상품에 이미지를 추가합니다.
     * 이미지의 순서는 현재 이미지 목록의 크기를 기준으로 자동 설정됩니다.
     * 
     * @param image 추가할 상품 이미지
     * @throws IllegalArgumentException image가 null인 경우
     */
    public void addImage(ProductImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    /**
     * 파일명을 이용하여 상품 이미지를 추가합니다.
     * ProductImage 객체를 생성하여 이미지 목록에 추가합니다.
     * 
     * @param fileName 이미지 파일명
     * @throws IllegalArgumentException fileName이 null이거나 빈 문자열인 경우
     */
    public void addImageString(String fileName) {
        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();
        addImage(productImage);
    }

    /**
     * 상품의 모든 이미지를 제거합니다.
     * 이미지 목록을 초기화하여 빈 상태로 만듭니다.
     */
    public void clearList() {
        this.imageList.clear();
    }
}
