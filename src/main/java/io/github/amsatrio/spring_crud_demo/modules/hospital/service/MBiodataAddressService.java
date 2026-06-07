package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodataAddress;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MBiodataAddressService {

    Page<MBiodataAddress> getPageMBiodataAddress(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MBiodataAddress getMBiodataAddress(Long id);

    void deleteAllMBiodataAddress(List<MBiodataAddress> mBiodataAddressList);

    void deleteMBiodataAddress(Long id);

    MBiodataAddress updateMBiodataAddress(MBiodataAddress mBiodataAddress);

    MBiodataAddress createMBiodataAddress(MBiodataAddress mBiodataAddress);

}

