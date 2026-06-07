package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerVaHistory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerVaHistoryService;
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
@RequestMapping("/v1/t-customer-va-history")
public class TCustomerVaHistoryApi {

    private final TCustomerVaHistoryService tCustomerVaHistoryService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerVaHistoryApi(
            TCustomerVaHistoryService tCustomerVaHistoryService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerVaHistoryService = tCustomerVaHistoryService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerVaHistory(
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
            log.error("tCustomerVaHistory > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerVaHistory>> tCustomerVaHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerVaHistoryService.getPageTCustomerVaHistory(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerVaHistoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerVaHistory(@PathVariable Long id) {
        Response<TCustomerVaHistory> tCustomerVaHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerVaHistoryService.getTCustomerVaHistory(id));
        return ResponseEntity.status(tCustomerVaHistoryResponse.getStatus()).body(tCustomerVaHistoryResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerVaHistoryHeader() {
        Response<Object> tCustomerVaHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerVaHistory()));
        return ResponseEntity.status(tCustomerVaHistoryResponse.getStatus()).body(tCustomerVaHistoryResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerVaHistory(@Valid @RequestBody TCustomerVaHistory tCustomerVaHistory) {
        Response<TCustomerVaHistory> tCustomerVaHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerVaHistoryService.createTCustomerVaHistory(tCustomerVaHistory));
        return ResponseEntity.status(tCustomerVaHistoryResponse.getStatus()).body(tCustomerVaHistoryResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerVaHistory(@Valid @RequestBody TCustomerVaHistory tCustomerVaHistory, @PathVariable Long id) {
        if (!Objects.equals(tCustomerVaHistory.getId(), id)) {
            tCustomerVaHistory.setId(id);
        }

        Response<Object> tCustomerVaHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerVaHistoryService.updateTCustomerVaHistory(tCustomerVaHistory));
        return ResponseEntity.status(tCustomerVaHistoryResponse.getStatus()).body(tCustomerVaHistoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerVaHistory(@PathVariable Long id) {
        this.tCustomerVaHistoryService.deleteTCustomerVaHistory(id);
        Response<Object> tCustomerVaHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerVaHistoryResponse.getStatus()).body(tCustomerVaHistoryResponse);
    }
}
