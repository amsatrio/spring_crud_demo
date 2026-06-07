package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerRegisteredCard;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerRegisteredCardService;
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
@RequestMapping("/v1/t-customer-registered-card")
public class TCustomerRegisteredCardApi {

    private final TCustomerRegisteredCardService tCustomerRegisteredCardService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerRegisteredCardApi(
            TCustomerRegisteredCardService tCustomerRegisteredCardService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerRegisteredCardService = tCustomerRegisteredCardService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerRegisteredCard(
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
            log.error("tCustomerRegisteredCard > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerRegisteredCard>> tCustomerRegisteredCardResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerRegisteredCardService.getPageTCustomerRegisteredCard(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerRegisteredCardResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerRegisteredCard(@PathVariable Long id) {
        Response<TCustomerRegisteredCard> tCustomerRegisteredCardResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerRegisteredCardService.getTCustomerRegisteredCard(id));
        return ResponseEntity.status(tCustomerRegisteredCardResponse.getStatus()).body(tCustomerRegisteredCardResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerRegisteredCardHeader() {
        Response<Object> tCustomerRegisteredCardResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerRegisteredCard()));
        return ResponseEntity.status(tCustomerRegisteredCardResponse.getStatus()).body(tCustomerRegisteredCardResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerRegisteredCard(@Valid @RequestBody TCustomerRegisteredCard tCustomerRegisteredCard) {
        Response<TCustomerRegisteredCard> tCustomerRegisteredCardResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerRegisteredCardService.createTCustomerRegisteredCard(tCustomerRegisteredCard));
        return ResponseEntity.status(tCustomerRegisteredCardResponse.getStatus()).body(tCustomerRegisteredCardResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerRegisteredCard(@Valid @RequestBody TCustomerRegisteredCard tCustomerRegisteredCard, @PathVariable Long id) {
        if (!Objects.equals(tCustomerRegisteredCard.getId(), id)) {
            tCustomerRegisteredCard.setId(id);
        }

        Response<Object> tCustomerRegisteredCardResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerRegisteredCardService.updateTCustomerRegisteredCard(tCustomerRegisteredCard));
        return ResponseEntity.status(tCustomerRegisteredCardResponse.getStatus()).body(tCustomerRegisteredCardResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerRegisteredCard(@PathVariable Long id) {
        this.tCustomerRegisteredCardService.deleteTCustomerRegisteredCard(id);
        Response<Object> tCustomerRegisteredCardResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerRegisteredCardResponse.getStatus()).body(tCustomerRegisteredCardResponse);
    }
}
