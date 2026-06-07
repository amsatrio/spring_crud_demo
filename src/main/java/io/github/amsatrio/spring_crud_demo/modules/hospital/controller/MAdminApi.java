package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MAdmin;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MAdminService;
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
@RequestMapping("/v1/m-admin")
public class MAdminApi {

    private final MAdminService mAdminService;
    private final HttpServletRequest httpServletRequest;

    public MAdminApi(
            MAdminService mAdminService,
            HttpServletRequest httpServletRequest) {
        this.mAdminService = mAdminService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMAdmin(
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
            log.error("mAdmin > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MAdmin>> mAdminResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mAdminService.getPageMAdmin(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mAdminResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMAdmin(@PathVariable Long id) {
        Response<MAdmin> mAdminResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mAdminService.getMAdmin(id));
        return ResponseEntity.status(mAdminResponse.getStatus()).body(mAdminResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMAdminHeader() {
        Response<Object> mAdminResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MAdmin()));
        return ResponseEntity.status(mAdminResponse.getStatus()).body(mAdminResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMAdmin(@Valid @RequestBody MAdmin mAdmin) {
        Response<MAdmin> mAdminResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mAdminService.createMAdmin(mAdmin));
        return ResponseEntity.status(mAdminResponse.getStatus()).body(mAdminResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMAdmin(@Valid @RequestBody MAdmin mAdmin, @PathVariable Long id) {
        if (!Objects.equals(mAdmin.getId(), id)) {
            mAdmin.setId(id);
        }

        Response<Object> mAdminResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mAdminService.updateMAdmin(mAdmin));
        return ResponseEntity.status(mAdminResponse.getStatus()).body(mAdminResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMAdmin(@PathVariable Long id) {
        this.mAdminService.deleteMAdmin(id);
        Response<Object> mAdminResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mAdminResponse.getStatus()).body(mAdminResponse);
    }
}
