package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TMedicalItemPurchaseDetail;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TMedicalItemPurchaseDetailService {

    Page<TMedicalItemPurchaseDetail> getPageTMedicalItemPurchaseDetail(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TMedicalItemPurchaseDetail getTMedicalItemPurchaseDetail(Long id);

    void deleteAllTMedicalItemPurchaseDetail(List<TMedicalItemPurchaseDetail> tMedicalItemPurchaseDetailList);

    void deleteTMedicalItemPurchaseDetail(Long id);

    TMedicalItemPurchaseDetail updateTMedicalItemPurchaseDetail(TMedicalItemPurchaseDetail tMedicalItemPurchaseDetail);

    TMedicalItemPurchaseDetail createTMedicalItemPurchaseDetail(TMedicalItemPurchaseDetail tMedicalItemPurchaseDetail);

}

