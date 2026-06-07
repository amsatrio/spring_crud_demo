package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointmentRescheduleHistory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TAppointmentRescheduleHistoryService;
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
@RequestMapping("/v1/t-appointment-reschedule-history")
public class TAppointmentRescheduleHistoryApi {

    private final TAppointmentRescheduleHistoryService tAppointmentRescheduleHistoryService;
    private final HttpServletRequest httpServletRequest;

    public TAppointmentRescheduleHistoryApi(
            TAppointmentRescheduleHistoryService tAppointmentRescheduleHistoryService,
            HttpServletRequest httpServletRequest) {
        this.tAppointmentRescheduleHistoryService = tAppointmentRescheduleHistoryService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTAppointmentRescheduleHistory(
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
            log.error("tAppointmentRescheduleHistory > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TAppointmentRescheduleHistory>> tAppointmentRescheduleHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tAppointmentRescheduleHistoryService.getPageTAppointmentRescheduleHistory(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tAppointmentRescheduleHistoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTAppointmentRescheduleHistory(@PathVariable Long id) {
        Response<TAppointmentRescheduleHistory> tAppointmentRescheduleHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentRescheduleHistoryService.getTAppointmentRescheduleHistory(id));
        return ResponseEntity.status(tAppointmentRescheduleHistoryResponse.getStatus()).body(tAppointmentRescheduleHistoryResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTAppointmentRescheduleHistoryHeader() {
        Response<Object> tAppointmentRescheduleHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TAppointmentRescheduleHistory()));
        return ResponseEntity.status(tAppointmentRescheduleHistoryResponse.getStatus()).body(tAppointmentRescheduleHistoryResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTAppointmentRescheduleHistory(@Valid @RequestBody TAppointmentRescheduleHistory tAppointmentRescheduleHistory) {
        Response<TAppointmentRescheduleHistory> tAppointmentRescheduleHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentRescheduleHistoryService.createTAppointmentRescheduleHistory(tAppointmentRescheduleHistory));
        return ResponseEntity.status(tAppointmentRescheduleHistoryResponse.getStatus()).body(tAppointmentRescheduleHistoryResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTAppointmentRescheduleHistory(@Valid @RequestBody TAppointmentRescheduleHistory tAppointmentRescheduleHistory, @PathVariable Long id) {
        if (!Objects.equals(tAppointmentRescheduleHistory.getId(), id)) {
            tAppointmentRescheduleHistory.setId(id);
        }

        Response<Object> tAppointmentRescheduleHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentRescheduleHistoryService.updateTAppointmentRescheduleHistory(tAppointmentRescheduleHistory));
        return ResponseEntity.status(tAppointmentRescheduleHistoryResponse.getStatus()).body(tAppointmentRescheduleHistoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTAppointmentRescheduleHistory(@PathVariable Long id) {
        this.tAppointmentRescheduleHistoryService.deleteTAppointmentRescheduleHistory(id);
        Response<Object> tAppointmentRescheduleHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tAppointmentRescheduleHistoryResponse.getStatus()).body(tAppointmentRescheduleHistoryResponse);
    }
}
