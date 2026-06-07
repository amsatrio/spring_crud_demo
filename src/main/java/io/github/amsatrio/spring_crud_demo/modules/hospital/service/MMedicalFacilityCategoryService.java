package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacilityCategory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MMedicalFacilityCategoryService {

    Page<MMedicalFacilityCategory> getPageMMedicalFacilityCategory(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MMedicalFacilityCategory getMMedicalFacilityCategory(Long id);

    void deleteAllMMedicalFacilityCategory(List<MMedicalFacilityCategory> mMedicalFacilityCategoryList);

    void deleteMMedicalFacilityCategory(Long id);

    MMedicalFacilityCategory updateMMedicalFacilityCategory(MMedicalFacilityCategory mMedicalFacilityCategory);

    MMedicalFacilityCategory createMMedicalFacilityCategory(MMedicalFacilityCategory mMedicalFacilityCategory);

}

