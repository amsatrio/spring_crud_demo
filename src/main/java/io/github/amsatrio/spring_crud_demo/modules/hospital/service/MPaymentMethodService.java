package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MPaymentMethod;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MPaymentMethodService {

    Page<MPaymentMethod> getPageMPaymentMethod(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MPaymentMethod getMPaymentMethod(Long id);

    void deleteAllMPaymentMethod(List<MPaymentMethod> mPaymentMethodList);

    void deleteMPaymentMethod(Long id);

    MPaymentMethod updateMPaymentMethod(MPaymentMethod mPaymentMethod);

    MPaymentMethod createMPaymentMethod(MPaymentMethod mPaymentMethod);

}

