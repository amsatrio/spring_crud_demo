package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMenuRole;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMenuRoleService;
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
@RequestMapping("/v1/m-menu-role")
public class MMenuRoleApi {

    private final MMenuRoleService mMenuRoleService;
    private final HttpServletRequest httpServletRequest;

    public MMenuRoleApi(
            MMenuRoleService mMenuRoleService,
            HttpServletRequest httpServletRequest) {
        this.mMenuRoleService = mMenuRoleService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMMenuRole(
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
            log.error("mMenuRole > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MMenuRole>> mMenuRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mMenuRoleService.getPageMMenuRole(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mMenuRoleResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMMenuRole(@PathVariable Long id) {
        Response<MMenuRole> mMenuRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMenuRoleService.getMMenuRole(id));
        return ResponseEntity.status(mMenuRoleResponse.getStatus()).body(mMenuRoleResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMMenuRoleHeader() {
        Response<Object> mMenuRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MMenuRole()));
        return ResponseEntity.status(mMenuRoleResponse.getStatus()).body(mMenuRoleResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMMenuRole(@Valid @RequestBody MMenuRole mMenuRole) {
        Response<MMenuRole> mMenuRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMenuRoleService.createMMenuRole(mMenuRole));
        return ResponseEntity.status(mMenuRoleResponse.getStatus()).body(mMenuRoleResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMMenuRole(@Valid @RequestBody MMenuRole mMenuRole, @PathVariable Long id) {
        if (!Objects.equals(mMenuRole.getId(), id)) {
            mMenuRole.setId(id);
        }

        Response<Object> mMenuRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMenuRoleService.updateMMenuRole(mMenuRole));
        return ResponseEntity.status(mMenuRoleResponse.getStatus()).body(mMenuRoleResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMMenuRole(@PathVariable Long id) {
        this.mMenuRoleService.deleteMMenuRole(id);
        Response<Object> mMenuRoleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mMenuRoleResponse.getStatus()).body(mMenuRoleResponse);
    }
}
