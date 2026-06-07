package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MDoctorEducation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MDoctorEducationService {

    Page<MDoctorEducation> getPageMDoctorEducation(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MDoctorEducation getMDoctorEducation(Long id);

    void deleteAllMDoctorEducation(List<MDoctorEducation> mDoctorEducationList);

    void deleteMDoctorEducation(Long id);

    MDoctorEducation updateMDoctorEducation(MDoctorEducation mDoctorEducation);

    MDoctorEducation createMDoctorEducation(MDoctorEducation mDoctorEducation);

}

