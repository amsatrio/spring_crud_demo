package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TResetPassword;

import java.util.Optional;

public interface TResetPasswordRepository extends JpaRepository<TResetPassword, Long>, JpaSpecificationExecutor<TResetPassword> {
    Page<TResetPassword> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TResetPassword> findByIdAndIsDeleteFalse(Long id);
}
