package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCourier;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MCourierService;
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
@RequestMapping("/v1/m-courier")
public class MCourierApi {

    private final MCourierService mCourierService;
    private final HttpServletRequest httpServletRequest;

    public MCourierApi(
            MCourierService mCourierService,
            HttpServletRequest httpServletRequest) {
        this.mCourierService = mCourierService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMCourier(
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
            log.error("mCourier > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MCourier>> mCourierResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mCourierService.getPageMCourier(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mCourierResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMCourier(@PathVariable Long id) {
        Response<MCourier> mCourierResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCourierService.getMCourier(id));
        return ResponseEntity.status(mCourierResponse.getStatus()).body(mCourierResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMCourierHeader() {
        Response<Object> mCourierResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MCourier()));
        return ResponseEntity.status(mCourierResponse.getStatus()).body(mCourierResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMCourier(@Valid @RequestBody MCourier mCourier) {
        Response<MCourier> mCourierResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCourierService.createMCourier(mCourier));
        return ResponseEntity.status(mCourierResponse.getStatus()).body(mCourierResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMCourier(@Valid @RequestBody MCourier mCourier, @PathVariable Long id) {
        if (!Objects.equals(mCourier.getId(), id)) {
            mCourier.setId(id);
        }

        Response<Object> mCourierResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mCourierService.updateMCourier(mCourier));
        return ResponseEntity.status(mCourierResponse.getStatus()).body(mCourierResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMCourier(@PathVariable Long id) {
        this.mCourierService.deleteMCourier(id);
        Response<Object> mCourierResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mCourierResponse.getStatus()).body(mCourierResponse);
    }
}
