package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacilityCategory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMedicalFacilityCategoryService;
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
@RequestMapping("/v1/m-medical-facility-category")
public class MMedicalFacilityCategoryApi {

    private final MMedicalFacilityCategoryService mMedicalFacilityCategoryService;
    private final HttpServletRequest httpServletRequest;

    public MMedicalFacilityCategoryApi(
            MMedicalFacilityCategoryService mMedicalFacilityCategoryService,
            HttpServletRequest httpServletRequest) {
        this.mMedicalFacilityCategoryService = mMedicalFacilityCategoryService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMMedicalFacilityCategory(
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
            log.error("mMedicalFacilityCategory > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MMedicalFacilityCategory>> mMedicalFacilityCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mMedicalFacilityCategoryService.getPageMMedicalFacilityCategory(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mMedicalFacilityCategoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMMedicalFacilityCategory(@PathVariable Long id) {
        Response<MMedicalFacilityCategory> mMedicalFacilityCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityCategoryService.getMMedicalFacilityCategory(id));
        return ResponseEntity.status(mMedicalFacilityCategoryResponse.getStatus()).body(mMedicalFacilityCategoryResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMMedicalFacilityCategoryHeader() {
        Response<Object> mMedicalFacilityCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MMedicalFacilityCategory()));
        return ResponseEntity.status(mMedicalFacilityCategoryResponse.getStatus()).body(mMedicalFacilityCategoryResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMMedicalFacilityCategory(@Valid @RequestBody MMedicalFacilityCategory mMedicalFacilityCategory) {
        Response<MMedicalFacilityCategory> mMedicalFacilityCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityCategoryService.createMMedicalFacilityCategory(mMedicalFacilityCategory));
        return ResponseEntity.status(mMedicalFacilityCategoryResponse.getStatus()).body(mMedicalFacilityCategoryResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMMedicalFacilityCategory(@Valid @RequestBody MMedicalFacilityCategory mMedicalFacilityCategory, @PathVariable Long id) {
        if (!Objects.equals(mMedicalFacilityCategory.getId(), id)) {
            mMedicalFacilityCategory.setId(id);
        }

        Response<Object> mMedicalFacilityCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityCategoryService.updateMMedicalFacilityCategory(mMedicalFacilityCategory));
        return ResponseEntity.status(mMedicalFacilityCategoryResponse.getStatus()).body(mMedicalFacilityCategoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMMedicalFacilityCategory(@PathVariable Long id) {
        this.mMedicalFacilityCategoryService.deleteMMedicalFacilityCategory(id);
        Response<Object> mMedicalFacilityCategoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mMedicalFacilityCategoryResponse.getStatus()).body(mMedicalFacilityCategoryResponse);
    }
}
