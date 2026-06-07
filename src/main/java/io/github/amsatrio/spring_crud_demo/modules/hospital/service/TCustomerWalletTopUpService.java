package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWalletTopUp;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCustomerWalletTopUpService {

    Page<TCustomerWalletTopUp> getPageTCustomerWalletTopUp(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCustomerWalletTopUp getTCustomerWalletTopUp(Long id);

    void deleteAllTCustomerWalletTopUp(List<TCustomerWalletTopUp> tCustomerWalletTopUpList);

    void deleteTCustomerWalletTopUp(Long id);

    TCustomerWalletTopUp updateTCustomerWalletTopUp(TCustomerWalletTopUp tCustomerWalletTopUp);

    TCustomerWalletTopUp createTCustomerWalletTopUp(TCustomerWalletTopUp tCustomerWalletTopUp);

}

