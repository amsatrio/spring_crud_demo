package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacilitySchedule;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MMedicalFacilityScheduleService {

    Page<MMedicalFacilitySchedule> getPageMMedicalFacilitySchedule(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MMedicalFacilitySchedule getMMedicalFacilitySchedule(Long id);

    void deleteAllMMedicalFacilitySchedule(List<MMedicalFacilitySchedule> mMedicalFacilityScheduleList);

    void deleteMMedicalFacilitySchedule(Long id);

    MMedicalFacilitySchedule updateMMedicalFacilitySchedule(MMedicalFacilitySchedule mMedicalFacilitySchedule);

    MMedicalFacilitySchedule createMMedicalFacilitySchedule(MMedicalFacilitySchedule mMedicalFacilitySchedule);

}

