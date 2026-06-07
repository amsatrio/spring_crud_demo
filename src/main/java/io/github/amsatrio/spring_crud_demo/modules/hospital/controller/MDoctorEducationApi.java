package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MDoctorEducation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MDoctorEducationService;
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
@RequestMapping("/v1/m-doctor-education")
public class MDoctorEducationApi {

    private final MDoctorEducationService mDoctorEducationService;
    private final HttpServletRequest httpServletRequest;

    public MDoctorEducationApi(
            MDoctorEducationService mDoctorEducationService,
            HttpServletRequest httpServletRequest) {
        this.mDoctorEducationService = mDoctorEducationService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMDoctorEducation(
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
            log.error("mDoctorEducation > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MDoctorEducation>> mDoctorEducationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mDoctorEducationService.getPageMDoctorEducation(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mDoctorEducationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMDoctorEducation(@PathVariable Long id) {
        Response<MDoctorEducation> mDoctorEducationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mDoctorEducationService.getMDoctorEducation(id));
        return ResponseEntity.status(mDoctorEducationResponse.getStatus()).body(mDoctorEducationResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMDoctorEducationHeader() {
        Response<Object> mDoctorEducationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MDoctorEducation()));
        return ResponseEntity.status(mDoctorEducationResponse.getStatus()).body(mDoctorEducationResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMDoctorEducation(@Valid @RequestBody MDoctorEducation mDoctorEducation) {
        Response<MDoctorEducation> mDoctorEducationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mDoctorEducationService.createMDoctorEducation(mDoctorEducation));
        return ResponseEntity.status(mDoctorEducationResponse.getStatus()).body(mDoctorEducationResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMDoctorEducation(@Valid @RequestBody MDoctorEducation mDoctorEducation, @PathVariable Long id) {
        if (!Objects.equals(mDoctorEducation.getId(), id)) {
            mDoctorEducation.setId(id);
        }

        Response<Object> mDoctorEducationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mDoctorEducationService.updateMDoctorEducation(mDoctorEducation));
        return ResponseEntity.status(mDoctorEducationResponse.getStatus()).body(mDoctorEducationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMDoctorEducation(@PathVariable Long id) {
        this.mDoctorEducationService.deleteMDoctorEducation(id);
        Response<Object> mDoctorEducationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mDoctorEducationResponse.getStatus()).body(mDoctorEducationResponse);
    }
}
