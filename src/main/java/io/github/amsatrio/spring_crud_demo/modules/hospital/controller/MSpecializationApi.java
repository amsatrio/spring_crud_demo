package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MSpecialization;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MSpecializationService;
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
@RequestMapping("/v1/m-specialization")
public class MSpecializationApi {

    private final MSpecializationService mSpecializationService;
    private final HttpServletRequest httpServletRequest;

    public MSpecializationApi(
            MSpecializationService mSpecializationService,
            HttpServletRequest httpServletRequest) {
        this.mSpecializationService = mSpecializationService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMSpecialization(
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
            log.error("mSpecialization > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MSpecialization>> mSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mSpecializationService.getPageMSpecialization(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mSpecializationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMSpecialization(@PathVariable Long id) {
        Response<MSpecialization> mSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mSpecializationService.getMSpecialization(id));
        return ResponseEntity.status(mSpecializationResponse.getStatus()).body(mSpecializationResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMSpecializationHeader() {
        Response<Object> mSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MSpecialization()));
        return ResponseEntity.status(mSpecializationResponse.getStatus()).body(mSpecializationResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMSpecialization(@Valid @RequestBody MSpecialization mSpecialization) {
        Response<MSpecialization> mSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mSpecializationService.createMSpecialization(mSpecialization));
        return ResponseEntity.status(mSpecializationResponse.getStatus()).body(mSpecializationResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMSpecialization(@Valid @RequestBody MSpecialization mSpecialization, @PathVariable Long id) {
        if (!Objects.equals(mSpecialization.getId(), id)) {
            mSpecialization.setId(id);
        }

        Response<Object> mSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mSpecializationService.updateMSpecialization(mSpecialization));
        return ResponseEntity.status(mSpecializationResponse.getStatus()).body(mSpecializationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMSpecialization(@PathVariable Long id) {
        this.mSpecializationService.deleteMSpecialization(id);
        Response<Object> mSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mSpecializationResponse.getStatus()).body(mSpecializationResponse);
    }
}
