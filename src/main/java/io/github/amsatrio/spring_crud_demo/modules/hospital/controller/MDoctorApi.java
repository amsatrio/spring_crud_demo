package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MDoctor;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MDoctorService;
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
@RequestMapping("/v1/m-doctor")
public class MDoctorApi {

    private final MDoctorService mDoctorService;
    private final HttpServletRequest httpServletRequest;

    public MDoctorApi(
            MDoctorService mDoctorService,
            HttpServletRequest httpServletRequest) {
        this.mDoctorService = mDoctorService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMDoctor(
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
            log.error("mDoctor > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MDoctor>> mDoctorResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mDoctorService.getPageMDoctor(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mDoctorResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMDoctor(@PathVariable Long id) {
        Response<MDoctor> mDoctorResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mDoctorService.getMDoctor(id));
        return ResponseEntity.status(mDoctorResponse.getStatus()).body(mDoctorResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMDoctorHeader() {
        Response<Object> mDoctorResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MDoctor()));
        return ResponseEntity.status(mDoctorResponse.getStatus()).body(mDoctorResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMDoctor(@Valid @RequestBody MDoctor mDoctor) {
        Response<MDoctor> mDoctorResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mDoctorService.createMDoctor(mDoctor));
        return ResponseEntity.status(mDoctorResponse.getStatus()).body(mDoctorResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMDoctor(@Valid @RequestBody MDoctor mDoctor, @PathVariable Long id) {
        if (!Objects.equals(mDoctor.getId(), id)) {
            mDoctor.setId(id);
        }

        Response<Object> mDoctorResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mDoctorService.updateMDoctor(mDoctor));
        return ResponseEntity.status(mDoctorResponse.getStatus()).body(mDoctorResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMDoctor(@PathVariable Long id) {
        this.mDoctorService.deleteMDoctor(id);
        Response<Object> mDoctorResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mDoctorResponse.getStatus()).body(mDoctorResponse);
    }
}
