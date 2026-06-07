package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomerRelation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MCustomerRelationService {

    Page<MCustomerRelation> getPageMCustomerRelation(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MCustomerRelation getMCustomerRelation(Long id);

    void deleteAllMCustomerRelation(List<MCustomerRelation> mCustomerRelationList);

    void deleteMCustomerRelation(Long id);

    MCustomerRelation updateMCustomerRelation(MCustomerRelation mCustomerRelation);

    MCustomerRelation createMCustomerRelation(MCustomerRelation mCustomerRelation);

}

