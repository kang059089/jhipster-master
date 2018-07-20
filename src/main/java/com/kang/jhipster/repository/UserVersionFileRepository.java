package com.kang.jhipster.repository;

import com.kang.jhipster.domain.UserVersionFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserVersionFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserVersionFileRepository extends JpaRepository<UserVersionFile, Long> {

}
