package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeTreatment;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TDoctorOfficeTreatmentService {

    Page<TDoctorOfficeTreatment> getPageTDoctorOfficeTreatment(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TDoctorOfficeTreatment getTDoctorOfficeTreatment(Long id);

    void deleteAllTDoctorOfficeTreatment(List<TDoctorOfficeTreatment> tDoctorOfficeTreatmentList);

    void deleteTDoctorOfficeTreatment(Long id);

    TDoctorOfficeTreatment updateTDoctorOfficeTreatment(TDoctorOfficeTreatment tDoctorOfficeTreatment);

    TDoctorOfficeTreatment createTDoctorOfficeTreatment(TDoctorOfficeTreatment tDoctorOfficeTreatment);

}

