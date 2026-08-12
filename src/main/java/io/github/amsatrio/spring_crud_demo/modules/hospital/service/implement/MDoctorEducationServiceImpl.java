package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MDoctorEducation;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MDoctorEducationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MDoctorEducationService;
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
public class MDoctorEducationServiceImpl implements MDoctorEducationService {

    private final MDoctorEducationRepository mDoctorEducationRepository;

    public MDoctorEducationServiceImpl(
            MDoctorEducationRepository mDoctorEducationRepository) {
        this.mDoctorEducationRepository = mDoctorEducationRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mDoctorEducationCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<MDoctorEducation> getPageMDoctorEducation(
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
        Specification<MDoctorEducation> mDoctorEducationSpecification = (root, query, cb) -> cb.conjunction();

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                MDoctorEducation mDoctorEducation = new MDoctorEducation();

                Class<? extends MDoctorEducation> mDoctorEducationClass = mDoctorEducation.getClass();

                Field[] fields = mDoctorEducationClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    mDoctorEducationSpecification = mDoctorEducationSpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageMDoctorEducation > GLOBAL FILTERING > error ", exception);
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
                            log.error("getPageMDoctorEducation > error ", exception);
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
                                    mDoctorEducationSpecification = mDoctorEducationSpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    mDoctorEducationSpecification = mDoctorEducationSpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    mDoctorEducationSpecification = mDoctorEducationSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    mDoctorEducationSpecification = mDoctorEducationSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    mDoctorEducationSpecification = mDoctorEducationSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    mDoctorEducationSpecification = mDoctorEducationSpecification
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
                                    mDoctorEducationSpecification = mDoctorEducationSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    mDoctorEducationSpecification = mDoctorEducationSpecification
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
            log.error("getPageMDoctorEducation > CUSTOM FILTERING > error ", exception);
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

        return this.mDoctorEducationRepository.findAll(mDoctorEducationSpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mDoctorEducationCache", key = "#id")
    public MDoctorEducation getMDoctorEducation(Long id) {
        Optional<MDoctorEducation> optionalMDoctorEducation = this.mDoctorEducationRepository.findById(id);
        if (!optionalMDoctorEducation.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalMDoctorEducation.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "mDoctorEducationCache", allEntries = true)
    public void deleteAllMDoctorEducation(List<MDoctorEducation> mDoctorEducationList) {
        this.mDoctorEducationRepository.deleteAll(mDoctorEducationList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mDoctorEducationCache", allEntries = true)
    public void deleteMDoctorEducation(Long id) {
        this.mDoctorEducationRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mDoctorEducationCache", allEntries = true)
    public MDoctorEducation updateMDoctorEducation(MDoctorEducation mDoctorEducation) {
        Optional<MDoctorEducation> optionalMDoctorEducation = this.mDoctorEducationRepository.findById(mDoctorEducation.getId());
        if (!optionalMDoctorEducation.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + mDoctorEducation.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        mDoctorEducation.setCreatedBy(optionalMDoctorEducation.get().getCreatedBy());
        mDoctorEducation.setCreatedOn(optionalMDoctorEducation.get().getCreatedOn());

        mDoctorEducation.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        mDoctorEducation.setModifiedOn(new Date());

        if(mDoctorEducation.getIsDelete() == null){
            mDoctorEducation.setIsDelete(false);
        }
        if (mDoctorEducation.getIsDelete() == true) {
            mDoctorEducation.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            mDoctorEducation.setDeletedOn(new Date());
            mDoctorEducation.setIsDelete(true);
        } else {
            mDoctorEducation.setDeletedBy(null);
            mDoctorEducation.setDeletedOn(null);
            mDoctorEducation.setIsDelete(false);
        }

        return this.mDoctorEducationRepository.save(mDoctorEducation);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mDoctorEducationCache", allEntries = true)
    public MDoctorEducation createMDoctorEducation(MDoctorEducation mDoctorEducation) {
        Optional<MDoctorEducation> optionalMDoctorEducation = this.mDoctorEducationRepository.findById(mDoctorEducation.getId());
        if (optionalMDoctorEducation.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + mDoctorEducation.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        mDoctorEducation.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        mDoctorEducation.setCreatedOn(new Date());
        mDoctorEducation.setModifiedBy(null);
        mDoctorEducation.setModifiedOn(null);
        mDoctorEducation.setDeletedBy(null);
        mDoctorEducation.setDeletedOn(null);
        mDoctorEducation.setIsDelete(false);

        return this.mDoctorEducationRepository.save(mDoctorEducation);
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
