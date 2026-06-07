package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacility;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MMedicalFacilityService {

    Page<MMedicalFacility> getPageMMedicalFacility(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MMedicalFacility getMMedicalFacility(Long id);

    void deleteAllMMedicalFacility(List<MMedicalFacility> mMedicalFacilityList);

    void deleteMMedicalFacility(Long id);

    MMedicalFacility updateMMedicalFacility(MMedicalFacility mMedicalFacility);

    MMedicalFacility createMMedicalFacility(MMedicalFacility mMedicalFacility);

}

