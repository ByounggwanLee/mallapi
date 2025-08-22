package com.skax.core.common.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 페이징 정보를 담는 클래스
 * 
 * @author ByounggwanLee
 * @since 2025-08-21
 * @version 1.0
 */
@Getter
@Builder
public class Pageable {
    
    /**
     * 현재 페이지 번호 (0부터 시작)
     */
    private final int page;
    
    /**
     * 페이지 크기
     */
    private final int size;
    
    /**
     * 정렬 정보
     */
    private final String sort;
    
    /**
     * 페이지 정보로 Pageable 객체를 생성합니다.
     * 
     * @param page 현재 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return Pageable 객체
     */
    public static Pageable of(int page, int size) {
        return Pageable.builder()
                .page(page)
                .size(size)
                .build();
    }
    
    /**
     * 페이지 정보와 정렬 정보로 Pageable 객체를 생성합니다.
     * 
     * @param page 현재 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sort 정렬 정보
     * @return Pageable 객체
     */
    public static Pageable of(int page, int size, String sort) {
        return Pageable.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .build();
    }
}
