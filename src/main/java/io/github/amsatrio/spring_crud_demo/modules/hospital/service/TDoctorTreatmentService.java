package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorTreatment;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TDoctorTreatmentService {

    Page<TDoctorTreatment> getPageTDoctorTreatment(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TDoctorTreatment getTDoctorTreatment(Long id);

    void deleteAllTDoctorTreatment(List<TDoctorTreatment> tDoctorTreatmentList);

    void deleteTDoctorTreatment(Long id);

    TDoctorTreatment updateTDoctorTreatment(TDoctorTreatment tDoctorTreatment);

    TDoctorTreatment createTDoctorTreatment(TDoctorTreatment tDoctorTreatment);

}

