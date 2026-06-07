package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TToken;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TTokenService;
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
@RequestMapping("/v1/t-token")
public class TTokenApi {

    private final TTokenService tTokenService;
    private final HttpServletRequest httpServletRequest;

    public TTokenApi(
            TTokenService tTokenService,
            HttpServletRequest httpServletRequest) {
        this.tTokenService = tTokenService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTToken(
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
            log.error("tToken > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TToken>> tTokenResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tTokenService.getPageTToken(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tTokenResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTToken(@PathVariable Long id) {
        Response<TToken> tTokenResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tTokenService.getTToken(id));
        return ResponseEntity.status(tTokenResponse.getStatus()).body(tTokenResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTTokenHeader() {
        Response<Object> tTokenResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TToken()));
        return ResponseEntity.status(tTokenResponse.getStatus()).body(tTokenResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTToken(@Valid @RequestBody TToken tToken) {
        Response<TToken> tTokenResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tTokenService.createTToken(tToken));
        return ResponseEntity.status(tTokenResponse.getStatus()).body(tTokenResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTToken(@Valid @RequestBody TToken tToken, @PathVariable Long id) {
        if (!Objects.equals(tToken.getId(), id)) {
            tToken.setId(id);
        }

        Response<Object> tTokenResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tTokenService.updateTToken(tToken));
        return ResponseEntity.status(tTokenResponse.getStatus()).body(tTokenResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTToken(@PathVariable Long id) {
        this.tTokenService.deleteTToken(id);
        Response<Object> tTokenResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tTokenResponse.getStatus()).body(tTokenResponse);
    }
}
