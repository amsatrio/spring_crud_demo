package io.github.amsatrio.spring_crud_demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortRequest {
    private String id;
    private boolean desc;
}
