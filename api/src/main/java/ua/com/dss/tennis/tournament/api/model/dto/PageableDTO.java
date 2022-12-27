package ua.com.dss.tennis.tournament.api.model.dto;

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
