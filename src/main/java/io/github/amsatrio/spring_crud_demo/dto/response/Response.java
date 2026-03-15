package io.github.amsatrio.spring_crud_demo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable{
    private String path;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

    public String toJsonString(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch(Exception exception) {
            log.error("error:", exception);
        }

        return null;
    }
}
