package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointment;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TAppointmentService {

    Page<TAppointment> getPageTAppointment(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TAppointment getTAppointment(Long id);

    void deleteAllTAppointment(List<TAppointment> tAppointmentList);

    void deleteTAppointment(Long id);

    TAppointment updateTAppointment(TAppointment tAppointment);

    TAppointment createTAppointment(TAppointment tAppointment);

}

