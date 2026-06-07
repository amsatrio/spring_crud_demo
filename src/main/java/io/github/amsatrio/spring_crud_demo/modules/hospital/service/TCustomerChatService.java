package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerChat;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCustomerChatService {

    Page<TCustomerChat> getPageTCustomerChat(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCustomerChat getTCustomerChat(Long id);

    void deleteAllTCustomerChat(List<TCustomerChat> tCustomerChatList);

    void deleteTCustomerChat(Long id);

    TCustomerChat updateTCustomerChat(TCustomerChat tCustomerChat);

    TCustomerChat createTCustomerChat(TCustomerChat tCustomerChat);

}

