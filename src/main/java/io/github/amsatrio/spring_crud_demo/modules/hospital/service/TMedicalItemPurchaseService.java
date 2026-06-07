package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TMedicalItemPurchase;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TMedicalItemPurchaseService {

    Page<TMedicalItemPurchase> getPageTMedicalItemPurchase(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TMedicalItemPurchase getTMedicalItemPurchase(Long id);

    void deleteAllTMedicalItemPurchase(List<TMedicalItemPurchase> tMedicalItemPurchaseList);

    void deleteTMedicalItemPurchase(Long id);

    TMedicalItemPurchase updateTMedicalItemPurchase(TMedicalItemPurchase tMedicalItemPurchase);

    TMedicalItemPurchase createTMedicalItemPurchase(TMedicalItemPurchase tMedicalItemPurchase);

}

