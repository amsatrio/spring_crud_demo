package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWalletWithdraw;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerWalletWithdrawService;
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
@RequestMapping("/v1/t-customer-wallet-withdraw")
public class TCustomerWalletWithdrawApi {

    private final TCustomerWalletWithdrawService tCustomerWalletWithdrawService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerWalletWithdrawApi(
            TCustomerWalletWithdrawService tCustomerWalletWithdrawService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerWalletWithdrawService = tCustomerWalletWithdrawService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerWalletWithdraw(
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
            log.error("tCustomerWalletWithdraw > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerWalletWithdraw>> tCustomerWalletWithdrawResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerWalletWithdrawService.getPageTCustomerWalletWithdraw(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerWalletWithdrawResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerWalletWithdraw(@PathVariable Long id) {
        Response<TCustomerWalletWithdraw> tCustomerWalletWithdrawResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletWithdrawService.getTCustomerWalletWithdraw(id));
        return ResponseEntity.status(tCustomerWalletWithdrawResponse.getStatus()).body(tCustomerWalletWithdrawResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerWalletWithdrawHeader() {
        Response<Object> tCustomerWalletWithdrawResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerWalletWithdraw()));
        return ResponseEntity.status(tCustomerWalletWithdrawResponse.getStatus()).body(tCustomerWalletWithdrawResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerWalletWithdraw(@Valid @RequestBody TCustomerWalletWithdraw tCustomerWalletWithdraw) {
        Response<TCustomerWalletWithdraw> tCustomerWalletWithdrawResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletWithdrawService.createTCustomerWalletWithdraw(tCustomerWalletWithdraw));
        return ResponseEntity.status(tCustomerWalletWithdrawResponse.getStatus()).body(tCustomerWalletWithdrawResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerWalletWithdraw(@Valid @RequestBody TCustomerWalletWithdraw tCustomerWalletWithdraw, @PathVariable Long id) {
        if (!Objects.equals(tCustomerWalletWithdraw.getId(), id)) {
            tCustomerWalletWithdraw.setId(id);
        }

        Response<Object> tCustomerWalletWithdrawResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletWithdrawService.updateTCustomerWalletWithdraw(tCustomerWalletWithdraw));
        return ResponseEntity.status(tCustomerWalletWithdrawResponse.getStatus()).body(tCustomerWalletWithdrawResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerWalletWithdraw(@PathVariable Long id) {
        this.tCustomerWalletWithdrawService.deleteTCustomerWalletWithdraw(id);
        Response<Object> tCustomerWalletWithdrawResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerWalletWithdrawResponse.getStatus()).body(tCustomerWalletWithdrawResponse);
    }
}
