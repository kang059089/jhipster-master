package com.kang.jhipster.repository;

import com.kang.jhipster.domain.AdminVersionFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AdminVersionFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminVersionFileRepository extends JpaRepository<AdminVersionFile, Long> {

    @Query(value = "select * from admin_version_file where version_release_state = ?1 ;", nativeQuery = true)
    AdminVersionFile findByVersionReleaseState(Long VersionReleaseState);
}
