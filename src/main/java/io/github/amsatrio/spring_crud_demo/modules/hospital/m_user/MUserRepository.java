package io.github.amsatrio.spring_crud_demo.modules.hospital.m_user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MUserRepository extends JpaRepository<MUser, Long>, JpaSpecificationExecutor<MUser> {
    Page<MUser> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MUser> findByIdAndIsDeleteFalse(Long id);
    Optional<MUser> findByEmailAndIsDeleteFalse(String email);
    Optional<MUser> findByEmail(String email);
}
