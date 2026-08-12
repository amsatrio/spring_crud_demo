package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeTreatment;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TDoctorOfficeTreatmentRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TDoctorOfficeTreatmentService;
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
public class TDoctorOfficeTreatmentServiceImpl implements TDoctorOfficeTreatmentService {

    private final TDoctorOfficeTreatmentRepository tDoctorOfficeTreatmentRepository;

    public TDoctorOfficeTreatmentServiceImpl(
            TDoctorOfficeTreatmentRepository tDoctorOfficeTreatmentRepository) {
        this.tDoctorOfficeTreatmentRepository = tDoctorOfficeTreatmentRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "tDoctorOfficeTreatmentCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<TDoctorOfficeTreatment> getPageTDoctorOfficeTreatment(
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
        Specification<TDoctorOfficeTreatment> tDoctorOfficeTreatmentSpecification = (root, query, cb) -> cb.conjunction();

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                TDoctorOfficeTreatment tDoctorOfficeTreatment = new TDoctorOfficeTreatment();

                Class<? extends TDoctorOfficeTreatment> tDoctorOfficeTreatmentClass = tDoctorOfficeTreatment.getClass();

                Field[] fields = tDoctorOfficeTreatmentClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageTDoctorOfficeTreatment > GLOBAL FILTERING > error ", exception);
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
                            log.error("getPageTDoctorOfficeTreatment > error ", exception);
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
                                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification
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
                                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    tDoctorOfficeTreatmentSpecification = tDoctorOfficeTreatmentSpecification
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
            log.error("getPageTDoctorOfficeTreatment > CUSTOM FILTERING > error ", exception);
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

        return this.tDoctorOfficeTreatmentRepository.findAll(tDoctorOfficeTreatmentSpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "tDoctorOfficeTreatmentCache", key = "#id")
    public TDoctorOfficeTreatment getTDoctorOfficeTreatment(Long id) {
        Optional<TDoctorOfficeTreatment> optionalTDoctorOfficeTreatment = this.tDoctorOfficeTreatmentRepository.findById(id);
        if (!optionalTDoctorOfficeTreatment.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalTDoctorOfficeTreatment.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeTreatmentCache", allEntries = true)
    public void deleteAllTDoctorOfficeTreatment(List<TDoctorOfficeTreatment> tDoctorOfficeTreatmentList) {
        this.tDoctorOfficeTreatmentRepository.deleteAll(tDoctorOfficeTreatmentList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeTreatmentCache", allEntries = true)
    public void deleteTDoctorOfficeTreatment(Long id) {
        this.tDoctorOfficeTreatmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeTreatmentCache", allEntries = true)
    public TDoctorOfficeTreatment updateTDoctorOfficeTreatment(TDoctorOfficeTreatment tDoctorOfficeTreatment) {
        Optional<TDoctorOfficeTreatment> optionalTDoctorOfficeTreatment = this.tDoctorOfficeTreatmentRepository.findById(tDoctorOfficeTreatment.getId());
        if (!optionalTDoctorOfficeTreatment.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + tDoctorOfficeTreatment.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        tDoctorOfficeTreatment.setCreatedBy(optionalTDoctorOfficeTreatment.get().getCreatedBy());
        tDoctorOfficeTreatment.setCreatedOn(optionalTDoctorOfficeTreatment.get().getCreatedOn());

        tDoctorOfficeTreatment.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        tDoctorOfficeTreatment.setModifiedOn(new Date());

        if(tDoctorOfficeTreatment.getIsDelete() == null){
            tDoctorOfficeTreatment.setIsDelete(false);
        }
        if (tDoctorOfficeTreatment.getIsDelete() == true) {
            tDoctorOfficeTreatment.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            tDoctorOfficeTreatment.setDeletedOn(new Date());
            tDoctorOfficeTreatment.setIsDelete(true);
        } else {
            tDoctorOfficeTreatment.setDeletedBy(null);
            tDoctorOfficeTreatment.setDeletedOn(null);
            tDoctorOfficeTreatment.setIsDelete(false);
        }

        return this.tDoctorOfficeTreatmentRepository.save(tDoctorOfficeTreatment);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeTreatmentCache", allEntries = true)
    public TDoctorOfficeTreatment createTDoctorOfficeTreatment(TDoctorOfficeTreatment tDoctorOfficeTreatment) {
        Optional<TDoctorOfficeTreatment> optionalTDoctorOfficeTreatment = this.tDoctorOfficeTreatmentRepository.findById(tDoctorOfficeTreatment.getId());
        if (optionalTDoctorOfficeTreatment.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + tDoctorOfficeTreatment.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        tDoctorOfficeTreatment.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        tDoctorOfficeTreatment.setCreatedOn(new Date());
        tDoctorOfficeTreatment.setModifiedBy(null);
        tDoctorOfficeTreatment.setModifiedOn(null);
        tDoctorOfficeTreatment.setDeletedBy(null);
        tDoctorOfficeTreatment.setDeletedOn(null);
        tDoctorOfficeTreatment.setIsDelete(false);

        return this.tDoctorOfficeTreatmentRepository.save(tDoctorOfficeTreatment);
    }
    
    private Long getUserDetailsIdFromAuthenticationSecurity(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if(authentication == null) return 0L;
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId();
        }
        
        return 0L;
    }

}
