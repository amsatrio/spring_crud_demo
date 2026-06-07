package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerCustomNominal;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerCustomNominalService;
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
@RequestMapping("/v1/t-customer-custom-nominal")
public class TCustomerCustomNominalApi {

    private final TCustomerCustomNominalService tCustomerCustomNominalService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerCustomNominalApi(
            TCustomerCustomNominalService tCustomerCustomNominalService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerCustomNominalService = tCustomerCustomNominalService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerCustomNominal(
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
            log.error("tCustomerCustomNominal > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerCustomNominal>> tCustomerCustomNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerCustomNominalService.getPageTCustomerCustomNominal(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerCustomNominalResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerCustomNominal(@PathVariable Long id) {
        Response<TCustomerCustomNominal> tCustomerCustomNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerCustomNominalService.getTCustomerCustomNominal(id));
        return ResponseEntity.status(tCustomerCustomNominalResponse.getStatus()).body(tCustomerCustomNominalResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerCustomNominalHeader() {
        Response<Object> tCustomerCustomNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerCustomNominal()));
        return ResponseEntity.status(tCustomerCustomNominalResponse.getStatus()).body(tCustomerCustomNominalResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerCustomNominal(@Valid @RequestBody TCustomerCustomNominal tCustomerCustomNominal) {
        Response<TCustomerCustomNominal> tCustomerCustomNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerCustomNominalService.createTCustomerCustomNominal(tCustomerCustomNominal));
        return ResponseEntity.status(tCustomerCustomNominalResponse.getStatus()).body(tCustomerCustomNominalResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerCustomNominal(@Valid @RequestBody TCustomerCustomNominal tCustomerCustomNominal, @PathVariable Long id) {
        if (!Objects.equals(tCustomerCustomNominal.getId(), id)) {
            tCustomerCustomNominal.setId(id);
        }

        Response<Object> tCustomerCustomNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerCustomNominalService.updateTCustomerCustomNominal(tCustomerCustomNominal));
        return ResponseEntity.status(tCustomerCustomNominalResponse.getStatus()).body(tCustomerCustomNominalResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerCustomNominal(@PathVariable Long id) {
        this.tCustomerCustomNominalService.deleteTCustomerCustomNominal(id);
        Response<Object> tCustomerCustomNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerCustomNominalResponse.getStatus()).body(tCustomerCustomNominalResponse);
    }
}
