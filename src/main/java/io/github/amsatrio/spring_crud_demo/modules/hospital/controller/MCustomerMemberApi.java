package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomerMember;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MCustomerMemberService;
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
@RequestMapping("/v1/m-customer-member")
public class MCustomerMemberApi {

    private final MCustomerMemberService mCustomerMemberService;
    private final HttpServletRequest httpServletRequest;

    public MCustomerMemberApi(
            MCustomerMemberService mCustomerMemberService,
            HttpServletRequest httpServletRequest) {
        this.mCustomerMemberService = mCustomerMemberService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMCustomerMember(
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
            log.error("mCustomerMember > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MCustomerMember>> mCustomerMemberResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mCustomerMemberService.getPageMCustomerMember(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mCustomerMemberResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMCustomerMember(@PathVariable Long id) {
        Response<MCustomerMember> mCustomerMemberResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerMemberService.getMCustomerMember(id));
        return ResponseEntity.status(mCustomerMemberResponse.getStatus()).body(mCustomerMemberResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMCustomerMemberHeader() {
        Response<Object> mCustomerMemberResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MCustomerMember()));
        return ResponseEntity.status(mCustomerMemberResponse.getStatus()).body(mCustomerMemberResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMCustomerMember(@Valid @RequestBody MCustomerMember mCustomerMember) {
        Response<MCustomerMember> mCustomerMemberResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerMemberService.createMCustomerMember(mCustomerMember));
        return ResponseEntity.status(mCustomerMemberResponse.getStatus()).body(mCustomerMemberResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMCustomerMember(@Valid @RequestBody MCustomerMember mCustomerMember, @PathVariable Long id) {
        if (!Objects.equals(mCustomerMember.getId(), id)) {
            mCustomerMember.setId(id);
        }

        Response<Object> mCustomerMemberResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCustomerMemberService.updateMCustomerMember(mCustomerMember));
        return ResponseEntity.status(mCustomerMemberResponse.getStatus()).body(mCustomerMemberResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMCustomerMember(@PathVariable Long id) {
        this.mCustomerMemberService.deleteMCustomerMember(id);
        Response<Object> mCustomerMemberResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mCustomerMemberResponse.getStatus()).body(mCustomerMemberResponse);
    }
}
