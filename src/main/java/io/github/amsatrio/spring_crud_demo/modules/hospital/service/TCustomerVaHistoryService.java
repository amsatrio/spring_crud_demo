package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerVaHistory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCustomerVaHistoryService {

    Page<TCustomerVaHistory> getPageTCustomerVaHistory(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCustomerVaHistory getTCustomerVaHistory(Long id);

    void deleteAllTCustomerVaHistory(List<TCustomerVaHistory> tCustomerVaHistoryList);

    void deleteTCustomerVaHistory(Long id);

    TCustomerVaHistory updateTCustomerVaHistory(TCustomerVaHistory tCustomerVaHistory);

    TCustomerVaHistory createTCustomerVaHistory(TCustomerVaHistory tCustomerVaHistory);

}

