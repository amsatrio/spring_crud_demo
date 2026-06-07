package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomerRelation;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MCustomerRelationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MCustomerRelationService;
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
public class MCustomerRelationServiceImpl implements MCustomerRelationService {

    private final MCustomerRelationRepository mCustomerRelationRepository;

    public MCustomerRelationServiceImpl(
            MCustomerRelationRepository mCustomerRelationRepository) {
        this.mCustomerRelationRepository = mCustomerRelationRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mCustomerRelationCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<MCustomerRelation> getPageMCustomerRelation(
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
        Specification<MCustomerRelation> mCustomerRelationSpecification = Specification.where(null);

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                MCustomerRelation mCustomerRelation = new MCustomerRelation();

                Class<? extends MCustomerRelation> mCustomerRelationClass = mCustomerRelation.getClass();

                Field[] fields = mCustomerRelationClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    mCustomerRelationSpecification = mCustomerRelationSpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageMCustomerRelation > GLOBAL FILTERING > error ", exception);
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
                            log.error("getPageMCustomerRelation > error ", exception);
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
                                    mCustomerRelationSpecification = mCustomerRelationSpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    mCustomerRelationSpecification = mCustomerRelationSpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    mCustomerRelationSpecification = mCustomerRelationSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    mCustomerRelationSpecification = mCustomerRelationSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    mCustomerRelationSpecification = mCustomerRelationSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    mCustomerRelationSpecification = mCustomerRelationSpecification
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
                                    mCustomerRelationSpecification = mCustomerRelationSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    mCustomerRelationSpecification = mCustomerRelationSpecification
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
            log.error("getPageMCustomerRelation > CUSTOM FILTERING > error ", exception);
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

        return this.mCustomerRelationRepository.findAll(mCustomerRelationSpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mCustomerRelationCache", key = "#id")
    public MCustomerRelation getMCustomerRelation(Long id) {
        Optional<MCustomerRelation> optionalMCustomerRelation = this.mCustomerRelationRepository.findById(id);
        if (!optionalMCustomerRelation.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalMCustomerRelation.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "mCustomerRelationCache", allEntries = true)
    public void deleteAllMCustomerRelation(List<MCustomerRelation> mCustomerRelationList) {
        this.mCustomerRelationRepository.deleteAll(mCustomerRelationList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mCustomerRelationCache", allEntries = true)
    public void deleteMCustomerRelation(Long id) {
        this.mCustomerRelationRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mCustomerRelationCache", allEntries = true)
    public MCustomerRelation updateMCustomerRelation(MCustomerRelation mCustomerRelation) {
        Optional<MCustomerRelation> optionalMCustomerRelation = this.mCustomerRelationRepository.findById(mCustomerRelation.getId());
        if (!optionalMCustomerRelation.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + mCustomerRelation.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        mCustomerRelation.setCreatedBy(optionalMCustomerRelation.get().getCreatedBy());
        mCustomerRelation.setCreatedOn(optionalMCustomerRelation.get().getCreatedOn());

        mCustomerRelation.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        mCustomerRelation.setModifiedOn(new Date());

        if(mCustomerRelation.getIsDelete() == null){
            mCustomerRelation.setIsDelete(false);
        }
        if (mCustomerRelation.getIsDelete() == true) {
            mCustomerRelation.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            mCustomerRelation.setDeletedOn(new Date());
            mCustomerRelation.setIsDelete(true);
        } else {
            mCustomerRelation.setDeletedBy(null);
            mCustomerRelation.setDeletedOn(null);
            mCustomerRelation.setIsDelete(false);
        }

        return this.mCustomerRelationRepository.save(mCustomerRelation);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mCustomerRelationCache", allEntries = true)
    public MCustomerRelation createMCustomerRelation(MCustomerRelation mCustomerRelation) {
        Optional<MCustomerRelation> optionalMCustomerRelation = this.mCustomerRelationRepository.findById(mCustomerRelation.getId());
        if (optionalMCustomerRelation.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + mCustomerRelation.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        mCustomerRelation.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        mCustomerRelation.setCreatedOn(new Date());
        mCustomerRelation.setModifiedBy(null);
        mCustomerRelation.setModifiedOn(null);
        mCustomerRelation.setDeletedBy(null);
        mCustomerRelation.setDeletedOn(null);
        mCustomerRelation.setIsDelete(false);

        return this.mCustomerRelationRepository.save(mCustomerRelation);
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
