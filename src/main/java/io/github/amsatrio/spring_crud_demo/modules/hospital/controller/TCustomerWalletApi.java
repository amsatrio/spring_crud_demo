package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWallet;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerWalletService;
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
@RequestMapping("/v1/t-customer-wallet")
public class TCustomerWalletApi {

    private final TCustomerWalletService tCustomerWalletService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerWalletApi(
            TCustomerWalletService tCustomerWalletService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerWalletService = tCustomerWalletService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerWallet(
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
            log.error("tCustomerWallet > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerWallet>> tCustomerWalletResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerWalletService.getPageTCustomerWallet(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerWalletResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerWallet(@PathVariable Long id) {
        Response<TCustomerWallet> tCustomerWalletResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletService.getTCustomerWallet(id));
        return ResponseEntity.status(tCustomerWalletResponse.getStatus()).body(tCustomerWalletResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerWalletHeader() {
        Response<Object> tCustomerWalletResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerWallet()));
        return ResponseEntity.status(tCustomerWalletResponse.getStatus()).body(tCustomerWalletResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerWallet(@Valid @RequestBody TCustomerWallet tCustomerWallet) {
        Response<TCustomerWallet> tCustomerWalletResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletService.createTCustomerWallet(tCustomerWallet));
        return ResponseEntity.status(tCustomerWalletResponse.getStatus()).body(tCustomerWalletResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerWallet(@Valid @RequestBody TCustomerWallet tCustomerWallet, @PathVariable Long id) {
        if (!Objects.equals(tCustomerWallet.getId(), id)) {
            tCustomerWallet.setId(id);
        }

        Response<Object> tCustomerWalletResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerWalletService.updateTCustomerWallet(tCustomerWallet));
        return ResponseEntity.status(tCustomerWalletResponse.getStatus()).body(tCustomerWalletResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerWallet(@PathVariable Long id) {
        this.tCustomerWalletService.deleteTCustomerWallet(id);
        Response<Object> tCustomerWalletResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerWalletResponse.getStatus()).body(tCustomerWalletResponse);
    }
}
