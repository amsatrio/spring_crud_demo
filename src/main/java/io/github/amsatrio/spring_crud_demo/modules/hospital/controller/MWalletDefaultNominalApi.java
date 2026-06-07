package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MWalletDefaultNominal;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MWalletDefaultNominalService;
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
@RequestMapping("/v1/m-wallet-default-nominal")
public class MWalletDefaultNominalApi {

    private final MWalletDefaultNominalService mWalletDefaultNominalService;
    private final HttpServletRequest httpServletRequest;

    public MWalletDefaultNominalApi(
            MWalletDefaultNominalService mWalletDefaultNominalService,
            HttpServletRequest httpServletRequest) {
        this.mWalletDefaultNominalService = mWalletDefaultNominalService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMWalletDefaultNominal(
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
            log.error("mWalletDefaultNominal > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MWalletDefaultNominal>> mWalletDefaultNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mWalletDefaultNominalService.getPageMWalletDefaultNominal(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mWalletDefaultNominalResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMWalletDefaultNominal(@PathVariable Long id) {
        Response<MWalletDefaultNominal> mWalletDefaultNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mWalletDefaultNominalService.getMWalletDefaultNominal(id));
        return ResponseEntity.status(mWalletDefaultNominalResponse.getStatus()).body(mWalletDefaultNominalResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMWalletDefaultNominalHeader() {
        Response<Object> mWalletDefaultNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MWalletDefaultNominal()));
        return ResponseEntity.status(mWalletDefaultNominalResponse.getStatus()).body(mWalletDefaultNominalResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMWalletDefaultNominal(@Valid @RequestBody MWalletDefaultNominal mWalletDefaultNominal) {
        Response<MWalletDefaultNominal> mWalletDefaultNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mWalletDefaultNominalService.createMWalletDefaultNominal(mWalletDefaultNominal));
        return ResponseEntity.status(mWalletDefaultNominalResponse.getStatus()).body(mWalletDefaultNominalResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMWalletDefaultNominal(@Valid @RequestBody MWalletDefaultNominal mWalletDefaultNominal, @PathVariable Long id) {
        if (!Objects.equals(mWalletDefaultNominal.getId(), id)) {
            mWalletDefaultNominal.setId(id);
        }

        Response<Object> mWalletDefaultNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mWalletDefaultNominalService.updateMWalletDefaultNominal(mWalletDefaultNominal));
        return ResponseEntity.status(mWalletDefaultNominalResponse.getStatus()).body(mWalletDefaultNominalResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMWalletDefaultNominal(@PathVariable Long id) {
        this.mWalletDefaultNominalService.deleteMWalletDefaultNominal(id);
        Response<Object> mWalletDefaultNominalResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mWalletDefaultNominalResponse.getStatus()).body(mWalletDefaultNominalResponse);
    }
}
