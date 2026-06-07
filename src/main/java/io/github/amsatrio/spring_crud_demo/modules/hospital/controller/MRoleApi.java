package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MRole;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MRoleService;
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
@RequestMapping("/v1/m-role")
public class MRoleApi {

    private final MRoleService mRoleService;
    private final HttpServletRequest httpServletRequest;

    public MRoleApi(
            MRoleService mRoleService,
            HttpServletRequest httpServletRequest) {
        this.mRoleService = mRoleService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMRole(
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
            log.error("mRole > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MRole>> mRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mRoleService.getPageMRole(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mRoleResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMRole(@PathVariable Long id) {
        Response<MRole> mRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mRoleService.getMRole(id));
        return ResponseEntity.status(mRoleResponse.getStatus()).body(mRoleResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMRoleHeader() {
        Response<Object> mRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MRole()));
        return ResponseEntity.status(mRoleResponse.getStatus()).body(mRoleResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMRole(@Valid @RequestBody MRole mRole) {
        Response<MRole> mRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mRoleService.createMRole(mRole));
        return ResponseEntity.status(mRoleResponse.getStatus()).body(mRoleResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMRole(@Valid @RequestBody MRole mRole, @PathVariable Long id) {
        if (!Objects.equals(mRole.getId(), id)) {
            mRole.setId(id);
        }

        Response<Object> mRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mRoleService.updateMRole(mRole));
        return ResponseEntity.status(mRoleResponse.getStatus()).body(mRoleResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMRole(@PathVariable Long id) {
        this.mRoleService.deleteMRole(id);
        Response<Object> mRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mRoleResponse.getStatus()).body(mRoleResponse);
    }
}
