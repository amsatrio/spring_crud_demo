package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeTreatmentPrice;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TDoctorOfficeTreatmentPriceService {

    Page<TDoctorOfficeTreatmentPrice> getPageTDoctorOfficeTreatmentPrice(
            int page,
            int size,
            List<SortRequest> sorts,
            List<FilterRequest> filters,
            String globalFilters
    );

    TDoctorOfficeTreatmentPrice getTDoctorOfficeTreatmentPrice(Long id);

    void deleteAllTDoctorOfficeTreatmentPrice(List<TDoctorOfficeTreatmentPrice> tDoctorOfficeTreatmentPriceList);

    void deleteTDoctorOfficeTreatmentPrice(Long id);

    TDoctorOfficeTreatmentPrice updateTDoctorOfficeTreatmentPrice(TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPrice);

    TDoctorOfficeTreatmentPrice createTDoctorOfficeTreatmentPrice(TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPrice);

}

