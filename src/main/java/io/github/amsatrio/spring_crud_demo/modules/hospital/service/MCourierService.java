package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCourier;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MCourierService {

    Page<MCourier> getPageMCourier(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MCourier getMCourier(Long id);

    void deleteAllMCourier(List<MCourier> mCourierList);

    void deleteMCourier(Long id);

    MCourier updateMCourier(MCourier mCourier);

    MCourier createMCourier(MCourier mCourier);

}

