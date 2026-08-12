package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacilityCategory;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MMedicalFacilityCategoryRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MMedicalFacilityCategoryService;
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
public class MMedicalFacilityCategoryServiceImpl implements MMedicalFacilityCategoryService {

    private final MMedicalFacilityCategoryRepository mMedicalFacilityCategoryRepository;

    public MMedicalFacilityCategoryServiceImpl(
            MMedicalFacilityCategoryRepository mMedicalFacilityCategoryRepository) {
        this.mMedicalFacilityCategoryRepository = mMedicalFacilityCategoryRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mMedicalFacilityCategoryCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<MMedicalFacilityCategory> getPageMMedicalFacilityCategory(
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
        Specification<MMedicalFacilityCategory> mMedicalFacilityCategorySpecification = (root, query, cb) -> cb.conjunction();

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                MMedicalFacilityCategory mMedicalFacilityCategory = new MMedicalFacilityCategory();

                Class<? extends MMedicalFacilityCategory> mMedicalFacilityCategoryClass = mMedicalFacilityCategory.getClass();

                Field[] fields = mMedicalFacilityCategoryClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageMMedicalFacilityCategory > GLOBAL FILTERING > error ", exception);
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
                            log.error("getPageMMedicalFacilityCategory > error ", exception);
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
                                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification
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
                                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    mMedicalFacilityCategorySpecification = mMedicalFacilityCategorySpecification
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
            log.error("getPageMMedicalFacilityCategory > CUSTOM FILTERING > error ", exception);
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

        return this.mMedicalFacilityCategoryRepository.findAll(mMedicalFacilityCategorySpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mMedicalFacilityCategoryCache", key = "#id")
    public MMedicalFacilityCategory getMMedicalFacilityCategory(Long id) {
        Optional<MMedicalFacilityCategory> optionalMMedicalFacilityCategory = this.mMedicalFacilityCategoryRepository.findById(id);
        if (!optionalMMedicalFacilityCategory.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalMMedicalFacilityCategory.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "mMedicalFacilityCategoryCache", allEntries = true)
    public void deleteAllMMedicalFacilityCategory(List<MMedicalFacilityCategory> mMedicalFacilityCategoryList) {
        this.mMedicalFacilityCategoryRepository.deleteAll(mMedicalFacilityCategoryList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mMedicalFacilityCategoryCache", allEntries = true)
    public void deleteMMedicalFacilityCategory(Long id) {
        this.mMedicalFacilityCategoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mMedicalFacilityCategoryCache", allEntries = true)
    public MMedicalFacilityCategory updateMMedicalFacilityCategory(MMedicalFacilityCategory mMedicalFacilityCategory) {
        Optional<MMedicalFacilityCategory> optionalMMedicalFacilityCategory = this.mMedicalFacilityCategoryRepository.findById(mMedicalFacilityCategory.getId());
        if (!optionalMMedicalFacilityCategory.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + mMedicalFacilityCategory.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        mMedicalFacilityCategory.setCreatedBy(optionalMMedicalFacilityCategory.get().getCreatedBy());
        mMedicalFacilityCategory.setCreatedOn(optionalMMedicalFacilityCategory.get().getCreatedOn());

        mMedicalFacilityCategory.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        mMedicalFacilityCategory.setModifiedOn(new Date());

        if(mMedicalFacilityCategory.getIsDelete() == null){
            mMedicalFacilityCategory.setIsDelete(false);
        }
        if (mMedicalFacilityCategory.getIsDelete() == true) {
            mMedicalFacilityCategory.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            mMedicalFacilityCategory.setDeletedOn(new Date());
            mMedicalFacilityCategory.setIsDelete(true);
        } else {
            mMedicalFacilityCategory.setDeletedBy(null);
            mMedicalFacilityCategory.setDeletedOn(null);
            mMedicalFacilityCategory.setIsDelete(false);
        }

        return this.mMedicalFacilityCategoryRepository.save(mMedicalFacilityCategory);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mMedicalFacilityCategoryCache", allEntries = true)
    public MMedicalFacilityCategory createMMedicalFacilityCategory(MMedicalFacilityCategory mMedicalFacilityCategory) {
        Optional<MMedicalFacilityCategory> optionalMMedicalFacilityCategory = this.mMedicalFacilityCategoryRepository.findById(mMedicalFacilityCategory.getId());
        if (optionalMMedicalFacilityCategory.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + mMedicalFacilityCategory.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        mMedicalFacilityCategory.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        mMedicalFacilityCategory.setCreatedOn(new Date());
        mMedicalFacilityCategory.setModifiedBy(null);
        mMedicalFacilityCategory.setModifiedOn(null);
        mMedicalFacilityCategory.setDeletedBy(null);
        mMedicalFacilityCategory.setDeletedOn(null);
        mMedicalFacilityCategory.setIsDelete(false);

        return this.mMedicalFacilityCategoryRepository.save(mMedicalFacilityCategory);
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
