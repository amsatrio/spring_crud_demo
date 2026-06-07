package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TResetPassword;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TResetPasswordService;
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
@RequestMapping("/v1/t-reset-password")
public class TResetPasswordApi {

    private final TResetPasswordService tResetPasswordService;
    private final HttpServletRequest httpServletRequest;

    public TResetPasswordApi(
            TResetPasswordService tResetPasswordService,
            HttpServletRequest httpServletRequest) {
        this.tResetPasswordService = tResetPasswordService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTResetPassword(
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
            log.error("tResetPassword > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TResetPassword>> tResetPasswordResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tResetPasswordService.getPageTResetPassword(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tResetPasswordResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTResetPassword(@PathVariable Long id) {
        Response<TResetPassword> tResetPasswordResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tResetPasswordService.getTResetPassword(id));
        return ResponseEntity.status(tResetPasswordResponse.getStatus()).body(tResetPasswordResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTResetPasswordHeader() {
        Response<Object> tResetPasswordResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TResetPassword()));
        return ResponseEntity.status(tResetPasswordResponse.getStatus()).body(tResetPasswordResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTResetPassword(@Valid @RequestBody TResetPassword tResetPassword) {
        Response<TResetPassword> tResetPasswordResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tResetPasswordService.createTResetPassword(tResetPassword));
        return ResponseEntity.status(tResetPasswordResponse.getStatus()).body(tResetPasswordResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTResetPassword(@Valid @RequestBody TResetPassword tResetPassword, @PathVariable Long id) {
        if (!Objects.equals(tResetPassword.getId(), id)) {
            tResetPassword.setId(id);
        }

        Response<Object> tResetPasswordResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tResetPasswordService.updateTResetPassword(tResetPassword));
        return ResponseEntity.status(tResetPasswordResponse.getStatus()).body(tResetPasswordResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTResetPassword(@PathVariable Long id) {
        this.tResetPasswordService.deleteTResetPassword(id);
        Response<Object> tResetPasswordResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tResetPasswordResponse.getStatus()).body(tResetPasswordResponse);
    }
}
