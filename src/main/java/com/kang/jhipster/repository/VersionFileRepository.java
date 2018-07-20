package com.kang.jhipster.repository;

import com.kang.jhipster.domain.VersionFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VersionFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersionFileRepository extends JpaRepository<VersionFile, Long> {

}
