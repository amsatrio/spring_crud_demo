package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomer;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MCustomerRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MCustomerService;
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
public class MCustomerServiceImpl implements MCustomerService {

    private final MCustomerRepository mCustomerRepository;

    public MCustomerServiceImpl(
            MCustomerRepository mCustomerRepository) {
        this.mCustomerRepository = mCustomerRepository;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mCustomerCache", key = "{#page, #size, #sorts, #filters, #globalFilters}")
    public Page<MCustomer> getPageMCustomer(
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
        Specification<MCustomer> mCustomerSpecification = (root, query, cb) -> cb.conjunction();

        // === GLOBAL FILTERING
        try {
            if (!globalFilters.isEmpty()) {
                MCustomer mCustomer = new MCustomer();

                Class<? extends MCustomer> mCustomerClass = mCustomer.getClass();

                Field[] fields = mCustomerClass.getDeclaredFields();
                for (Field field : fields) {
                    // exclude item from filter
                    if (field.getName().contains("id")) {
                        continue;
                    } else if (field.getType() != String.class) {
                        continue;
                    }

                    mCustomerSpecification = mCustomerSpecification
                            .or((root, query, builder) -> builder.like(root.get(field.getName()),
                                    "%" + globalFilters + "%"));
                }

            }
        } catch (Exception exception) {
            log.error("getPageMCustomer > GLOBAL FILTERING > error ", exception);
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
                            log.error("getPageMCustomer > error ", exception);
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
                                    mCustomerSpecification = mCustomerSpecification.and((root, query, builder) -> {
                                        return builder.between(
                                                root.get(filterRequest.getId()).as(Date.class),
                                                date1, date2);
                                    });
                                    break;
                                default: // TEXT
                                    mCustomerSpecification = mCustomerSpecification
                                            .and((root, query, builder) -> builder.between(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1.toString(), finaObject2.toString()));
                                    break;
                            }
                            break;
                        case EQUALS:
                            switch (filterRequest.getDataType()) {
                                case NUMBER:
                                    mCustomerSpecification = mCustomerSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    mCustomerSpecification = mCustomerSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Date.class), finaObject1));
                                    break;
                                case BOOLEAN:
                                    mCustomerSpecification = mCustomerSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()).as(Boolean.class),
                                                    Boolean.valueOf(finaObject1.toString())));
                                    break;
                                default: // TEXT
                                    mCustomerSpecification = mCustomerSpecification
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
                                    mCustomerSpecification = mCustomerSpecification
                                            .and((root, query, builder) -> builder.equal(
                                                    root.get(filterRequest.getId()),
                                                    finaObject1));
                                    break;
                                case DATE:
                                    break;
                                default: // TEXT
                                    mCustomerSpecification = mCustomerSpecification
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
            log.error("getPageMCustomer > CUSTOM FILTERING > error ", exception);
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

        return this.mCustomerRepository.findAll(mCustomerSpecification, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "mCustomerCache", key = "#id")
    public MCustomer getMCustomer(Long id) {
        Optional<MCustomer> optionalMCustomer = this.mCustomerRepository.findById(id);
        if (!optionalMCustomer.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + id, new HttpHeaders(),
                    null,
                    null);
        }
        return optionalMCustomer.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "mCustomerCache", allEntries = true)
    public void deleteAllMCustomer(List<MCustomer> mCustomerList) {
        this.mCustomerRepository.deleteAll(mCustomerList);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mCustomerCache", allEntries = true)
    public void deleteMCustomer(Long id) {
        this.mCustomerRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mCustomerCache", allEntries = true)
    public MCustomer updateMCustomer(MCustomer mCustomer) {
        Optional<MCustomer> optionalMCustomer = this.mCustomerRepository.findById(mCustomer.getId());
        if (!optionalMCustomer.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no data found for id " + mCustomer.getId(),
                    new HttpHeaders(), null,
                    null);
        }

        mCustomer.setCreatedBy(optionalMCustomer.get().getCreatedBy());
        mCustomer.setCreatedOn(optionalMCustomer.get().getCreatedOn());

        mCustomer.setModifiedBy(getUserDetailsIdFromAuthenticationSecurity());
        mCustomer.setModifiedOn(new Date());

        if(mCustomer.getIsDelete() == null){
            mCustomer.setIsDelete(false);
        }
        if (mCustomer.getIsDelete() == true) {
            mCustomer.setDeletedBy(getUserDetailsIdFromAuthenticationSecurity());
            mCustomer.setDeletedOn(new Date());
            mCustomer.setIsDelete(true);
        } else {
            mCustomer.setDeletedBy(null);
            mCustomer.setDeletedOn(null);
            mCustomer.setIsDelete(false);
        }

        return this.mCustomerRepository.save(mCustomer);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mCustomerCache", allEntries = true)
    public MCustomer createMCustomer(MCustomer mCustomer) {
        Optional<MCustomer> optionalMCustomer = this.mCustomerRepository.findById(mCustomer.getId());
        if (optionalMCustomer.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "data with id " + mCustomer.getId() + " exists",
                    new HttpHeaders(), null, null);
        }

        mCustomer.setCreatedBy(getUserDetailsIdFromAuthenticationSecurity());
        mCustomer.setCreatedOn(new Date());
        mCustomer.setModifiedBy(null);
        mCustomer.setModifiedOn(null);
        mCustomer.setDeletedBy(null);
        mCustomer.setDeletedOn(null);
        mCustomer.setIsDelete(false);

        return this.mCustomerRepository.save(mCustomer);
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
