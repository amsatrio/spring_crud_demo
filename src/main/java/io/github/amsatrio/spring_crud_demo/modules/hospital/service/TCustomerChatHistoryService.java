package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerChatHistory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TCustomerChatHistoryService {

    Page<TCustomerChatHistory> getPageTCustomerChatHistory(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TCustomerChatHistory getTCustomerChatHistory(Long id);

    void deleteAllTCustomerChatHistory(List<TCustomerChatHistory> tCustomerChatHistoryList);

    void deleteTCustomerChatHistory(Long id);

    TCustomerChatHistory updateTCustomerChatHistory(TCustomerChatHistory tCustomerChatHistory);

    TCustomerChatHistory createTCustomerChatHistory(TCustomerChatHistory tCustomerChatHistory);

}

