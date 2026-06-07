package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBloodGroup;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MBloodGroupService {

    Page<MBloodGroup> getPageMBloodGroup(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MBloodGroup getMBloodGroup(Long id);

    void deleteAllMBloodGroup(List<MBloodGroup> mBloodGroupList);

    void deleteMBloodGroup(Long id);

    MBloodGroup updateMBloodGroup(MBloodGroup mBloodGroup);

    MBloodGroup createMBloodGroup(MBloodGroup mBloodGroup);

}

