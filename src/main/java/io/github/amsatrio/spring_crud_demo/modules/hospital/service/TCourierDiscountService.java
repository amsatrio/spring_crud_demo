package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCourierDiscount;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCourierDiscountService {

    Page<TCourierDiscount> getPageTCourierDiscount(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCourierDiscount getTCourierDiscount(Long id);

    void deleteAllTCourierDiscount(List<TCourierDiscount> tCourierDiscountList);

    void deleteTCourierDiscount(Long id);

    TCourierDiscount updateTCourierDiscount(TCourierDiscount tCourierDiscount);

    TCourierDiscount createTCourierDiscount(TCourierDiscount tCourierDiscount);

}

