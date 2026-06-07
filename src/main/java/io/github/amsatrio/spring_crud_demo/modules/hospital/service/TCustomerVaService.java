package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerVa;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCustomerVaService {

    Page<TCustomerVa> getPageTCustomerVa(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCustomerVa getTCustomerVa(Long id);

    void deleteAllTCustomerVa(List<TCustomerVa> tCustomerVaList);

    void deleteTCustomerVa(Long id);

    TCustomerVa updateTCustomerVa(TCustomerVa tCustomerVa);

    TCustomerVa createTCustomerVa(TCustomerVa tCustomerVa);

}

