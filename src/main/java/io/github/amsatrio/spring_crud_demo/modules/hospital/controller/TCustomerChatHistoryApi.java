package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerChatHistory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerChatHistoryService;
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
@RequestMapping("/v1/t-customer-chat-history")
public class TCustomerChatHistoryApi {

    private final TCustomerChatHistoryService tCustomerChatHistoryService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerChatHistoryApi(
            TCustomerChatHistoryService tCustomerChatHistoryService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerChatHistoryService = tCustomerChatHistoryService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerChatHistory(
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
            log.error("tCustomerChatHistory > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerChatHistory>> tCustomerChatHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerChatHistoryService.getPageTCustomerChatHistory(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerChatHistoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerChatHistory(@PathVariable Long id) {
        Response<TCustomerChatHistory> tCustomerChatHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerChatHistoryService.getTCustomerChatHistory(id));
        return ResponseEntity.status(tCustomerChatHistoryResponse.getStatus()).body(tCustomerChatHistoryResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerChatHistoryHeader() {
        Response<Object> tCustomerChatHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerChatHistory()));
        return ResponseEntity.status(tCustomerChatHistoryResponse.getStatus()).body(tCustomerChatHistoryResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerChatHistory(@Valid @RequestBody TCustomerChatHistory tCustomerChatHistory) {
        Response<TCustomerChatHistory> tCustomerChatHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerChatHistoryService.createTCustomerChatHistory(tCustomerChatHistory));
        return ResponseEntity.status(tCustomerChatHistoryResponse.getStatus()).body(tCustomerChatHistoryResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerChatHistory(@Valid @RequestBody TCustomerChatHistory tCustomerChatHistory, @PathVariable Long id) {
        if (!Objects.equals(tCustomerChatHistory.getId(), id)) {
            tCustomerChatHistory.setId(id);
        }

        Response<Object> tCustomerChatHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerChatHistoryService.updateTCustomerChatHistory(tCustomerChatHistory));
        return ResponseEntity.status(tCustomerChatHistoryResponse.getStatus()).body(tCustomerChatHistoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerChatHistory(@PathVariable Long id) {
        this.tCustomerChatHistoryService.deleteTCustomerChatHistory(id);
        Response<Object> tCustomerChatHistoryResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerChatHistoryResponse.getStatus()).body(tCustomerChatHistoryResponse);
    }
}
