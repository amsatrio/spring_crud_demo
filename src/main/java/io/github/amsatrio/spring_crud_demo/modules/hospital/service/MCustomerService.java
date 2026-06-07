package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomer;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MCustomerService {

    Page<MCustomer> getPageMCustomer(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MCustomer getMCustomer(Long id);

    void deleteAllMCustomer(List<MCustomer> mCustomerList);

    void deleteMCustomer(Long id);

    MCustomer updateMCustomer(MCustomer mCustomer);

    MCustomer createMCustomer(MCustomer mCustomer);

}

