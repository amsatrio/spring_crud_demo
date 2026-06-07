package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeTreatmentPrice;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TDoctorOfficeTreatmentPriceService;
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
@RequestMapping("/v1/t-doctor-office-treatment-price")
public class TDoctorOfficeTreatmentPriceApi {

    private final TDoctorOfficeTreatmentPriceService tDoctorOfficeTreatmentPriceService;
    private final HttpServletRequest httpServletRequest;

    public TDoctorOfficeTreatmentPriceApi(
            TDoctorOfficeTreatmentPriceService tDoctorOfficeTreatmentPriceService,
            HttpServletRequest httpServletRequest) {
        this.tDoctorOfficeTreatmentPriceService = tDoctorOfficeTreatmentPriceService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTDoctorOfficeTreatmentPrice(
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
            log.error("tDoctorOfficeTreatmentPrice > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TDoctorOfficeTreatmentPrice>> tDoctorOfficeTreatmentPriceResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tDoctorOfficeTreatmentPriceService.getPageTDoctorOfficeTreatmentPrice(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tDoctorOfficeTreatmentPriceResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTDoctorOfficeTreatmentPrice(@PathVariable Long id) {
        Response<TDoctorOfficeTreatmentPrice> tDoctorOfficeTreatmentPriceResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeTreatmentPriceService.getTDoctorOfficeTreatmentPrice(id));
        return ResponseEntity.status(tDoctorOfficeTreatmentPriceResponse.getStatus()).body(tDoctorOfficeTreatmentPriceResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTDoctorOfficeTreatmentPriceHeader() {
        Response<Object> tDoctorOfficeTreatmentPriceResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TDoctorOfficeTreatmentPrice()));
        return ResponseEntity.status(tDoctorOfficeTreatmentPriceResponse.getStatus()).body(tDoctorOfficeTreatmentPriceResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTDoctorOfficeTreatmentPrice(@Valid @RequestBody TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPrice) {
        Response<TDoctorOfficeTreatmentPrice> tDoctorOfficeTreatmentPriceResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeTreatmentPriceService.createTDoctorOfficeTreatmentPrice(tDoctorOfficeTreatmentPrice));
        return ResponseEntity.status(tDoctorOfficeTreatmentPriceResponse.getStatus()).body(tDoctorOfficeTreatmentPriceResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTDoctorOfficeTreatmentPrice(@Valid @RequestBody TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPrice, @PathVariable Long id) {
        if (!Objects.equals(tDoctorOfficeTreatmentPrice.getId(), id)) {
            tDoctorOfficeTreatmentPrice.setId(id);
        }

        Response<Object> tDoctorOfficeTreatmentPriceResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tDoctorOfficeTreatmentPriceService.updateTDoctorOfficeTreatmentPrice(tDoctorOfficeTreatmentPrice));
        return ResponseEntity.status(tDoctorOfficeTreatmentPriceResponse.getStatus()).body(tDoctorOfficeTreatmentPriceResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTDoctorOfficeTreatmentPrice(@PathVariable Long id) {
        this.tDoctorOfficeTreatmentPriceService.deleteTDoctorOfficeTreatmentPrice(id);
        Response<Object> tDoctorOfficeTreatmentPriceResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tDoctorOfficeTreatmentPriceResponse.getStatus()).body(tDoctorOfficeTreatmentPriceResponse);
    }
}
