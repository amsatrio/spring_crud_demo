package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCourierDiscount;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCourierDiscountService;
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
@RequestMapping("/v1/t-courier-discount")
public class TCourierDiscountApi {

    private final TCourierDiscountService tCourierDiscountService;
    private final HttpServletRequest httpServletRequest;

    public TCourierDiscountApi(
            TCourierDiscountService tCourierDiscountService,
            HttpServletRequest httpServletRequest) {
        this.tCourierDiscountService = tCourierDiscountService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCourierDiscount(
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
            log.error("tCourierDiscount > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCourierDiscount>> tCourierDiscountResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCourierDiscountService.getPageTCourierDiscount(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCourierDiscountResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCourierDiscount(@PathVariable Long id) {
        Response<TCourierDiscount> tCourierDiscountResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCourierDiscountService.getTCourierDiscount(id));
        return ResponseEntity.status(tCourierDiscountResponse.getStatus()).body(tCourierDiscountResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCourierDiscountHeader() {
        Response<Object> tCourierDiscountResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCourierDiscount()));
        return ResponseEntity.status(tCourierDiscountResponse.getStatus()).body(tCourierDiscountResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCourierDiscount(@Valid @RequestBody TCourierDiscount tCourierDiscount) {
        Response<TCourierDiscount> tCourierDiscountResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCourierDiscountService.createTCourierDiscount(tCourierDiscount));
        return ResponseEntity.status(tCourierDiscountResponse.getStatus()).body(tCourierDiscountResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCourierDiscount(@Valid @RequestBody TCourierDiscount tCourierDiscount, @PathVariable Long id) {
        if (!Objects.equals(tCourierDiscount.getId(), id)) {
            tCourierDiscount.setId(id);
        }

        Response<Object> tCourierDiscountResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCourierDiscountService.updateTCourierDiscount(tCourierDiscount));
        return ResponseEntity.status(tCourierDiscountResponse.getStatus()).body(tCourierDiscountResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCourierDiscount(@PathVariable Long id) {
        this.tCourierDiscountService.deleteTCourierDiscount(id);
        Response<Object> tCourierDiscountResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCourierDiscountResponse.getStatus()).body(tCourierDiscountResponse);
    }
}
