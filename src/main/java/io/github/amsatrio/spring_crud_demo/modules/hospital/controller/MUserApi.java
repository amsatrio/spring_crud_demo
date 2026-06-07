package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MUser;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MUserService;
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
@RequestMapping("/v1/m-user")
public class MUserApi {

    private final MUserService mUserService;
    private final HttpServletRequest httpServletRequest;

    public MUserApi(
            MUserService mUserService,
            HttpServletRequest httpServletRequest) {
        this.mUserService = mUserService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMUser(
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
            log.error("mUser > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MUser>> mUserResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mUserService.getPageMUser(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mUserResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMUser(@PathVariable Long id) {
        Response<MUser> mUserResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mUserService.getMUser(id));
        return ResponseEntity.status(mUserResponse.getStatus()).body(mUserResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMUserHeader() {
        Response<Object> mUserResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MUser()));
        return ResponseEntity.status(mUserResponse.getStatus()).body(mUserResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMUser(@Valid @RequestBody MUser mUser) {
        Response<MUser> mUserResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mUserService.createMUser(mUser));
        return ResponseEntity.status(mUserResponse.getStatus()).body(mUserResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMUser(@Valid @RequestBody MUser mUser, @PathVariable Long id) {
        if (!Objects.equals(mUser.getId(), id)) {
            mUser.setId(id);
        }

        Response<Object> mUserResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mUserService.updateMUser(mUser));
        return ResponseEntity.status(mUserResponse.getStatus()).body(mUserResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMUser(@PathVariable Long id) {
        this.mUserService.deleteMUser(id);
        Response<Object> mUserResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mUserResponse.getStatus()).body(mUserResponse);
    }
}
