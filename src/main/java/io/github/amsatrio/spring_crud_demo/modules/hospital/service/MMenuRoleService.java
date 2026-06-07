package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMenuRole;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MMenuRoleService {

    Page<MMenuRole> getPageMMenuRole(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MMenuRole getMMenuRole(Long id);

    void deleteAllMMenuRole(List<MMenuRole> mMenuRoleList);

    void deleteMMenuRole(Long id);

    MMenuRole updateMMenuRole(MMenuRole mMenuRole);

    MMenuRole createMMenuRole(MMenuRole mMenuRole);

}

