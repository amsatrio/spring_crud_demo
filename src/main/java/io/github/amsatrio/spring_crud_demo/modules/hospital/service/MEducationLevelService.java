package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MEducationLevel;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MEducationLevelService {

    Page<MEducationLevel> getPageMEducationLevel(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MEducationLevel getMEducationLevel(Long id);

    void deleteAllMEducationLevel(List<MEducationLevel> mEducationLevelList);

    void deleteMEducationLevel(Long id);

    MEducationLevel updateMEducationLevel(MEducationLevel mEducationLevel);

    MEducationLevel createMEducationLevel(MEducationLevel mEducationLevel);

}

