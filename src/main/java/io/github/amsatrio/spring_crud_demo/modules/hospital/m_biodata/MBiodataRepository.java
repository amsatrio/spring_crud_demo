package io.github.amsatrio.spring_crud_demo.modules.hospital.m_biodata;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MBiodataRepository extends JpaRepository<MBiodata, Long>, JpaSpecificationExecutor<MBiodata> {
    Page<MBiodata> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MBiodata> findByIdAndIsDeleteFalse(Long id);
}
