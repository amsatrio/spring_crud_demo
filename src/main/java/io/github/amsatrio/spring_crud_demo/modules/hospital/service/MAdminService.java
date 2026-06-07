package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MAdmin;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MAdminService {

    Page<MAdmin> getPageMAdmin(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MAdmin getMAdmin(Long id);

    void deleteAllMAdmin(List<MAdmin> mAdminList);

    void deleteMAdmin(Long id);

    MAdmin updateMAdmin(MAdmin mAdmin);

    MAdmin createMAdmin(MAdmin mAdmin);

}

