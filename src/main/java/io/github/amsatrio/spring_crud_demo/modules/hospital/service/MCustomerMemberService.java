package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomerMember;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MCustomerMemberService {

    Page<MCustomerMember> getPageMCustomerMember(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    MCustomerMember getMCustomerMember(Long id);

    void deleteAllMCustomerMember(List<MCustomerMember> mCustomerMemberList);

    void deleteMCustomerMember(Long id);

    MCustomerMember updateMCustomerMember(MCustomerMember mCustomerMember);

    MCustomerMember createMCustomerMember(MCustomerMember mCustomerMember);

}

