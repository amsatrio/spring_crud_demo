package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodataAttachment;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MBiodataAttachmentService;
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
@RequestMapping("/v1/m-biodata-attachment")
public class MBiodataAttachmentApi {

    private final MBiodataAttachmentService mBiodataAttachmentService;
    private final HttpServletRequest httpServletRequest;

    public MBiodataAttachmentApi(
            MBiodataAttachmentService mBiodataAttachmentService,
            HttpServletRequest httpServletRequest) {
        this.mBiodataAttachmentService = mBiodataAttachmentService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getPageMBiodataAttachment(
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
            log.error("mBiodataAttachment > parse error ", exception);
            
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "payload is invalid",
                    new HttpHeaders(), null, null);
        }

        Response<Page<MBiodataAttachment>> mBiodataAttachmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                mBiodataAttachmentService.getPageMBiodataAttachment(page, size, sorts, filters, search));
        return ResponseEntity.status(200).body(mBiodataAttachmentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMBiodataAttachment(@PathVariable Long id) {
        Response<MBiodataAttachment> mBiodataAttachmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataAttachmentService.getMBiodataAttachment(id));
        return ResponseEntity.status(mBiodataAttachmentResponse.getStatus()).body(mBiodataAttachmentResponse);
    }

    @GetMapping("/header")
    public ResponseEntity<Object> getMBiodataAttachmentHeader() {
        Response<Object> mBiodataAttachmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                Converter.modelToHeaderMap(new MBiodataAttachment()));
        return ResponseEntity.status(mBiodataAttachmentResponse.getStatus()).body(mBiodataAttachmentResponse);
    }

    @PostMapping
    public ResponseEntity<Object> createMBiodataAttachment(@Valid @RequestBody MBiodataAttachment mBiodataAttachment) {
        Response<MBiodataAttachment> mBiodataAttachmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataAttachmentService.createMBiodataAttachment(mBiodataAttachment));
        return ResponseEntity.status(mBiodataAttachmentResponse.getStatus()).body(mBiodataAttachmentResponse);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMBiodataAttachment(@Valid @RequestBody MBiodataAttachment mBiodataAttachment, @PathVariable Long id) {
        if (!Objects.equals(mBiodataAttachment.getId(), id)) {
            mBiodataAttachment.setId(id);
        }

        Response<Object> mBiodataAttachmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                this.mBiodataAttachmentService.updateMBiodataAttachment(mBiodataAttachment));
        return ResponseEntity.status(mBiodataAttachmentResponse.getStatus()).body(mBiodataAttachmentResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMBiodataAttachment(@PathVariable Long id) {
        this.mBiodataAttachmentService.deleteMBiodataAttachment(id);
        Response<Object> mBiodataAttachmentResponse = new Response<>(
                this.httpServletRequest.getRequestURI(),
                new Date(),
                HttpStatus.OK.value(),
                "success",
                null);
        return ResponseEntity.status(mBiodataAttachmentResponse.getStatus()).body(mBiodataAttachmentResponse);
    }
}
