package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerVa;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerVaService;
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
@RequestMapping("/v1/t-customer-va")
public class TCustomerVaApi {

    private final TCustomerVaService tCustomerVaService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerVaApi(
            TCustomerVaService tCustomerVaService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerVaService = tCustomerVaService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerVa(
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
            log.error("tCustomerVa > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerVa>> tCustomerVaResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerVaService.getPageTCustomerVa(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerVaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerVa(@PathVariable Long id) {
        Response<TCustomerVa> tCustomerVaResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerVaService.getTCustomerVa(id));
        return ResponseEntity.status(tCustomerVaResponse.getStatus()).body(tCustomerVaResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerVaHeader() {
        Response<Object> tCustomerVaResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerVa()));
        return ResponseEntity.status(tCustomerVaResponse.getStatus()).body(tCustomerVaResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerVa(@Valid @RequestBody TCustomerVa tCustomerVa) {
        Response<TCustomerVa> tCustomerVaResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerVaService.createTCustomerVa(tCustomerVa));
        return ResponseEntity.status(tCustomerVaResponse.getStatus()).body(tCustomerVaResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerVa(@Valid @RequestBody TCustomerVa tCustomerVa, @PathVariable Long id) {
        if (!Objects.equals(tCustomerVa.getId(), id)) {
            tCustomerVa.setId(id);
        }

        Response<Object> tCustomerVaResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerVaService.updateTCustomerVa(tCustomerVa));
        return ResponseEntity.status(tCustomerVaResponse.getStatus()).body(tCustomerVaResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerVa(@PathVariable Long id) {
        this.tCustomerVaService.deleteTCustomerVa(id);
        Response<Object> tCustomerVaResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerVaResponse.getStatus()).body(tCustomerVaResponse);
    }
}
