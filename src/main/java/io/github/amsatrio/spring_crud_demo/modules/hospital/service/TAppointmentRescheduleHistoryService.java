package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointmentRescheduleHistory;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TAppointmentRescheduleHistoryService {

    Page<TAppointmentRescheduleHistory> getPageTAppointmentRescheduleHistory(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TAppointmentRescheduleHistory getTAppointmentRescheduleHistory(Long id);

    void deleteAllTAppointmentRescheduleHistory(List<TAppointmentRescheduleHistory> tAppointmentRescheduleHistoryList);

    void deleteTAppointmentRescheduleHistory(Long id);

    TAppointmentRescheduleHistory updateTAppointmentRescheduleHistory(TAppointmentRescheduleHistory tAppointmentRescheduleHistory);

    TAppointmentRescheduleHistory createTAppointmentRescheduleHistory(TAppointmentRescheduleHistory tAppointmentRescheduleHistory);

}

