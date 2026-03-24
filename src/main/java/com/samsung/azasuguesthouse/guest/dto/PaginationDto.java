package com.samsung.azasuguesthouse.guest.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaginationDto<T> {
    private int currentPage;
    private int maxPage;

    private List<T> list;

    public PaginationDto(int currentPage, int maxPage, List<T> list) {
        this.currentPage = currentPage;
        this.maxPage = maxPage;
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public List<T> getList() {
        return list;
    }
}

