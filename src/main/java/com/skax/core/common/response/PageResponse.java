package com.skax.core.common.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 페이징 처리된 데이터를 위한 응답 클래스
 * 
 * @param <T> 응답 데이터 타입
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Getter
@Builder
public class PageResponse<T> {

    /**
     * 현재 페이지 번호 (0부터 시작)
     */
    private final int page;

    /**
     * 페이지 크기
     */
    private final int size;

    /**
     * 전체 요소 수
     */
    private final long totalElements;

    /**
     * 전체 페이지 수
     */
    private final int totalPages;

    /**
     * 첫 번째 페이지 여부
     */
    private final boolean first;

    /**
     * 마지막 페이지 여부
     */
    private final boolean last;

    /**
     * 빈 페이지 여부
     */
    private final boolean empty;

    /**
     * 페이지 데이터
     */
    private final List<T> content;

    /**
     * 페이징 정보와 컨텐츠로 PageResponse를 생성합니다.
     * 
     * @param content 페이지 데이터
     * @param page 현재 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param totalElements 전체 요소 수
     * @param <T> 데이터 타입
     * @return PageResponse 객체
     */
    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean first = page == 0;
        boolean last = page >= totalPages - 1;
        boolean empty = content.isEmpty();
        
        return PageResponse.<T>builder()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .first(first)
                .last(last)
                .empty(empty)
                .content(content)
                .build();
    }

    /**
     * Spring Data의 Page 객체로부터 PageResponse를 생성합니다.
     * (Spring Data JPA가 클래스패스에 있을 때 사용)
     * 
     * @param page Spring Data Page 객체
     * @param <T> 데이터 타입
     * @return PageResponse 객체
     */
    @SuppressWarnings("unchecked")
    public static <T> PageResponse<T> from(Object page) {
        try {
            // 리플렉션을 사용하여 Page 객체의 메서드 호출
            Class<?> pageClass = page.getClass();
            java.lang.reflect.Method getNumber = pageClass.getMethod("getNumber");
            java.lang.reflect.Method getSize = pageClass.getMethod("getSize");
            java.lang.reflect.Method getTotalElements = pageClass.getMethod("getTotalElements");
            java.lang.reflect.Method getTotalPages = pageClass.getMethod("getTotalPages");
            java.lang.reflect.Method isFirst = pageClass.getMethod("isFirst");
            java.lang.reflect.Method isLast = pageClass.getMethod("isLast");
            java.lang.reflect.Method isEmpty = pageClass.getMethod("isEmpty");
            java.lang.reflect.Method getContent = pageClass.getMethod("getContent");
            
            return PageResponse.<T>builder()
                    .page((Integer) getNumber.invoke(page))
                    .size((Integer) getSize.invoke(page))
                    .totalElements((Long) getTotalElements.invoke(page))
                    .totalPages((Integer) getTotalPages.invoke(page))
                    .first((Boolean) isFirst.invoke(page))
                    .last((Boolean) isLast.invoke(page))
                    .empty((Boolean) isEmpty.invoke(page))
                    .content((List<T>) getContent.invoke(page))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Spring Data Page 객체 변환 중 오류 발생", e);
        }
    }
}
