package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodata;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MBiodataService {

    Page<MBiodata> getPageMBiodata(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MBiodata getMBiodata(Long id);

    void deleteAllMBiodata(List<MBiodata> mBiodataList);

    void deleteMBiodata(Long id);

    MBiodata updateMBiodata(MBiodata mBiodata);

    MBiodata createMBiodata(MBiodata mBiodata);

}

