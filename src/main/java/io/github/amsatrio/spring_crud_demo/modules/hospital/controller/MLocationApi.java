package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MLocation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MLocationService;
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
@RequestMapping("/v1/m-location")
public class MLocationApi {

    private final MLocationService mLocationService;
    private final HttpServletRequest httpServletRequest;

    public MLocationApi(
            MLocationService mLocationService,
            HttpServletRequest httpServletRequest) {
        this.mLocationService = mLocationService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMLocation(
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
            log.error("mLocation > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MLocation>> mLocationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mLocationService.getPageMLocation(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mLocationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMLocation(@PathVariable Long id) {
        Response<MLocation> mLocationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mLocationService.getMLocation(id));
        return ResponseEntity.status(mLocationResponse.getStatus()).body(mLocationResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMLocationHeader() {
        Response<Object> mLocationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MLocation()));
        return ResponseEntity.status(mLocationResponse.getStatus()).body(mLocationResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMLocation(@Valid @RequestBody MLocation mLocation) {
        Response<MLocation> mLocationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mLocationService.createMLocation(mLocation));
        return ResponseEntity.status(mLocationResponse.getStatus()).body(mLocationResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMLocation(@Valid @RequestBody MLocation mLocation, @PathVariable Long id) {
        if (!Objects.equals(mLocation.getId(), id)) {
            mLocation.setId(id);
        }

        Response<Object> mLocationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mLocationService.updateMLocation(mLocation));
        return ResponseEntity.status(mLocationResponse.getStatus()).body(mLocationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMLocation(@PathVariable Long id) {
        this.mLocationService.deleteMLocation(id);
        Response<Object> mLocationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mLocationResponse.getStatus()).body(mLocationResponse);
    }
}
