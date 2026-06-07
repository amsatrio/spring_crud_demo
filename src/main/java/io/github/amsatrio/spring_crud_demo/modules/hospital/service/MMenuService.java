package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMenu;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MMenuService {

    Page<MMenu> getPageMMenu(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MMenu getMMenu(Long id);

    void deleteAllMMenu(List<MMenu> mMenuList);

    void deleteMMenu(Long id);

    MMenu updateMMenu(MMenu mMenu);

    MMenu createMMenu(MMenu mMenu);

}

