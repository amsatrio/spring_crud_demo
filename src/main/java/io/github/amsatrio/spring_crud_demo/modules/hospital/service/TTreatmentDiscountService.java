package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TTreatmentDiscount;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TTreatmentDiscountService {

    Page<TTreatmentDiscount> getPageTTreatmentDiscount(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TTreatmentDiscount getTTreatmentDiscount(Long id);

    void deleteAllTTreatmentDiscount(List<TTreatmentDiscount> tTreatmentDiscountList);

    void deleteTTreatmentDiscount(Long id);

    TTreatmentDiscount updateTTreatmentDiscount(TTreatmentDiscount tTreatmentDiscount);

    TTreatmentDiscount createTTreatmentDiscount(TTreatmentDiscount tTreatmentDiscount);

}

