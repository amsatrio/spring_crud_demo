package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MLocation;

import java.util.Optional;

public interface MLocationRepository extends JpaRepository<MLocation, Long>, JpaSpecificationExecutor<MLocation> {
    Page<MLocation> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MLocation> findByIdAndIsDeleteFalse(Long id);

    @Query("select m.id from MLocation m where m.locationLevelId = ?1 and m.id = ?2 order by m.id")
    Optional<Long> findIdByLocationLevelIdAndIdOrderByIdAsc(Long locationLevelId, Long id);
}
