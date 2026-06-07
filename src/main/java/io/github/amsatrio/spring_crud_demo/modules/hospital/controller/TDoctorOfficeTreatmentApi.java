package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeTreatment;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TDoctorOfficeTreatmentService;
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
@RequestMapping("/v1/t-doctor-office-treatment")
public class TDoctorOfficeTreatmentApi {

    private final TDoctorOfficeTreatmentService tDoctorOfficeTreatmentService;
    private final HttpServletRequest httpServletRequest;

    public TDoctorOfficeTreatmentApi(
            TDoctorOfficeTreatmentService tDoctorOfficeTreatmentService,
            HttpServletRequest httpServletRequest) {
        this.tDoctorOfficeTreatmentService = tDoctorOfficeTreatmentService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTDoctorOfficeTreatment(
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
            log.error("tDoctorOfficeTreatment > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TDoctorOfficeTreatment>> tDoctorOfficeTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tDoctorOfficeTreatmentService.getPageTDoctorOfficeTreatment(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tDoctorOfficeTreatmentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTDoctorOfficeTreatment(@PathVariable Long id) {
        Response<TDoctorOfficeTreatment> tDoctorOfficeTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeTreatmentService.getTDoctorOfficeTreatment(id));
        return ResponseEntity.status(tDoctorOfficeTreatmentResponse.getStatus()).body(tDoctorOfficeTreatmentResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTDoctorOfficeTreatmentHeader() {
        Response<Object> tDoctorOfficeTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TDoctorOfficeTreatment()));
        return ResponseEntity.status(tDoctorOfficeTreatmentResponse.getStatus()).body(tDoctorOfficeTreatmentResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTDoctorOfficeTreatment(@Valid @RequestBody TDoctorOfficeTreatment tDoctorOfficeTreatment) {
        Response<TDoctorOfficeTreatment> tDoctorOfficeTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeTreatmentService.createTDoctorOfficeTreatment(tDoctorOfficeTreatment));
        return ResponseEntity.status(tDoctorOfficeTreatmentResponse.getStatus()).body(tDoctorOfficeTreatmentResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTDoctorOfficeTreatment(@Valid @RequestBody TDoctorOfficeTreatment tDoctorOfficeTreatment, @PathVariable Long id) {
        if (!Objects.equals(tDoctorOfficeTreatment.getId(), id)) {
            tDoctorOfficeTreatment.setId(id);
        }

        Response<Object> tDoctorOfficeTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeTreatmentService.updateTDoctorOfficeTreatment(tDoctorOfficeTreatment));
        return ResponseEntity.status(tDoctorOfficeTreatmentResponse.getStatus()).body(tDoctorOfficeTreatmentResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTDoctorOfficeTreatment(@PathVariable Long id) {
        this.tDoctorOfficeTreatmentService.deleteTDoctorOfficeTreatment(id);
        Response<Object> tDoctorOfficeTreatmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tDoctorOfficeTreatmentResponse.getStatus()).body(tDoctorOfficeTreatmentResponse);
    }
}
