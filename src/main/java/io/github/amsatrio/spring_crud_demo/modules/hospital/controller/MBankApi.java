package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBank;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MBankService;
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
@RequestMapping("/v1/m-bank")
public class MBankApi {

    private final MBankService mBankService;
    private final HttpServletRequest httpServletRequest;

    public MBankApi(
            MBankService mBankService,
            HttpServletRequest httpServletRequest) {
        this.mBankService = mBankService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMBank(
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
            log.error("mBank > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MBank>> mBankResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mBankService.getPageMBank(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mBankResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMBank(@PathVariable Long id) {
        Response<MBank> mBankResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBankService.getMBank(id));
        return ResponseEntity.status(mBankResponse.getStatus()).body(mBankResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMBankHeader() {
        Response<Object> mBankResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MBank()));
        return ResponseEntity.status(mBankResponse.getStatus()).body(mBankResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMBank(@Valid @RequestBody MBank mBank) {
        Response<MBank> mBankResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBankService.createMBank(mBank));
        return ResponseEntity.status(mBankResponse.getStatus()).body(mBankResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMBank(@Valid @RequestBody MBank mBank, @PathVariable Long id) {
        if (!Objects.equals(mBank.getId(), id)) {
            mBank.setId(id);
        }

        Response<Object> mBankResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBankService.updateMBank(mBank));
        return ResponseEntity.status(mBankResponse.getStatus()).body(mBankResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMBank(@PathVariable Long id) {
        this.mBankService.deleteMBank(id);
        Response<Object> mBankResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mBankResponse.getStatus()).body(mBankResponse);
    }
}
