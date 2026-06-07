package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TMedicalItemPurchaseDetail;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TMedicalItemPurchaseDetailService;
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
@RequestMapping("/v1/t-medical-item-purchase-detail")
public class TMedicalItemPurchaseDetailApi {

    private final TMedicalItemPurchaseDetailService tMedicalItemPurchaseDetailService;
    private final HttpServletRequest httpServletRequest;

    public TMedicalItemPurchaseDetailApi(
            TMedicalItemPurchaseDetailService tMedicalItemPurchaseDetailService,
            HttpServletRequest httpServletRequest) {
        this.tMedicalItemPurchaseDetailService = tMedicalItemPurchaseDetailService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTMedicalItemPurchaseDetail(
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
            log.error("tMedicalItemPurchaseDetail > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TMedicalItemPurchaseDetail>> tMedicalItemPurchaseDetailResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tMedicalItemPurchaseDetailService.getPageTMedicalItemPurchaseDetail(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tMedicalItemPurchaseDetailResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTMedicalItemPurchaseDetail(@PathVariable Long id) {
        Response<TMedicalItemPurchaseDetail> tMedicalItemPurchaseDetailResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tMedicalItemPurchaseDetailService.getTMedicalItemPurchaseDetail(id));
        return ResponseEntity.status(tMedicalItemPurchaseDetailResponse.getStatus()).body(tMedicalItemPurchaseDetailResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTMedicalItemPurchaseDetailHeader() {
        Response<Object> tMedicalItemPurchaseDetailResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TMedicalItemPurchaseDetail()));
        return ResponseEntity.status(tMedicalItemPurchaseDetailResponse.getStatus()).body(tMedicalItemPurchaseDetailResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTMedicalItemPurchaseDetail(@Valid @RequestBody TMedicalItemPurchaseDetail tMedicalItemPurchaseDetail) {
        Response<TMedicalItemPurchaseDetail> tMedicalItemPurchaseDetailResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tMedicalItemPurchaseDetailService.createTMedicalItemPurchaseDetail(tMedicalItemPurchaseDetail));
        return ResponseEntity.status(tMedicalItemPurchaseDetailResponse.getStatus()).body(tMedicalItemPurchaseDetailResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTMedicalItemPurchaseDetail(@Valid @RequestBody TMedicalItemPurchaseDetail tMedicalItemPurchaseDetail, @PathVariable Long id) {
        if (!Objects.equals(tMedicalItemPurchaseDetail.getId(), id)) {
            tMedicalItemPurchaseDetail.setId(id);
        }

        Response<Object> tMedicalItemPurchaseDetailResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tMedicalItemPurchaseDetailService.updateTMedicalItemPurchaseDetail(tMedicalItemPurchaseDetail));
        return ResponseEntity.status(tMedicalItemPurchaseDetailResponse.getStatus()).body(tMedicalItemPurchaseDetailResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTMedicalItemPurchaseDetail(@PathVariable Long id) {
        this.tMedicalItemPurchaseDetailService.deleteTMedicalItemPurchaseDetail(id);
        Response<Object> tMedicalItemPurchaseDetailResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tMedicalItemPurchaseDetailResponse.getStatus()).body(tMedicalItemPurchaseDetailResponse);
    }
}
