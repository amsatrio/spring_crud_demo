package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCurrentDoctorSpecialization;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCurrentDoctorSpecializationService {

    Page<TCurrentDoctorSpecialization> getPageTCurrentDoctorSpecialization(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCurrentDoctorSpecialization getTCurrentDoctorSpecialization(Long id);

    void deleteAllTCurrentDoctorSpecialization(List<TCurrentDoctorSpecialization> tCurrentDoctorSpecializationList);

    void deleteTCurrentDoctorSpecialization(Long id);

    TCurrentDoctorSpecialization updateTCurrentDoctorSpecialization(TCurrentDoctorSpecialization tCurrentDoctorSpecialization);

    TCurrentDoctorSpecialization createTCurrentDoctorSpecialization(TCurrentDoctorSpecialization tCurrentDoctorSpecialization);

}

