package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItemSegmentation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMedicalItemSegmentationService;
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
@RequestMapping("/v1/m-medical-item-segmentation")
public class MMedicalItemSegmentationApi {

    private final MMedicalItemSegmentationService mMedicalItemSegmentationService;
    private final HttpServletRequest httpServletRequest;

    public MMedicalItemSegmentationApi(
            MMedicalItemSegmentationService mMedicalItemSegmentationService,
            HttpServletRequest httpServletRequest) {
        this.mMedicalItemSegmentationService = mMedicalItemSegmentationService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMMedicalItemSegmentation(
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
            log.error("mMedicalItemSegmentation > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MMedicalItemSegmentation>> mMedicalItemSegmentationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mMedicalItemSegmentationService.getPageMMedicalItemSegmentation(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mMedicalItemSegmentationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMMedicalItemSegmentation(@PathVariable Long id) {
        Response<MMedicalItemSegmentation> mMedicalItemSegmentationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemSegmentationService.getMMedicalItemSegmentation(id));
        return ResponseEntity.status(mMedicalItemSegmentationResponse.getStatus()).body(mMedicalItemSegmentationResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMMedicalItemSegmentationHeader() {
        Response<Object> mMedicalItemSegmentationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MMedicalItemSegmentation()));
        return ResponseEntity.status(mMedicalItemSegmentationResponse.getStatus()).body(mMedicalItemSegmentationResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMMedicalItemSegmentation(@Valid @RequestBody MMedicalItemSegmentation mMedicalItemSegmentation) {
        Response<MMedicalItemSegmentation> mMedicalItemSegmentationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemSegmentationService.createMMedicalItemSegmentation(mMedicalItemSegmentation));
        return ResponseEntity.status(mMedicalItemSegmentationResponse.getStatus()).body(mMedicalItemSegmentationResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMMedicalItemSegmentation(@Valid @RequestBody MMedicalItemSegmentation mMedicalItemSegmentation, @PathVariable Long id) {
        if (!Objects.equals(mMedicalItemSegmentation.getId(), id)) {
            mMedicalItemSegmentation.setId(id);
        }

        Response<Object> mMedicalItemSegmentationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalItemSegmentationService.updateMMedicalItemSegmentation(mMedicalItemSegmentation));
        return ResponseEntity.status(mMedicalItemSegmentationResponse.getStatus()).body(mMedicalItemSegmentationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMMedicalItemSegmentation(@PathVariable Long id) {
        this.mMedicalItemSegmentationService.deleteMMedicalItemSegmentation(id);
        Response<Object> mMedicalItemSegmentationResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mMedicalItemSegmentationResponse.getStatus()).body(mMedicalItemSegmentationResponse);
    }
}
