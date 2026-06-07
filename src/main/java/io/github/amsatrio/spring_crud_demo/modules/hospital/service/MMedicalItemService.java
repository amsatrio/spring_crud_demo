package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItem;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MMedicalItemService {

    Page<MMedicalItem> getPageMMedicalItem(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MMedicalItem getMMedicalItem(Long id);

    void deleteAllMMedicalItem(List<MMedicalItem> mMedicalItemList);

    void deleteMMedicalItem(Long id);

    MMedicalItem updateMMedicalItem(MMedicalItem mMedicalItem);

    MMedicalItem createMMedicalItem(MMedicalItem mMedicalItem);

}

