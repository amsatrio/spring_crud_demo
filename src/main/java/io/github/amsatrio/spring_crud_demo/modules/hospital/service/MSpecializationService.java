package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MSpecialization;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MSpecializationService {

    Page<MSpecialization> getPageMSpecialization(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MSpecialization getMSpecialization(Long id);

    void deleteAllMSpecialization(List<MSpecialization> mSpecializationList);

    void deleteMSpecialization(Long id);

    MSpecialization updateMSpecialization(MSpecialization mSpecialization);

    MSpecialization createMSpecialization(MSpecialization mSpecialization);

}

