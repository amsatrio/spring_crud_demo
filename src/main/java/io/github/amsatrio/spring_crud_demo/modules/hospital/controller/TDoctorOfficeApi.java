package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOffice;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TDoctorOfficeService;
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
@RequestMapping("/v1/t-doctor-office")
public class TDoctorOfficeApi {

    private final TDoctorOfficeService tDoctorOfficeService;
    private final HttpServletRequest httpServletRequest;

    public TDoctorOfficeApi(
            TDoctorOfficeService tDoctorOfficeService,
            HttpServletRequest httpServletRequest) {
        this.tDoctorOfficeService = tDoctorOfficeService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTDoctorOffice(
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
            log.error("tDoctorOffice > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TDoctorOffice>> tDoctorOfficeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tDoctorOfficeService.getPageTDoctorOffice(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tDoctorOfficeResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTDoctorOffice(@PathVariable Long id) {
        Response<TDoctorOffice> tDoctorOfficeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeService.getTDoctorOffice(id));
        return ResponseEntity.status(tDoctorOfficeResponse.getStatus()).body(tDoctorOfficeResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTDoctorOfficeHeader() {
        Response<Object> tDoctorOfficeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TDoctorOffice()));
        return ResponseEntity.status(tDoctorOfficeResponse.getStatus()).body(tDoctorOfficeResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTDoctorOffice(@Valid @RequestBody TDoctorOffice tDoctorOffice) {
        Response<TDoctorOffice> tDoctorOfficeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeService.createTDoctorOffice(tDoctorOffice));
        return ResponseEntity.status(tDoctorOfficeResponse.getStatus()).body(tDoctorOfficeResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTDoctorOffice(@Valid @RequestBody TDoctorOffice tDoctorOffice, @PathVariable Long id) {
        if (!Objects.equals(tDoctorOffice.getId(), id)) {
            tDoctorOffice.setId(id);
        }

        Response<Object> tDoctorOfficeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeService.updateTDoctorOffice(tDoctorOffice));
        return ResponseEntity.status(tDoctorOfficeResponse.getStatus()).body(tDoctorOfficeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTDoctorOffice(@PathVariable Long id) {
        this.tDoctorOfficeService.deleteTDoctorOffice(id);
        Response<Object> tDoctorOfficeResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tDoctorOfficeResponse.getStatus()).body(tDoctorOfficeResponse);
    }
}
