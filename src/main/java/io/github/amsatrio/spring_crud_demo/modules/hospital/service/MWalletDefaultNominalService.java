package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MWalletDefaultNominal;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MWalletDefaultNominalService {

    Page<MWalletDefaultNominal> getPageMWalletDefaultNominal(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MWalletDefaultNominal getMWalletDefaultNominal(Long id);

    void deleteAllMWalletDefaultNominal(List<MWalletDefaultNominal> mWalletDefaultNominalList);

    void deleteMWalletDefaultNominal(Long id);

    MWalletDefaultNominal updateMWalletDefaultNominal(MWalletDefaultNominal mWalletDefaultNominal);

    MWalletDefaultNominal createMWalletDefaultNominal(MWalletDefaultNominal mWalletDefaultNominal);

}

