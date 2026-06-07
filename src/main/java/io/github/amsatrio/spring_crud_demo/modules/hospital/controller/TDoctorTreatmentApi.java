package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorTreatment;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TDoctorTreatmentService;
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
@RequestMapping("/v1/t-doctor-treatment")
public class TDoctorTreatmentApi {

    private final TDoctorTreatmentService tDoctorTreatmentService;
    private final HttpServletRequest httpServletRequest;

    public TDoctorTreatmentApi(
            TDoctorTreatmentService tDoctorTreatmentService,
            HttpServletRequest httpServletRequest) {
        this.tDoctorTreatmentService = tDoctorTreatmentService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTDoctorTreatment(
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
            log.error("tDoctorTreatment > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TDoctorTreatment>> tDoctorTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tDoctorTreatmentService.getPageTDoctorTreatment(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tDoctorTreatmentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTDoctorTreatment(@PathVariable Long id) {
        Response<TDoctorTreatment> tDoctorTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorTreatmentService.getTDoctorTreatment(id));
        return ResponseEntity.status(tDoctorTreatmentResponse.getStatus()).body(tDoctorTreatmentResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTDoctorTreatmentHeader() {
        Response<Object> tDoctorTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TDoctorTreatment()));
        return ResponseEntity.status(tDoctorTreatmentResponse.getStatus()).body(tDoctorTreatmentResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTDoctorTreatment(@Valid @RequestBody TDoctorTreatment tDoctorTreatment) {
        Response<TDoctorTreatment> tDoctorTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorTreatmentService.createTDoctorTreatment(tDoctorTreatment));
        return ResponseEntity.status(tDoctorTreatmentResponse.getStatus()).body(tDoctorTreatmentResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTDoctorTreatment(@Valid @RequestBody TDoctorTreatment tDoctorTreatment, @PathVariable Long id) {
        if (!Objects.equals(tDoctorTreatment.getId(), id)) {
            tDoctorTreatment.setId(id);
        }

        Response<Object> tDoctorTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorTreatmentService.updateTDoctorTreatment(tDoctorTreatment));
        return ResponseEntity.status(tDoctorTreatmentResponse.getStatus()).body(tDoctorTreatmentResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTDoctorTreatment(@PathVariable Long id) {
        this.tDoctorTreatmentService.deleteTDoctorTreatment(id);
        Response<Object> tDoctorTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tDoctorTreatmentResponse.getStatus()).body(tDoctorTreatmentResponse);
    }
}
