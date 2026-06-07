package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWalletWithdraw;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCustomerWalletWithdrawService {

    Page<TCustomerWalletWithdraw> getPageTCustomerWalletWithdraw(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCustomerWalletWithdraw getTCustomerWalletWithdraw(Long id);

    void deleteAllTCustomerWalletWithdraw(List<TCustomerWalletWithdraw> tCustomerWalletWithdrawList);

    void deleteTCustomerWalletWithdraw(Long id);

    TCustomerWalletWithdraw updateTCustomerWalletWithdraw(TCustomerWalletWithdraw tCustomerWalletWithdraw);

    TCustomerWalletWithdraw createTCustomerWalletWithdraw(TCustomerWalletWithdraw tCustomerWalletWithdraw);

}

