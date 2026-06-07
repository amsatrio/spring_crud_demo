package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TToken;

import java.util.Optional;

public interface TTokenRepository extends JpaRepository<TToken, Long>, JpaSpecificationExecutor<TToken> {
    Page<TToken> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TToken> findByIdAndIsDeleteFalse(Long id);

    Optional<TToken> findByTokenAndIsDeleteFalse(String token);
}
