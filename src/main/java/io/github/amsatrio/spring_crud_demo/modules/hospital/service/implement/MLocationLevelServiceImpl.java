package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MLocationLevel;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MLocationLevelRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MLocationLevelService;
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
public class MLocationLevelServiceImpl implements MLocationLevelService {

    private final MLocationLevelRepository mLocationLevelRepository;

    public MLocationLevelServiceImpl(
            MLocationLevelRepository mLocationLevelRepository) {
        this.mLocationLevelRepository = mLocationLevelRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mLocationLevelCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<MLocationLevel> getPageMLocationLevel(
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
        Specification<MLocationLevel> mLocationLevelSpecification = Specification.where(null);

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                MLocationLevel mLocationLevel = new MLocationLevel();

                Class<? extends MLocationLevel> mLocationLevelClass = mLocationLevel.getClass();

                Field[] fields = mLocationLevelClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    mLocationLevelSpecification = mLocationLevelSpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageMLocationLevel > GLOBAL FILTERING > error ", exception);
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
                            log.error("getPageMLocationLevel > error ", exception);
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
                                    mLocationLevelSpecification = mLocationLevelSpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    mLocationLevelSpecification = mLocationLevelSpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    mLocationLevelSpecification = mLocationLevelSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    mLocationLevelSpecification = mLocationLevelSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    mLocationLevelSpecification = mLocationLevelSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    mLocationLevelSpecification = mLocationLevelSpecification
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
                                    mLocationLevelSpecification = mLocationLevelSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    mLocationLevelSpecification = mLocationLevelSpecification
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
            log.error("getPageMLocationLevel > CUSTOM FILTERING > error ", exception);
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

        return this.mLocationLevelRepository.findAll(mLocationLevelSpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mLocationLevelCache", key = "#id")
    public MLocationLevel getMLocationLevel(Long id) {
        Optional<MLocationLevel> optionalMLocationLevel = this.mLocationLevelRepository.findById(id);
        if (!optionalMLocationLevel.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalMLocationLevel.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "mLocationLevelCache", allEntries = true)
    public void deleteAllMLocationLevel(List<MLocationLevel> mLocationLevelList) {
        this.mLocationLevelRepository.deleteAll(mLocationLevelList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mLocationLevelCache", allEntries = true)
    public void deleteMLocationLevel(Long id) {
        this.mLocationLevelRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mLocationLevelCache", allEntries = true)
    public MLocationLevel updateMLocationLevel(MLocationLevel mLocationLevel) {
        Optional<MLocationLevel> optionalMLocationLevel = this.mLocationLevelRepository.findById(mLocationLevel.getId());
        if (!optionalMLocationLevel.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + mLocationLevel.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        mLocationLevel.setCreatedBy(optionalMLocationLevel.get().getCreatedBy());
        mLocationLevel.setCreatedOn(optionalMLocationLevel.get().getCreatedOn());

        mLocationLevel.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        mLocationLevel.setModifiedOn(new Date());

        if(mLocationLevel.getIsDelete() == null){
            mLocationLevel.setIsDelete(false);
        }
        if (mLocationLevel.getIsDelete() == true) {
            mLocationLevel.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            mLocationLevel.setDeletedOn(new Date());
            mLocationLevel.setIsDelete(true);
        } else {
            mLocationLevel.setDeletedBy(null);
            mLocationLevel.setDeletedOn(null);
            mLocationLevel.setIsDelete(false);
        }

        return this.mLocationLevelRepository.save(mLocationLevel);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mLocationLevelCache", allEntries = true)
    public MLocationLevel createMLocationLevel(MLocationLevel mLocationLevel) {
        Optional<MLocationLevel> optionalMLocationLevel = this.mLocationLevelRepository.findById(mLocationLevel.getId());
        if (optionalMLocationLevel.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + mLocationLevel.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        mLocationLevel.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        mLocationLevel.setCreatedOn(new Date());
        mLocationLevel.setModifiedBy(null);
        mLocationLevel.setModifiedOn(null);
        mLocationLevel.setDeletedBy(null);
        mLocationLevel.setDeletedOn(null);
        mLocationLevel.setIsDelete(false);

        return this.mLocationLevelRepository.save(mLocationLevel);
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
