package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMenu;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMenuService;
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
@RequestMapping("/v1/m-menu")
public class MMenuApi {

    private final MMenuService mMenuService;
    private final HttpServletRequest httpServletRequest;

    public MMenuApi(
            MMenuService mMenuService,
            HttpServletRequest httpServletRequest) {
        this.mMenuService = mMenuService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMMenu(
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
            log.error("mMenu > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MMenu>> mMenuResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mMenuService.getPageMMenu(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mMenuResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMMenu(@PathVariable Long id) {
        Response<MMenu> mMenuResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMenuService.getMMenu(id));
        return ResponseEntity.status(mMenuResponse.getStatus()).body(mMenuResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMMenuHeader() {
        Response<Object> mMenuResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MMenu()));
        return ResponseEntity.status(mMenuResponse.getStatus()).body(mMenuResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMMenu(@Valid @RequestBody MMenu mMenu) {
        Response<MMenu> mMenuResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMenuService.createMMenu(mMenu));
        return ResponseEntity.status(mMenuResponse.getStatus()).body(mMenuResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMMenu(@Valid @RequestBody MMenu mMenu, @PathVariable Long id) {
        if (!Objects.equals(mMenu.getId(), id)) {
            mMenu.setId(id);
        }

        Response<Object> mMenuResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mMenuService.updateMMenu(mMenu));
        return ResponseEntity.status(mMenuResponse.getStatus()).body(mMenuResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMMenu(@PathVariable Long id) {
        this.mMenuService.deleteMMenu(id);
        Response<Object> mMenuResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mMenuResponse.getStatus()).body(mMenuResponse);
    }
}
