package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomer;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MCustomerService;
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
@RequestMapping("/v1/m-customer")
public class MCustomerApi {

    private final MCustomerService mCustomerService;
    private final HttpServletRequest httpServletRequest;

    public MCustomerApi(
            MCustomerService mCustomerService,
            HttpServletRequest httpServletRequest) {
        this.mCustomerService = mCustomerService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMCustomer(
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
            log.error("mCustomer > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MCustomer>> mCustomerResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mCustomerService.getPageMCustomer(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mCustomerResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMCustomer(@PathVariable Long id) {
        Response<MCustomer> mCustomerResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerService.getMCustomer(id));
        return ResponseEntity.status(mCustomerResponse.getStatus()).body(mCustomerResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMCustomerHeader() {
        Response<Object> mCustomerResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MCustomer()));
        return ResponseEntity.status(mCustomerResponse.getStatus()).body(mCustomerResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMCustomer(@Valid @RequestBody MCustomer mCustomer) {
        Response<MCustomer> mCustomerResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerService.createMCustomer(mCustomer));
        return ResponseEntity.status(mCustomerResponse.getStatus()).body(mCustomerResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMCustomer(@Valid @RequestBody MCustomer mCustomer, @PathVariable Long id) {
        if (!Objects.equals(mCustomer.getId(), id)) {
            mCustomer.setId(id);
        }

        Response<Object> mCustomerResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerService.updateMCustomer(mCustomer));
        return ResponseEntity.status(mCustomerResponse.getStatus()).body(mCustomerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMCustomer(@PathVariable Long id) {
        this.mCustomerService.deleteMCustomer(id);
        Response<Object> mCustomerResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mCustomerResponse.getStatus()).body(mCustomerResponse);
    }
}
