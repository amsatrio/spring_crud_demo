package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MDoctor;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MDoctorService {

    Page<MDoctor> getPageMDoctor(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MDoctor getMDoctor(Long id);

    void deleteAllMDoctor(List<MDoctor> mDoctorList);

    void deleteMDoctor(Long id);

    MDoctor updateMDoctor(MDoctor mDoctor);

    MDoctor createMDoctor(MDoctor mDoctor);

}

