package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MLocation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MLocationService {

    Page<MLocation> getPageMLocation(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MLocation getMLocation(Long id);

    void deleteAllMLocation(List<MLocation> mLocationList);

    void deleteMLocation(Long id);

    MLocation updateMLocation(MLocation mLocation);

    MLocation createMLocation(MLocation mLocation);

}

