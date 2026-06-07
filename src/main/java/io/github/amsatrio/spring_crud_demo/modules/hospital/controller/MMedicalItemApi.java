package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItem;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMedicalItemService;
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
@RequestMapping("/v1/m-medical-item")
public class MMedicalItemApi {

    private final MMedicalItemService mMedicalItemService;
    private final HttpServletRequest httpServletRequest;

    public MMedicalItemApi(
            MMedicalItemService mMedicalItemService,
            HttpServletRequest httpServletRequest) {
        this.mMedicalItemService = mMedicalItemService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMMedicalItem(
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
            log.error("mMedicalItem > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MMedicalItem>> mMedicalItemResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mMedicalItemService.getPageMMedicalItem(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mMedicalItemResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMMedicalItem(@PathVariable Long id) {
        Response<MMedicalItem> mMedicalItemResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemService.getMMedicalItem(id));
        return ResponseEntity.status(mMedicalItemResponse.getStatus()).body(mMedicalItemResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMMedicalItemHeader() {
        Response<Object> mMedicalItemResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MMedicalItem()));
        return ResponseEntity.status(mMedicalItemResponse.getStatus()).body(mMedicalItemResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMMedicalItem(@Valid @RequestBody MMedicalItem mMedicalItem) {
        Response<MMedicalItem> mMedicalItemResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemService.createMMedicalItem(mMedicalItem));
        return ResponseEntity.status(mMedicalItemResponse.getStatus()).body(mMedicalItemResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMMedicalItem(@Valid @RequestBody MMedicalItem mMedicalItem, @PathVariable Long id) {
        if (!Objects.equals(mMedicalItem.getId(), id)) {
            mMedicalItem.setId(id);
        }

        Response<Object> mMedicalItemResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemService.updateMMedicalItem(mMedicalItem));
        return ResponseEntity.status(mMedicalItemResponse.getStatus()).body(mMedicalItemResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMMedicalItem(@PathVariable Long id) {
        this.mMedicalItemService.deleteMMedicalItem(id);
        Response<Object> mMedicalItemResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mMedicalItemResponse.getStatus()).body(mMedicalItemResponse);
    }
}
