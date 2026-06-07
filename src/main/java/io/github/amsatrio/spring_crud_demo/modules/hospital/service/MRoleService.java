package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MRole;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MRoleService {

    Page<MRole> getPageMRole(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MRole getMRole(Long id);

    void deleteAllMRole(List<MRole> mRoleList);

    void deleteMRole(Long id);

    MRole updateMRole(MRole mRole);

    MRole createMRole(MRole mRole);

}

