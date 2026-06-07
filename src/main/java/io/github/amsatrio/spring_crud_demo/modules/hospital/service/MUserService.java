package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MUser;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MUserService {

    Page<MUser> getPageMUser(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MUser getMUser(Long id);

    void deleteAllMUser(List<MUser> mUserList);

    void deleteMUser(Long id);

    MUser updateMUser(MUser mUser);

    MUser createMUser(MUser mUser);

}

