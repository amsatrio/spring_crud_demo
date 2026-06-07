package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWallet;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCustomerWalletService {

    Page<TCustomerWallet> getPageTCustomerWallet(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCustomerWallet getTCustomerWallet(Long id);

    void deleteAllTCustomerWallet(List<TCustomerWallet> tCustomerWalletList);

    void deleteTCustomerWallet(Long id);

    TCustomerWallet updateTCustomerWallet(TCustomerWallet tCustomerWallet);

    TCustomerWallet createTCustomerWallet(TCustomerWallet tCustomerWallet);

}

