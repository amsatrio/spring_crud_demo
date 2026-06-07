package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItemCategory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMedicalItemCategoryService;
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
@RequestMapping("/v1/m-medical-item-category")
public class MMedicalItemCategoryApi {

    private final MMedicalItemCategoryService mMedicalItemCategoryService;
    private final HttpServletRequest httpServletRequest;

    public MMedicalItemCategoryApi(
            MMedicalItemCategoryService mMedicalItemCategoryService,
            HttpServletRequest httpServletRequest) {
        this.mMedicalItemCategoryService = mMedicalItemCategoryService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMMedicalItemCategory(
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
            log.error("mMedicalItemCategory > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MMedicalItemCategory>> mMedicalItemCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mMedicalItemCategoryService.getPageMMedicalItemCategory(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mMedicalItemCategoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMMedicalItemCategory(@PathVariable Long id) {
        Response<MMedicalItemCategory> mMedicalItemCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemCategoryService.getMMedicalItemCategory(id));
        return ResponseEntity.status(mMedicalItemCategoryResponse.getStatus()).body(mMedicalItemCategoryResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMMedicalItemCategoryHeader() {
        Response<Object> mMedicalItemCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MMedicalItemCategory()));
        return ResponseEntity.status(mMedicalItemCategoryResponse.getStatus()).body(mMedicalItemCategoryResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMMedicalItemCategory(@Valid @RequestBody MMedicalItemCategory mMedicalItemCategory) {
        Response<MMedicalItemCategory> mMedicalItemCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemCategoryService.createMMedicalItemCategory(mMedicalItemCategory));
        return ResponseEntity.status(mMedicalItemCategoryResponse.getStatus()).body(mMedicalItemCategoryResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMMedicalItemCategory(@Valid @RequestBody MMedicalItemCategory mMedicalItemCategory, @PathVariable Long id) {
        if (!Objects.equals(mMedicalItemCategory.getId(), id)) {
            mMedicalItemCategory.setId(id);
        }

        Response<Object> mMedicalItemCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemCategoryService.updateMMedicalItemCategory(mMedicalItemCategory));
        return ResponseEntity.status(mMedicalItemCategoryResponse.getStatus()).body(mMedicalItemCategoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMMedicalItemCategory(@PathVariable Long id) {
        this.mMedicalItemCategoryService.deleteMMedicalItemCategory(id);
        Response<Object> mMedicalItemCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mMedicalItemCategoryResponse.getStatus()).body(mMedicalItemCategoryResponse);
    }
}
