package com.belanjaki.id.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
public class PaginationResponse<T> {
    private List<T> items;
    private int totalItems;
    private int itemsPerPage;
    private int currentPage;
    private int totalPages;
    private String nextPageUrl;
    private String prevPageUrl;
}
