package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCourierType;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MCourierTypeService;
import io.github.amsatrio.spring_crud_demo.util.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/v1/m-courier-type")
public class MCourierTypeApi {

    private final MCourierTypeService mCourierTypeService;
    private final HttpServletRequest httpServletRequest;

    public MCourierTypeApi(
            MCourierTypeService mCourierTypeService,
            HttpServletRequest httpServletRequest) {
        this.mCourierTypeService = mCourierTypeService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMCourierType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String sort,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "") String search) {

        List<FilterRequest> filters = new ArrayList<>();
        List<SortRequest> sorts = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (!filter.isEmpty()) {
                List<Object> objectList = objectMapper.readValue(filter, new TypeReference<List<Object>>() {
                });
                for (Object object : objectList) {
                    filters.add(objectMapper.convertValue(object, FilterRequest.class));
                }
            }
            if (!sort.isEmpty()) {
                sorts = objectMapper.readValue(sort, new TypeReference<List<SortRequest>>() {
                });
            }
        } catch (Exception exception) {
            log.error("mCourierType > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MCourierType>> mCourierTypeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mCourierTypeService.getPageMCourierType(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mCourierTypeResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMCourierType(@PathVariable Long id) {
        Response<MCourierType> mCourierTypeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCourierTypeService.getMCourierType(id));
        return ResponseEntity.status(mCourierTypeResponse.getStatus()).body(mCourierTypeResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMCourierTypeHeader() {
        Response<Object> mCourierTypeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MCourierType()));
        return ResponseEntity.status(mCourierTypeResponse.getStatus()).body(mCourierTypeResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMCourierType(@Valid @RequestBody MCourierType mCourierType) {
        Response<MCourierType> mCourierTypeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCourierTypeService.createMCourierType(mCourierType));
        return ResponseEntity.status(mCourierTypeResponse.getStatus()).body(mCourierTypeResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMCourierType(@Valid @RequestBody MCourierType mCourierType, @PathVariable Long id) {
        if (!Objects.equals(mCourierType.getId(), id)) {
            mCourierType.setId(id);
        }

        Response<Object> mCourierTypeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCourierTypeService.updateMCourierType(mCourierType));
        return ResponseEntity.status(mCourierTypeResponse.getStatus()).body(mCourierTypeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMCourierType(@PathVariable Long id) {
        this.mCourierTypeService.deleteMCourierType(id);
        Response<Object> mCourierTypeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mCourierTypeResponse.getStatus()).body(mCourierTypeResponse);
    }
}
