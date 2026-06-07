package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MPaymentMethod;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MPaymentMethodService;
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
@RequestMapping("/v1/m-payment-method")
public class MPaymentMethodApi {

    private final MPaymentMethodService mPaymentMethodService;
    private final HttpServletRequest httpServletRequest;

    public MPaymentMethodApi(
            MPaymentMethodService mPaymentMethodService,
            HttpServletRequest httpServletRequest) {
        this.mPaymentMethodService = mPaymentMethodService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMPaymentMethod(
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
            log.error("mPaymentMethod > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MPaymentMethod>> mPaymentMethodResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mPaymentMethodService.getPageMPaymentMethod(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mPaymentMethodResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMPaymentMethod(@PathVariable Long id) {
        Response<MPaymentMethod> mPaymentMethodResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mPaymentMethodService.getMPaymentMethod(id));
        return ResponseEntity.status(mPaymentMethodResponse.getStatus()).body(mPaymentMethodResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMPaymentMethodHeader() {
        Response<Object> mPaymentMethodResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MPaymentMethod()));
        return ResponseEntity.status(mPaymentMethodResponse.getStatus()).body(mPaymentMethodResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMPaymentMethod(@Valid @RequestBody MPaymentMethod mPaymentMethod) {
        Response<MPaymentMethod> mPaymentMethodResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mPaymentMethodService.createMPaymentMethod(mPaymentMethod));
        return ResponseEntity.status(mPaymentMethodResponse.getStatus()).body(mPaymentMethodResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMPaymentMethod(@Valid @RequestBody MPaymentMethod mPaymentMethod, @PathVariable Long id) {
        if (!Objects.equals(mPaymentMethod.getId(), id)) {
            mPaymentMethod.setId(id);
        }

        Response<Object> mPaymentMethodResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mPaymentMethodService.updateMPaymentMethod(mPaymentMethod));
        return ResponseEntity.status(mPaymentMethodResponse.getStatus()).body(mPaymentMethodResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMPaymentMethod(@PathVariable Long id) {
        this.mPaymentMethodService.deleteMPaymentMethod(id);
        Response<Object> mPaymentMethodResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mPaymentMethodResponse.getStatus()).body(mPaymentMethodResponse);
    }
}
