package io.github.amsatrio.spring_crud_demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterDataType;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private String id;
    private Object value;
    @JsonProperty("match_mode")
    private FilterMatchMode matchMode;
    @JsonProperty("data_type")
    private FilterDataType dataType;
}
