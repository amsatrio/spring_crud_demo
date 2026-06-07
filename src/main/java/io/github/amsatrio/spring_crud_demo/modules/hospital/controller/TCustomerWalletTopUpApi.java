package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWalletTopUp;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerWalletTopUpService;
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
@RequestMapping("/v1/t-customer-wallet-top-up")
public class TCustomerWalletTopUpApi {

    private final TCustomerWalletTopUpService tCustomerWalletTopUpService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerWalletTopUpApi(
            TCustomerWalletTopUpService tCustomerWalletTopUpService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerWalletTopUpService = tCustomerWalletTopUpService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerWalletTopUp(
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
            log.error("tCustomerWalletTopUp > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerWalletTopUp>> tCustomerWalletTopUpResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerWalletTopUpService.getPageTCustomerWalletTopUp(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerWalletTopUpResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerWalletTopUp(@PathVariable Long id) {
        Response<TCustomerWalletTopUp> tCustomerWalletTopUpResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletTopUpService.getTCustomerWalletTopUp(id));
        return ResponseEntity.status(tCustomerWalletTopUpResponse.getStatus()).body(tCustomerWalletTopUpResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerWalletTopUpHeader() {
        Response<Object> tCustomerWalletTopUpResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerWalletTopUp()));
        return ResponseEntity.status(tCustomerWalletTopUpResponse.getStatus()).body(tCustomerWalletTopUpResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerWalletTopUp(@Valid @RequestBody TCustomerWalletTopUp tCustomerWalletTopUp) {
        Response<TCustomerWalletTopUp> tCustomerWalletTopUpResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletTopUpService.createTCustomerWalletTopUp(tCustomerWalletTopUp));
        return ResponseEntity.status(tCustomerWalletTopUpResponse.getStatus()).body(tCustomerWalletTopUpResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerWalletTopUp(@Valid @RequestBody TCustomerWalletTopUp tCustomerWalletTopUp, @PathVariable Long id) {
        if (!Objects.equals(tCustomerWalletTopUp.getId(), id)) {
            tCustomerWalletTopUp.setId(id);
        }

        Response<Object> tCustomerWalletTopUpResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletTopUpService.updateTCustomerWalletTopUp(tCustomerWalletTopUp));
        return ResponseEntity.status(tCustomerWalletTopUpResponse.getStatus()).body(tCustomerWalletTopUpResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerWalletTopUp(@PathVariable Long id) {
        this.tCustomerWalletTopUpService.deleteTCustomerWalletTopUp(id);
        Response<Object> tCustomerWalletTopUpResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerWalletTopUpResponse.getStatus()).body(tCustomerWalletTopUpResponse);
    }
}
