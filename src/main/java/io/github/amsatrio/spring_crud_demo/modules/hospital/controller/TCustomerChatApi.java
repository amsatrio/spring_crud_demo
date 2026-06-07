package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerChat;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TCustomerChatService;
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
@RequestMapping("/v1/t-customer-chat")
public class TCustomerChatApi {

    private final TCustomerChatService tCustomerChatService;
    private final HttpServletRequest httpServletRequest;

    public TCustomerChatApi(
            TCustomerChatService tCustomerChatService,
            HttpServletRequest httpServletRequest) {
        this.tCustomerChatService = tCustomerChatService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageTCustomerChat(
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
            log.error("tCustomerChat > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<TCustomerChat>> tCustomerChatResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                tCustomerChatService.getPageTCustomerChat(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(tCustomerChatResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTCustomerChat(@PathVariable Long id) {
        Response<TCustomerChat> tCustomerChatResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerChatService.getTCustomerChat(id));
        return ResponseEntity.status(tCustomerChatResponse.getStatus()).body(tCustomerChatResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getTCustomerChatHeader() {
        Response<Object> tCustomerChatResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new TCustomerChat()));
        return ResponseEntity.status(tCustomerChatResponse.getStatus()).body(tCustomerChatResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createTCustomerChat(@Valid @RequestBody TCustomerChat tCustomerChat) {
        Response<TCustomerChat> tCustomerChatResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerChatService.createTCustomerChat(tCustomerChat));
        return ResponseEntity.status(tCustomerChatResponse.getStatus()).body(tCustomerChatResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTCustomerChat(@Valid @RequestBody TCustomerChat tCustomerChat, @PathVariable Long id) {
        if (!Objects.equals(tCustomerChat.getId(), id)) {
            tCustomerChat.setId(id);
        }

        Response<Object> tCustomerChatResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.tCustomerChatService.updateTCustomerChat(tCustomerChat));
        return ResponseEntity.status(tCustomerChatResponse.getStatus()).body(tCustomerChatResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTCustomerChat(@PathVariable Long id) {
        this.tCustomerChatService.deleteTCustomerChat(id);
        Response<Object> tCustomerChatResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(tCustomerChatResponse.getStatus()).body(tCustomerChatResponse);
    }
}
