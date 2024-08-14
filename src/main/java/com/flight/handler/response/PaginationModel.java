package com.flight.handler.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationModel<T> {
    private Integer indexFrom;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer totalCount;
    private Integer totalPages;
    private Boolean hasPreviousPage;
    private Boolean hasNextPage;

    public PaginationModel(Page<T> page) {
        this.indexFrom = Math.toIntExact(page.getPageable().getOffset());
        this.pageIndex = page.getPageable().getPageNumber();
        this.pageSize = page.getPageable().getPageSize();
        this.totalCount = Math.toIntExact(page.getTotalElements());
        this.totalPages = page.getTotalPages();
        this.hasPreviousPage = page.hasPrevious();
        this.hasNextPage = page.hasNext();
    }
}
