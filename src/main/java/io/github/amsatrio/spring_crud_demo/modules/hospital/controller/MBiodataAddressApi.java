package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodataAddress;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MBiodataAddressService;
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
@RequestMapping("/v1/m-biodata-address")
public class MBiodataAddressApi {

    private final MBiodataAddressService mBiodataAddressService;
    private final HttpServletRequest httpServletRequest;

    public MBiodataAddressApi(
            MBiodataAddressService mBiodataAddressService,
            HttpServletRequest httpServletRequest) {
        this.mBiodataAddressService = mBiodataAddressService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMBiodataAddress(
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
            log.error("mBiodataAddress > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MBiodataAddress>> mBiodataAddressResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mBiodataAddressService.getPageMBiodataAddress(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mBiodataAddressResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMBiodataAddress(@PathVariable Long id) {
        Response<MBiodataAddress> mBiodataAddressResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataAddressService.getMBiodataAddress(id));
        return ResponseEntity.status(mBiodataAddressResponse.getStatus()).body(mBiodataAddressResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMBiodataAddressHeader() {
        Response<Object> mBiodataAddressResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MBiodataAddress()));
        return ResponseEntity.status(mBiodataAddressResponse.getStatus()).body(mBiodataAddressResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMBiodataAddress(@Valid @RequestBody MBiodataAddress mBiodataAddress) {
        Response<MBiodataAddress> mBiodataAddressResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataAddressService.createMBiodataAddress(mBiodataAddress));
        return ResponseEntity.status(mBiodataAddressResponse.getStatus()).body(mBiodataAddressResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMBiodataAddress(@Valid @RequestBody MBiodataAddress mBiodataAddress, @PathVariable Long id) {
        if (!Objects.equals(mBiodataAddress.getId(), id)) {
            mBiodataAddress.setId(id);
        }

        Response<Object> mBiodataAddressResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataAddressService.updateMBiodataAddress(mBiodataAddress));
        return ResponseEntity.status(mBiodataAddressResponse.getStatus()).body(mBiodataAddressResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMBiodataAddress(@PathVariable Long id) {
        this.mBiodataAddressService.deleteMBiodataAddress(id);
        Response<Object> mBiodataAddressResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mBiodataAddressResponse.getStatus()).body(mBiodataAddressResponse);
    }
}
