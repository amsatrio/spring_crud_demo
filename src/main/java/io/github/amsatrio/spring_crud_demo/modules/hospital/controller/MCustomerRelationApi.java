package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomerRelation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MCustomerRelationService;
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
@RequestMapping("/v1/m-customer-relation")
public class MCustomerRelationApi {

    private final MCustomerRelationService mCustomerRelationService;
    private final HttpServletRequest httpServletRequest;

    public MCustomerRelationApi(
            MCustomerRelationService mCustomerRelationService,
            HttpServletRequest httpServletRequest) {
        this.mCustomerRelationService = mCustomerRelationService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMCustomerRelation(
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
            log.error("mCustomerRelation > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MCustomerRelation>> mCustomerRelationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mCustomerRelationService.getPageMCustomerRelation(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mCustomerRelationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMCustomerRelation(@PathVariable Long id) {
        Response<MCustomerRelation> mCustomerRelationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerRelationService.getMCustomerRelation(id));
        return ResponseEntity.status(mCustomerRelationResponse.getStatus()).body(mCustomerRelationResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMCustomerRelationHeader() {
        Response<Object> mCustomerRelationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MCustomerRelation()));
        return ResponseEntity.status(mCustomerRelationResponse.getStatus()).body(mCustomerRelationResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMCustomerRelation(@Valid @RequestBody MCustomerRelation mCustomerRelation) {
        Response<MCustomerRelation> mCustomerRelationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerRelationService.createMCustomerRelation(mCustomerRelation));
        return ResponseEntity.status(mCustomerRelationResponse.getStatus()).body(mCustomerRelationResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMCustomerRelation(@Valid @RequestBody MCustomerRelation mCustomerRelation, @PathVariable Long id) {
        if (!Objects.equals(mCustomerRelation.getId(), id)) {
            mCustomerRelation.setId(id);
        }

        Response<Object> mCustomerRelationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerRelationService.updateMCustomerRelation(mCustomerRelation));
        return ResponseEntity.status(mCustomerRelationResponse.getStatus()).body(mCustomerRelationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMCustomerRelation(@PathVariable Long id) {
        this.mCustomerRelationService.deleteMCustomerRelation(id);
        Response<Object> mCustomerRelationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mCustomerRelationResponse.getStatus()).body(mCustomerRelationResponse);
    }
}
