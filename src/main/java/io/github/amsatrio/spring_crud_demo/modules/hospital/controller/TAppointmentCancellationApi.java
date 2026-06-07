package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointmentCancellation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TAppointmentCancellationService;
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
@RequestMapping("/v1/t-appointment-cancellation")
public class TAppointmentCancellationApi {

    private final TAppointmentCancellationService tAppointmentCancellationService;
    private final HttpServletRequest httpServletRequest;

    public TAppointmentCancellationApi(
            TAppointmentCancellationService tAppointmentCancellationService,
            HttpServletRequest httpServletRequest) {
        this.tAppointmentCancellationService = tAppointmentCancellationService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTAppointmentCancellation(
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
            log.error("tAppointmentCancellation > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TAppointmentCancellation>> tAppointmentCancellationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tAppointmentCancellationService.getPageTAppointmentCancellation(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tAppointmentCancellationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTAppointmentCancellation(@PathVariable Long id) {
        Response<TAppointmentCancellation> tAppointmentCancellationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentCancellationService.getTAppointmentCancellation(id));
        return ResponseEntity.status(tAppointmentCancellationResponse.getStatus()).body(tAppointmentCancellationResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTAppointmentCancellationHeader() {
        Response<Object> tAppointmentCancellationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TAppointmentCancellation()));
        return ResponseEntity.status(tAppointmentCancellationResponse.getStatus()).body(tAppointmentCancellationResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTAppointmentCancellation(@Valid @RequestBody TAppointmentCancellation tAppointmentCancellation) {
        Response<TAppointmentCancellation> tAppointmentCancellationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentCancellationService.createTAppointmentCancellation(tAppointmentCancellation));
        return ResponseEntity.status(tAppointmentCancellationResponse.getStatus()).body(tAppointmentCancellationResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTAppointmentCancellation(@Valid @RequestBody TAppointmentCancellation tAppointmentCancellation, @PathVariable Long id) {
        if (!Objects.equals(tAppointmentCancellation.getId(), id)) {
            tAppointmentCancellation.setId(id);
        }

        Response<Object> tAppointmentCancellationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentCancellationService.updateTAppointmentCancellation(tAppointmentCancellation));
        return ResponseEntity.status(tAppointmentCancellationResponse.getStatus()).body(tAppointmentCancellationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTAppointmentCancellation(@PathVariable Long id) {
        this.tAppointmentCancellationService.deleteTAppointmentCancellation(id);
        Response<Object> tAppointmentCancellationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tAppointmentCancellationResponse.getStatus()).body(tAppointmentCancellationResponse);
    }
}
