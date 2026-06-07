package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TToken;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TTokenService {

    Page<TToken> getPageTToken(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TToken getTToken(Long id);

    void deleteAllTToken(List<TToken> tTokenList);

    void deleteTToken(Long id);

    TToken updateTToken(TToken tToken);

    TToken createTToken(TToken tToken);

}

