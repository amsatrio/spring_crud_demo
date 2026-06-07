package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBank;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MBankService {

    Page<MBank> getPageMBank(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MBank getMBank(Long id);

    void deleteAllMBank(List<MBank> mBankList);

    void deleteMBank(Long id);

    MBank updateMBank(MBank mBank);

    MBank createMBank(MBank mBank);

}

