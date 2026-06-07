package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MEducationLevel;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MEducationLevelRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MEducationLevelService;
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
public class MEducationLevelServiceImpl implements MEducationLevelService {

    private final MEducationLevelRepository mEducationLevelRepository;

    public MEducationLevelServiceImpl(
            MEducationLevelRepository mEducationLevelRepository) {
        this.mEducationLevelRepository = mEducationLevelRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mEducationLevelCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<MEducationLevel> getPageMEducationLevel(
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
        Specification<MEducationLevel> mEducationLevelSpecification = Specification.where(null);

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                MEducationLevel mEducationLevel = new MEducationLevel();

                Class<? extends MEducationLevel> mEducationLevelClass = mEducationLevel.getClass();

                Field[] fields = mEducationLevelClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    mEducationLevelSpecification = mEducationLevelSpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageMEducationLevel > GLOBAL FILTERING > error ", exception);
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
                            log.error("getPageMEducationLevel > error ", exception);
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
                                    mEducationLevelSpecification = mEducationLevelSpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    mEducationLevelSpecification = mEducationLevelSpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    mEducationLevelSpecification = mEducationLevelSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    mEducationLevelSpecification = mEducationLevelSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    mEducationLevelSpecification = mEducationLevelSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    mEducationLevelSpecification = mEducationLevelSpecification
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
                                    mEducationLevelSpecification = mEducationLevelSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    mEducationLevelSpecification = mEducationLevelSpecification
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
            log.error("getPageMEducationLevel > CUSTOM FILTERING > error ", exception);
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

        return this.mEducationLevelRepository.findAll(mEducationLevelSpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mEducationLevelCache", key = "#id")
    public MEducationLevel getMEducationLevel(Long id) {
        Optional<MEducationLevel> optionalMEducationLevel = this.mEducationLevelRepository.findById(id);
        if (!optionalMEducationLevel.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalMEducationLevel.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "mEducationLevelCache", allEntries = true)
    public void deleteAllMEducationLevel(List<MEducationLevel> mEducationLevelList) {
        this.mEducationLevelRepository.deleteAll(mEducationLevelList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mEducationLevelCache", allEntries = true)
    public void deleteMEducationLevel(Long id) {
        this.mEducationLevelRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mEducationLevelCache", allEntries = true)
    public MEducationLevel updateMEducationLevel(MEducationLevel mEducationLevel) {
        Optional<MEducationLevel> optionalMEducationLevel = this.mEducationLevelRepository.findById(mEducationLevel.getId());
        if (!optionalMEducationLevel.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + mEducationLevel.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        mEducationLevel.setCreatedBy(optionalMEducationLevel.get().getCreatedBy());
        mEducationLevel.setCreatedOn(optionalMEducationLevel.get().getCreatedOn());

        mEducationLevel.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        mEducationLevel.setModifiedOn(new Date());

        if(mEducationLevel.getIsDelete() == null){
            mEducationLevel.setIsDelete(false);
        }
        if (mEducationLevel.getIsDelete() == true) {
            mEducationLevel.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            mEducationLevel.setDeletedOn(new Date());
            mEducationLevel.setIsDelete(true);
        } else {
            mEducationLevel.setDeletedBy(null);
            mEducationLevel.setDeletedOn(null);
            mEducationLevel.setIsDelete(false);
        }

        return this.mEducationLevelRepository.save(mEducationLevel);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mEducationLevelCache", allEntries = true)
    public MEducationLevel createMEducationLevel(MEducationLevel mEducationLevel) {
        Optional<MEducationLevel> optionalMEducationLevel = this.mEducationLevelRepository.findById(mEducationLevel.getId());
        if (optionalMEducationLevel.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + mEducationLevel.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        mEducationLevel.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        mEducationLevel.setCreatedOn(new Date());
        mEducationLevel.setModifiedBy(null);
        mEducationLevel.setModifiedOn(null);
        mEducationLevel.setDeletedBy(null);
        mEducationLevel.setDeletedOn(null);
        mEducationLevel.setIsDelete(false);

        return this.mEducationLevelRepository.save(mEducationLevel);
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
