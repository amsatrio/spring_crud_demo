package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeSchedule;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TDoctorOfficeScheduleService {

    Page<TDoctorOfficeSchedule> getPageTDoctorOfficeSchedule(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TDoctorOfficeSchedule getTDoctorOfficeSchedule(Long id);

    void deleteAllTDoctorOfficeSchedule(List<TDoctorOfficeSchedule> tDoctorOfficeScheduleList);

    void deleteTDoctorOfficeSchedule(Long id);

    TDoctorOfficeSchedule updateTDoctorOfficeSchedule(TDoctorOfficeSchedule tDoctorOfficeSchedule);

    TDoctorOfficeSchedule createTDoctorOfficeSchedule(TDoctorOfficeSchedule tDoctorOfficeSchedule);

}

