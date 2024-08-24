package kma.cnpm.beapp.domain.common.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> implements Serializable {
    int pageSize;
    int totalElements;
    int totalPages;
    int pageNo;
    T items;
}
