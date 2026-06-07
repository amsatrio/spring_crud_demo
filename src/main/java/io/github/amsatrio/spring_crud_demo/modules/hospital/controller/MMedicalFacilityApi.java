package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacility;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMedicalFacilityService;
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
@RequestMapping("/v1/m-medical-facility")
public class MMedicalFacilityApi {

    private final MMedicalFacilityService mMedicalFacilityService;
    private final HttpServletRequest httpServletRequest;

    public MMedicalFacilityApi(
            MMedicalFacilityService mMedicalFacilityService,
            HttpServletRequest httpServletRequest) {
        this.mMedicalFacilityService = mMedicalFacilityService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMMedicalFacility(
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
            log.error("mMedicalFacility > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MMedicalFacility>> mMedicalFacilityResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mMedicalFacilityService.getPageMMedicalFacility(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mMedicalFacilityResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMMedicalFacility(@PathVariable Long id) {
        Response<MMedicalFacility> mMedicalFacilityResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityService.getMMedicalFacility(id));
        return ResponseEntity.status(mMedicalFacilityResponse.getStatus()).body(mMedicalFacilityResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMMedicalFacilityHeader() {
        Response<Object> mMedicalFacilityResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MMedicalFacility()));
        return ResponseEntity.status(mMedicalFacilityResponse.getStatus()).body(mMedicalFacilityResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMMedicalFacility(@Valid @RequestBody MMedicalFacility mMedicalFacility) {
        Response<MMedicalFacility> mMedicalFacilityResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityService.createMMedicalFacility(mMedicalFacility));
        return ResponseEntity.status(mMedicalFacilityResponse.getStatus()).body(mMedicalFacilityResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMMedicalFacility(@Valid @RequestBody MMedicalFacility mMedicalFacility, @PathVariable Long id) {
        if (!Objects.equals(mMedicalFacility.getId(), id)) {
            mMedicalFacility.setId(id);
        }

        Response<Object> mMedicalFacilityResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityService.updateMMedicalFacility(mMedicalFacility));
        return ResponseEntity.status(mMedicalFacilityResponse.getStatus()).body(mMedicalFacilityResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMMedicalFacility(@PathVariable Long id) {
        this.mMedicalFacilityService.deleteMMedicalFacility(id);
        Response<Object> mMedicalFacilityResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mMedicalFacilityResponse.getStatus()).body(mMedicalFacilityResponse);
    }
}
