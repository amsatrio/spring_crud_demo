package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOffice;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TDoctorOfficeRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TDoctorOfficeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@EnableCaching
public class TDoctorOfficeServiceImpl implements TDoctorOfficeService {

    private final TDoctorOfficeRepository tDoctorOfficeRepository;

    public TDoctorOfficeServiceImpl(
            TDoctorOfficeRepository tDoctorOfficeRepository) {
        this.tDoctorOfficeRepository = tDoctorOfficeRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "tDoctorOfficeCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<TDoctorOffice> getPageTDoctorOffice(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // === SORTING
        Sort sortOrder = Sort.unsorted();
        for (SortRequest sortRequest : sorts) {
            if (sortRequest.getId().isEmpty()) {
                continue;
            }
            sortOrder = Sort.by(sortRequest.getId()).ascending();
            if (sortRequest.isDesc()) {
                sortOrder = Sort.by(sortRequest.getId()).descending();
            }
        }

        // === FILTERING
        Specification<TDoctorOffice> tDoctorOfficeSpecification = Specification.where(null);

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                TDoctorOffice tDoctorOffice = new TDoctorOffice();

                Class<? extends TDoctorOffice> tDoctorOfficeClass = tDoctorOffice.getClass();

                Field[] fields = tDoctorOfficeClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    tDoctorOfficeSpecification = tDoctorOfficeSpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageTDoctorOffice > GLOBAL FILTERING > error ", exception);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Error:  " + exception.getMessage(),
                    new HttpHeaders(),
                    null,
                    null);
        }

        // === CUSTOM FILTERING
        try {
            if (!filters.isEmpty()) {

                for (FilterRequest filterRequest : filters) {

                    Object object1 = null;
                    Object object2 = null;
                    if (filterRequest.getValue().toString().startsWith("[")
                            && filterRequest.getValue().toString().endsWith("]")) {
                        try {
                            List<Object> objectList = objectMapper.convertValue(filterRequest.getValue(),
                                    new TypeReference<List<Object>>() {
                                    });
                            if (objectList.size() > 1) {
                                filterRequest.setMatchMode(FilterMatchMode.BETWEEN);
                                object1 = objectList.get(0);
                                object2 = objectList.get(1);
                            } else if (objectList.size() == 1) {
                                object1 = objectList.get(0);
                            } else {
                                continue;
                            }
                        } catch (Exception exception) {
                            log.error("getPageTDoctorOffice > error ", exception);
                        }
                    } else {
                        object1 = filterRequest.getValue();
                    }

                    final Object finaObject1 = object1;
                    final Object finaObject2 = object2;
                    switch (filterRequest.getMatchMode()) {
                        case BETWEEN:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    break;
                                case DATE:
                                    String[] stringValue = filterRequest.getValue().toString().split(" - ");
                                    object1 = stringValue[0];
                                    object2 = stringValue[1];
                                    Date date1 = simpleDateFormat.parse(object1.toString());
                                    Date date2 = simpleDateFormat.parse(object2.toString());
                                    tDoctorOfficeSpecification = tDoctorOfficeSpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    tDoctorOfficeSpecification = tDoctorOfficeSpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    tDoctorOfficeSpecification = tDoctorOfficeSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    tDoctorOfficeSpecification = tDoctorOfficeSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    tDoctorOfficeSpecification = tDoctorOfficeSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    tDoctorOfficeSpecification = tDoctorOfficeSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                            }
                            break;
                        case NOT:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    break;
                            }
                            break;
                        case LESS_THAN:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    break;
                            }
                            break;
                        case GREATER_THAN:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    break;
                            }
                            break;
                        default: // CONTAINS
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    tDoctorOfficeSpecification = tDoctorOfficeSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    tDoctorOfficeSpecification = tDoctorOfficeSpecification
                                            .and((root, query, builder) -> builder.like(
                                                    root.get(filterRequest.getId()),
                                                    "%" + finaObject1.toString()
                                                            .replaceAll("%", "")
                                                            .replaceAll("'", "")
                                                            .replaceAll("`", "") + "%"));
                                    break;
                            }
                            break;
                    }
                }
            }
        } catch (Exception exception) {
            log.error("getPageTDoctorOffice > CUSTOM FILTERING > error ", exception);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Error:  " + exception.getMessage(),
                    new HttpHeaders(),
                    null,
                    null);
        }

        // === PAGING
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        return this.tDoctorOfficeRepository.findAll(tDoctorOfficeSpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "tDoctorOfficeCache", key = "#id")
    public TDoctorOffice getTDoctorOffice(Long id) {
        Optional<TDoctorOffice> optionalTDoctorOffice = this.tDoctorOfficeRepository.findById(id);
        if (!optionalTDoctorOffice.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalTDoctorOffice.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeCache", allEntries = true)
    public void deleteAllTDoctorOffice(List<TDoctorOffice> tDoctorOfficeList) {
        this.tDoctorOfficeRepository.deleteAll(tDoctorOfficeList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeCache", allEntries = true)
    public void deleteTDoctorOffice(Long id) {
        this.tDoctorOfficeRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeCache", allEntries = true)
    public TDoctorOffice updateTDoctorOffice(TDoctorOffice tDoctorOffice) {
        Optional<TDoctorOffice> optionalTDoctorOffice = this.tDoctorOfficeRepository.findById(tDoctorOffice.getId());
        if (!optionalTDoctorOffice.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + tDoctorOffice.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        tDoctorOffice.setCreatedBy(optionalTDoctorOffice.get().getCreatedBy());
        tDoctorOffice.setCreatedOn(optionalTDoctorOffice.get().getCreatedOn());

        tDoctorOffice.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        tDoctorOffice.setModifiedOn(new Date());

        if(tDoctorOffice.getIsDelete() == null){
            tDoctorOffice.setIsDelete(false);
        }
        if (tDoctorOffice.getIsDelete() == true) {
            tDoctorOffice.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            tDoctorOffice.setDeletedOn(new Date());
            tDoctorOffice.setIsDelete(true);
        } else {
            tDoctorOffice.setDeletedBy(null);
            tDoctorOffice.setDeletedOn(null);
            tDoctorOffice.setIsDelete(false);
        }

        return this.tDoctorOfficeRepository.save(tDoctorOffice);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeCache", allEntries = true)
    public TDoctorOffice createTDoctorOffice(TDoctorOffice tDoctorOffice) {
        Optional<TDoctorOffice> optionalTDoctorOffice = this.tDoctorOfficeRepository.findById(tDoctorOffice.getId());
        if (optionalTDoctorOffice.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + tDoctorOffice.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        tDoctorOffice.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        tDoctorOffice.setCreatedOn(new Date());
        tDoctorOffice.setModifiedBy(null);
        tDoctorOffice.setModifiedOn(null);
        tDoctorOffice.setDeletedBy(null);
        tDoctorOffice.setDeletedOn(null);
        tDoctorOffice.setIsDelete(false);

        return this.tDoctorOfficeRepository.save(tDoctorOffice);
    }
    
    private Long getUserDetailsIdFromAuthenticationSecurity(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId();
        }
        
        return 0L;
    }

}
