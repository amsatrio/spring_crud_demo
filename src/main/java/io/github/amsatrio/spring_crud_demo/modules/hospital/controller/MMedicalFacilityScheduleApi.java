package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacilitySchedule;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMedicalFacilityScheduleService;
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
@RequestMapping("/v1/m-medical-facility-schedule")
public class MMedicalFacilityScheduleApi {

    private final MMedicalFacilityScheduleService mMedicalFacilityScheduleService;
    private final HttpServletRequest httpServletRequest;

    public MMedicalFacilityScheduleApi(
            MMedicalFacilityScheduleService mMedicalFacilityScheduleService,
            HttpServletRequest httpServletRequest) {
        this.mMedicalFacilityScheduleService = mMedicalFacilityScheduleService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMMedicalFacilitySchedule(
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
            log.error("mMedicalFacilitySchedule > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MMedicalFacilitySchedule>> mMedicalFacilityScheduleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mMedicalFacilityScheduleService.getPageMMedicalFacilitySchedule(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mMedicalFacilityScheduleResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMMedicalFacilitySchedule(@PathVariable Long id) {
        Response<MMedicalFacilitySchedule> mMedicalFacilityScheduleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityScheduleService.getMMedicalFacilitySchedule(id));
        return ResponseEntity.status(mMedicalFacilityScheduleResponse.getStatus()).body(mMedicalFacilityScheduleResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMMedicalFacilityScheduleHeader() {
        Response<Object> mMedicalFacilityScheduleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MMedicalFacilitySchedule()));
        return ResponseEntity.status(mMedicalFacilityScheduleResponse.getStatus()).body(mMedicalFacilityScheduleResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMMedicalFacilitySchedule(@Valid @RequestBody MMedicalFacilitySchedule mMedicalFacilitySchedule) {
        Response<MMedicalFacilitySchedule> mMedicalFacilityScheduleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityScheduleService.createMMedicalFacilitySchedule(mMedicalFacilitySchedule));
        return ResponseEntity.status(mMedicalFacilityScheduleResponse.getStatus()).body(mMedicalFacilityScheduleResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMMedicalFacilitySchedule(@Valid @RequestBody MMedicalFacilitySchedule mMedicalFacilitySchedule, @PathVariable Long id) {
        if (!Objects.equals(mMedicalFacilitySchedule.getId(), id)) {
            mMedicalFacilitySchedule.setId(id);
        }

        Response<Object> mMedicalFacilityScheduleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMedicalFacilityScheduleService.updateMMedicalFacilitySchedule(mMedicalFacilitySchedule));
        return ResponseEntity.status(mMedicalFacilityScheduleResponse.getStatus()).body(mMedicalFacilityScheduleResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMMedicalFacilitySchedule(@PathVariable Long id) {
        this.mMedicalFacilityScheduleService.deleteMMedicalFacilitySchedule(id);
        Response<Object> mMedicalFacilityScheduleResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mMedicalFacilityScheduleResponse.getStatus()).body(mMedicalFacilityScheduleResponse);
    }
}
