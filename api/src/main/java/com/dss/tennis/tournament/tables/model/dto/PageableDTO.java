package com.dss.tennis.tournament.tables.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableDTO<T> {

    private List<T> page;
    private Integer totalPages;
    private Integer pageSize;
    private Integer currentPage;
}
