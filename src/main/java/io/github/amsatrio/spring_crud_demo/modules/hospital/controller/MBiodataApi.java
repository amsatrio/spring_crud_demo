package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodata;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MBiodataService;
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
@RequestMapping("/v1/m-biodata")
public class MBiodataApi {

    private final MBiodataService mBiodataService;
    private final HttpServletRequest httpServletRequest;

    public MBiodataApi(
            MBiodataService mBiodataService,
            HttpServletRequest httpServletRequest) {
        this.mBiodataService = mBiodataService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMBiodata(
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
            log.error("mBiodata > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MBiodata>> mBiodataResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mBiodataService.getPageMBiodata(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mBiodataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMBiodata(@PathVariable Long id) {
        Response<MBiodata> mBiodataResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataService.getMBiodata(id));
        return ResponseEntity.status(mBiodataResponse.getStatus()).body(mBiodataResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMBiodataHeader() {
        Response<Object> mBiodataResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MBiodata()));
        return ResponseEntity.status(mBiodataResponse.getStatus()).body(mBiodataResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMBiodata(@Valid @RequestBody MBiodata mBiodata) {
        Response<MBiodata> mBiodataResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataService.createMBiodata(mBiodata));
        return ResponseEntity.status(mBiodataResponse.getStatus()).body(mBiodataResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMBiodata(@Valid @RequestBody MBiodata mBiodata, @PathVariable Long id) {
        if (!Objects.equals(mBiodata.getId(), id)) {
            mBiodata.setId(id);
        }

        Response<Object> mBiodataResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataService.updateMBiodata(mBiodata));
        return ResponseEntity.status(mBiodataResponse.getStatus()).body(mBiodataResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMBiodata(@PathVariable Long id) {
        this.mBiodataService.deleteMBiodata(id);
        Response<Object> mBiodataResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mBiodataResponse.getStatus()).body(mBiodataResponse);
    }
}
