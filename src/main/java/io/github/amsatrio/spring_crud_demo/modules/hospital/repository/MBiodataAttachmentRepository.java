package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodataAttachment;

import java.util.Optional;

public interface MBiodataAttachmentRepository extends JpaRepository<MBiodataAttachment, Long>, JpaSpecificationExecutor<MBiodataAttachment> {
    Page<MBiodataAttachment> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MBiodataAttachment> findByIdAndIsDeleteFalse(Long id);
}
