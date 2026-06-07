package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCurrentDoctorSpecialization;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCurrentDoctorSpecializationService;
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
@RequestMapping("/v1/t-current-doctor-specialization")
public class TCurrentDoctorSpecializationApi {

    private final TCurrentDoctorSpecializationService tCurrentDoctorSpecializationService;
    private final HttpServletRequest httpServletRequest;

    public TCurrentDoctorSpecializationApi(
            TCurrentDoctorSpecializationService tCurrentDoctorSpecializationService,
            HttpServletRequest httpServletRequest) {
        this.tCurrentDoctorSpecializationService = tCurrentDoctorSpecializationService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCurrentDoctorSpecialization(
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
            log.error("tCurrentDoctorSpecialization > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCurrentDoctorSpecialization>> tCurrentDoctorSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCurrentDoctorSpecializationService.getPageTCurrentDoctorSpecialization(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCurrentDoctorSpecializationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCurrentDoctorSpecialization(@PathVariable Long id) {
        Response<TCurrentDoctorSpecialization> tCurrentDoctorSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCurrentDoctorSpecializationService.getTCurrentDoctorSpecialization(id));
        return ResponseEntity.status(tCurrentDoctorSpecializationResponse.getStatus()).body(tCurrentDoctorSpecializationResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCurrentDoctorSpecializationHeader() {
        Response<Object> tCurrentDoctorSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCurrentDoctorSpecialization()));
        return ResponseEntity.status(tCurrentDoctorSpecializationResponse.getStatus()).body(tCurrentDoctorSpecializationResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCurrentDoctorSpecialization(@Valid @RequestBody TCurrentDoctorSpecialization tCurrentDoctorSpecialization) {
        Response<TCurrentDoctorSpecialization> tCurrentDoctorSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCurrentDoctorSpecializationService.createTCurrentDoctorSpecialization(tCurrentDoctorSpecialization));
        return ResponseEntity.status(tCurrentDoctorSpecializationResponse.getStatus()).body(tCurrentDoctorSpecializationResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCurrentDoctorSpecialization(@Valid @RequestBody TCurrentDoctorSpecialization tCurrentDoctorSpecialization, @PathVariable Long id) {
        if (!Objects.equals(tCurrentDoctorSpecialization.getId(), id)) {
            tCurrentDoctorSpecialization.setId(id);
        }

        Response<Object> tCurrentDoctorSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCurrentDoctorSpecializationService.updateTCurrentDoctorSpecialization(tCurrentDoctorSpecialization));
        return ResponseEntity.status(tCurrentDoctorSpecializationResponse.getStatus()).body(tCurrentDoctorSpecializationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCurrentDoctorSpecialization(@PathVariable Long id) {
        this.tCurrentDoctorSpecializationService.deleteTCurrentDoctorSpecialization(id);
        Response<Object> tCurrentDoctorSpecializationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCurrentDoctorSpecializationResponse.getStatus()).body(tCurrentDoctorSpecializationResponse);
    }
}
