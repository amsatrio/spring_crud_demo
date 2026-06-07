package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItemCategory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MMedicalItemCategoryService {

    Page<MMedicalItemCategory> getPageMMedicalItemCategory(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MMedicalItemCategory getMMedicalItemCategory(Long id);

    void deleteAllMMedicalItemCategory(List<MMedicalItemCategory> mMedicalItemCategoryList);

    void deleteMMedicalItemCategory(Long id);

    MMedicalItemCategory updateMMedicalItemCategory(MMedicalItemCategory mMedicalItemCategory);

    MMedicalItemCategory createMMedicalItemCategory(MMedicalItemCategory mMedicalItemCategory);

}

