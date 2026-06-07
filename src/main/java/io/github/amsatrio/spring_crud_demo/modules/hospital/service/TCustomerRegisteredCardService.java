package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerRegisteredCard;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCustomerRegisteredCardService {

    Page<TCustomerRegisteredCard> getPageTCustomerRegisteredCard(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCustomerRegisteredCard getTCustomerRegisteredCard(Long id);

    void deleteAllTCustomerRegisteredCard(List<TCustomerRegisteredCard> tCustomerRegisteredCardList);

    void deleteTCustomerRegisteredCard(Long id);

    TCustomerRegisteredCard updateTCustomerRegisteredCard(TCustomerRegisteredCard tCustomerRegisteredCard);

    TCustomerRegisteredCard createTCustomerRegisteredCard(TCustomerRegisteredCard tCustomerRegisteredCard);

}

