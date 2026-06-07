package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointmentCancellation;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TAppointmentCancellationService {

    Page<TAppointmentCancellation> getPageTAppointmentCancellation(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TAppointmentCancellation getTAppointmentCancellation(Long id);

    void deleteAllTAppointmentCancellation(List<TAppointmentCancellation> tAppointmentCancellationList);

    void deleteTAppointmentCancellation(Long id);

    TAppointmentCancellation updateTAppointmentCancellation(TAppointmentCancellation tAppointmentCancellation);

    TAppointmentCancellation createTAppointmentCancellation(TAppointmentCancellation tAppointmentCancellation);

}

