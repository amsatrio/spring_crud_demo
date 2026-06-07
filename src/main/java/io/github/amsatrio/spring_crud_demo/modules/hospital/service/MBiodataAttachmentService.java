package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodataAttachment;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MBiodataAttachmentService {

    Page<MBiodataAttachment> getPageMBiodataAttachment(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MBiodataAttachment getMBiodataAttachment(Long id);

    void deleteAllMBiodataAttachment(List<MBiodataAttachment> mBiodataAttachmentList);

    void deleteMBiodataAttachment(Long id);

    MBiodataAttachment updateMBiodataAttachment(MBiodataAttachment mBiodataAttachment);

    MBiodataAttachment createMBiodataAttachment(MBiodataAttachment mBiodataAttachment);

}

