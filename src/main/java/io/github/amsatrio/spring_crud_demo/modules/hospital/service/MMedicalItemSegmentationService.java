package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItemSegmentation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MMedicalItemSegmentationService {

    Page<MMedicalItemSegmentation> getPageMMedicalItemSegmentation(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MMedicalItemSegmentation getMMedicalItemSegmentation(Long id);

    void deleteAllMMedicalItemSegmentation(List<MMedicalItemSegmentation> mMedicalItemSegmentationList);

    void deleteMMedicalItemSegmentation(Long id);

    MMedicalItemSegmentation updateMMedicalItemSegmentation(MMedicalItemSegmentation mMedicalItemSegmentation);

    MMedicalItemSegmentation createMMedicalItemSegmentation(MMedicalItemSegmentation mMedicalItemSegmentation);

}

