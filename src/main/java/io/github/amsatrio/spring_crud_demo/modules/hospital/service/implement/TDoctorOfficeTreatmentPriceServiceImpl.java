package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeTreatmentPrice;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TDoctorOfficeTreatmentPriceRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.TDoctorOfficeTreatmentPriceService;
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
public class TDoctorOfficeTreatmentPriceServiceImpl implements TDoctorOfficeTreatmentPriceService {

    private final TDoctorOfficeTreatmentPriceRepository tDoctorOfficeTreatmentPriceRepository;

    public TDoctorOfficeTreatmentPriceServiceImpl(
            TDoctorOfficeTreatmentPriceRepository tDoctorOfficeTreatmentPriceRepository) {
        this.tDoctorOfficeTreatmentPriceRepository = tDoctorOfficeTreatmentPriceRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "tDoctorOfficeTreatmentPriceCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<TDoctorOfficeTreatmentPrice> getPageTDoctorOfficeTreatmentPrice(
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
        Specification<TDoctorOfficeTreatmentPrice> tDoctorOfficeTreatmentPriceSpecification = (root, query, cb) -> cb.conjunction();

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPrice = new TDoctorOfficeTreatmentPrice();

                Class<? extends TDoctorOfficeTreatmentPrice> tDoctorOfficeTreatmentPriceClass = tDoctorOfficeTreatmentPrice.getClass();

                Field[] fields = tDoctorOfficeTreatmentPriceClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageTDoctorOfficeTreatmentPrice > GLOBAL FILTERING > error ", exception);
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
                            log.error("getPageTDoctorOfficeTreatmentPrice > error ", exception);
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
                                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification
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
                                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    tDoctorOfficeTreatmentPriceSpecification = tDoctorOfficeTreatmentPriceSpecification
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
            log.error("getPageTDoctorOfficeTreatmentPrice > CUSTOM FILTERING > error ", exception);
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

        return this.tDoctorOfficeTreatmentPriceRepository.findAll(tDoctorOfficeTreatmentPriceSpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "tDoctorOfficeTreatmentPriceCache", key = "#id")
    public TDoctorOfficeTreatmentPrice getTDoctorOfficeTreatmentPrice(Long id) {
        Optional<TDoctorOfficeTreatmentPrice> optionalTDoctorOfficeTreatmentPrice = this.tDoctorOfficeTreatmentPriceRepository.findById(id);
        if (!optionalTDoctorOfficeTreatmentPrice.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalTDoctorOfficeTreatmentPrice.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeTreatmentPriceCache", allEntries = true)
    public void deleteAllTDoctorOfficeTreatmentPrice(List<TDoctorOfficeTreatmentPrice> tDoctorOfficeTreatmentPriceList) {
        this.tDoctorOfficeTreatmentPriceRepository.deleteAll(tDoctorOfficeTreatmentPriceList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeTreatmentPriceCache", allEntries = true)
    public void deleteTDoctorOfficeTreatmentPrice(Long id) {
        this.tDoctorOfficeTreatmentPriceRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeTreatmentPriceCache", allEntries = true)
    public TDoctorOfficeTreatmentPrice updateTDoctorOfficeTreatmentPrice(TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPrice) {
        Optional<TDoctorOfficeTreatmentPrice> optionalTDoctorOfficeTreatmentPrice = this.tDoctorOfficeTreatmentPriceRepository.findById(tDoctorOfficeTreatmentPrice.getId());
        if (!optionalTDoctorOfficeTreatmentPrice.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + tDoctorOfficeTreatmentPrice.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        tDoctorOfficeTreatmentPrice.setCreatedBy(optionalTDoctorOfficeTreatmentPrice.get().getCreatedBy());
        tDoctorOfficeTreatmentPrice.setCreatedOn(optionalTDoctorOfficeTreatmentPrice.get().getCreatedOn());

        tDoctorOfficeTreatmentPrice.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        tDoctorOfficeTreatmentPrice.setModifiedOn(new Date());

        if(tDoctorOfficeTreatmentPrice.getIsDelete() == null){
            tDoctorOfficeTreatmentPrice.setIsDelete(false);
        }
        if (tDoctorOfficeTreatmentPrice.getIsDelete() == true) {
            tDoctorOfficeTreatmentPrice.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            tDoctorOfficeTreatmentPrice.setDeletedOn(new Date());
            tDoctorOfficeTreatmentPrice.setIsDelete(true);
        } else {
            tDoctorOfficeTreatmentPrice.setDeletedBy(null);
            tDoctorOfficeTreatmentPrice.setDeletedOn(null);
            tDoctorOfficeTreatmentPrice.setIsDelete(false);
        }

        return this.tDoctorOfficeTreatmentPriceRepository.save(tDoctorOfficeTreatmentPrice);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tDoctorOfficeTreatmentPriceCache", allEntries = true)
    public TDoctorOfficeTreatmentPrice createTDoctorOfficeTreatmentPrice(TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPrice) {
        Optional<TDoctorOfficeTreatmentPrice> optionalTDoctorOfficeTreatmentPrice = this.tDoctorOfficeTreatmentPriceRepository.findById(tDoctorOfficeTreatmentPrice.getId());
        if (optionalTDoctorOfficeTreatmentPrice.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + tDoctorOfficeTreatmentPrice.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        tDoctorOfficeTreatmentPrice.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        tDoctorOfficeTreatmentPrice.setCreatedOn(new Date());
        tDoctorOfficeTreatmentPrice.setModifiedBy(null);
        tDoctorOfficeTreatmentPrice.setModifiedOn(null);
        tDoctorOfficeTreatmentPrice.setDeletedBy(null);
        tDoctorOfficeTreatmentPrice.setDeletedOn(null);
        tDoctorOfficeTreatmentPrice.setIsDelete(false);

        return this.tDoctorOfficeTreatmentPriceRepository.save(tDoctorOfficeTreatmentPrice);
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
