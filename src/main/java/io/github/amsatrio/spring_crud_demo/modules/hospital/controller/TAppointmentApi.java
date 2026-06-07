package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointment;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TAppointmentService;
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
@RequestMapping("/v1/t-appointment")
public class TAppointmentApi {

    private final TAppointmentService tAppointmentService;
    private final HttpServletRequest httpServletRequest;

    public TAppointmentApi(
            TAppointmentService tAppointmentService,
            HttpServletRequest httpServletRequest) {
        this.tAppointmentService = tAppointmentService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTAppointment(
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
            log.error("tAppointment > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TAppointment>> tAppointmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tAppointmentService.getPageTAppointment(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tAppointmentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTAppointment(@PathVariable Long id) {
        Response<TAppointment> tAppointmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentService.getTAppointment(id));
        return ResponseEntity.status(tAppointmentResponse.getStatus()).body(tAppointmentResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTAppointmentHeader() {
        Response<Object> tAppointmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TAppointment()));
        return ResponseEntity.status(tAppointmentResponse.getStatus()).body(tAppointmentResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTAppointment(@Valid @RequestBody TAppointment tAppointment) {
        Response<TAppointment> tAppointmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentService.createTAppointment(tAppointment));
        return ResponseEntity.status(tAppointmentResponse.getStatus()).body(tAppointmentResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTAppointment(@Valid @RequestBody TAppointment tAppointment, @PathVariable Long id) {
        if (!Objects.equals(tAppointment.getId(), id)) {
            tAppointment.setId(id);
        }

        Response<Object> tAppointmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentService.updateTAppointment(tAppointment));
        return ResponseEntity.status(tAppointmentResponse.getStatus()).body(tAppointmentResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTAppointment(@PathVariable Long id) {
        this.tAppointmentService.deleteTAppointment(id);
        Response<Object> tAppointmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tAppointmentResponse.getStatus()).body(tAppointmentResponse);
    }
}
