package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TResetPassword;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TResetPasswordService {

    Page<TResetPassword> getPageTResetPassword(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TResetPassword getTResetPassword(Long id);

    void deleteAllTResetPassword(List<TResetPassword> tResetPasswordList);

    void deleteTResetPassword(Long id);

    TResetPassword updateTResetPassword(TResetPassword tResetPassword);

    TResetPassword createTResetPassword(TResetPassword tResetPassword);

}

