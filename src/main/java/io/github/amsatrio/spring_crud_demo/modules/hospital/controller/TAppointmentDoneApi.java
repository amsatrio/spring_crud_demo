package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointmentDone;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TAppointmentDoneService;
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
@RequestMapping("/v1/t-appointment-done")
public class TAppointmentDoneApi {

    private final TAppointmentDoneService tAppointmentDoneService;
    private final HttpServletRequest httpServletRequest;

    public TAppointmentDoneApi(
            TAppointmentDoneService tAppointmentDoneService,
            HttpServletRequest httpServletRequest) {
        this.tAppointmentDoneService = tAppointmentDoneService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTAppointmentDone(
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
            log.error("tAppointmentDone > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TAppointmentDone>> tAppointmentDoneResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tAppointmentDoneService.getPageTAppointmentDone(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tAppointmentDoneResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTAppointmentDone(@PathVariable Long id) {
        Response<TAppointmentDone> tAppointmentDoneResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentDoneService.getTAppointmentDone(id));
        return ResponseEntity.status(tAppointmentDoneResponse.getStatus()).body(tAppointmentDoneResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTAppointmentDoneHeader() {
        Response<Object> tAppointmentDoneResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TAppointmentDone()));
        return ResponseEntity.status(tAppointmentDoneResponse.getStatus()).body(tAppointmentDoneResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTAppointmentDone(@Valid @RequestBody TAppointmentDone tAppointmentDone) {
        Response<TAppointmentDone> tAppointmentDoneResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentDoneService.createTAppointmentDone(tAppointmentDone));
        return ResponseEntity.status(tAppointmentDoneResponse.getStatus()).body(tAppointmentDoneResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTAppointmentDone(@Valid @RequestBody TAppointmentDone tAppointmentDone, @PathVariable Long id) {
        if (!Objects.equals(tAppointmentDone.getId(), id)) {
            tAppointmentDone.setId(id);
        }

        Response<Object> tAppointmentDoneResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tAppointmentDoneService.updateTAppointmentDone(tAppointmentDone));
        return ResponseEntity.status(tAppointmentDoneResponse.getStatus()).body(tAppointmentDoneResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTAppointmentDone(@PathVariable Long id) {
        this.tAppointmentDoneService.deleteTAppointmentDone(id);
        Response<Object> tAppointmentDoneResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tAppointmentDoneResponse.getStatus()).body(tAppointmentDoneResponse);
    }
}
