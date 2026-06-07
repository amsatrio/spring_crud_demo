package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MLocationLevel;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MLocationLevelService {

    Page<MLocationLevel> getPageMLocationLevel(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MLocationLevel getMLocationLevel(Long id);

    void deleteAllMLocationLevel(List<MLocationLevel> mLocationLevelList);

    void deleteMLocationLevel(Long id);

    MLocationLevel updateMLocationLevel(MLocationLevel mLocationLevel);

    MLocationLevel createMLocationLevel(MLocationLevel mLocationLevel);

}

