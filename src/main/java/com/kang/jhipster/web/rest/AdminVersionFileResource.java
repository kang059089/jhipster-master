package com.kang.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kang.jhipster.domain.AdminVersionFile;
import com.kang.jhipster.repository.AdminVersionFileRepository;
import com.kang.jhipster.security.AuthoritiesConstants;
import com.kang.jhipster.web.rest.errors.BadRequestAlertException;
import com.kang.jhipster.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AdminVersionFile.
 */
@RestController
@RequestMapping("/api")
public class AdminVersionFileResource {

    private final Logger log = LoggerFactory.getLogger(AdminVersionFileResource.class);

    private static final String ENTITY_NAME = "adminVersionFile";

    private final AdminVersionFileRepository adminVersionFileRepository;

    public AdminVersionFileResource(AdminVersionFileRepository adminVersionFileRepository) {
        this.adminVersionFileRepository = adminVersionFileRepository;
    }

    /**
     * POST  /admin-version-files : Create a new adminVersionFile.
     *
     * @param adminVersionFile the adminVersionFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adminVersionFile, or with status 400 (Bad Request) if the adminVersionFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/admin-version-files")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AdminVersionFile> createAdminVersionFile(@RequestBody AdminVersionFile adminVersionFile) throws URISyntaxException {
        log.debug("REST request to save AdminVersionFile : {}", adminVersionFile);
        if (adminVersionFile.getId() != null) {
            throw new BadRequestAlertException("A new adminVersionFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdminVersionFile result = adminVersionFileRepository.save(adminVersionFile);
        return ResponseEntity.created(new URI("/api/admin-version-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /admin-version-files : Updates an existing adminVersionFile.
     *
     * @param adminVersionFile the adminVersionFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adminVersionFile,
     * or with status 400 (Bad Request) if the adminVersionFile is not valid,
     * or with status 500 (Internal Server Error) if the adminVersionFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/admin-version-files")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AdminVersionFile> updateAdminVersionFile(@RequestBody AdminVersionFile adminVersionFile) throws URISyntaxException {
        log.debug("REST request to update AdminVersionFile : {}", adminVersionFile);
        if (adminVersionFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AdminVersionFile result = adminVersionFileRepository.save(adminVersionFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adminVersionFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /admin-version-files : get all the adminVersionFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of adminVersionFiles in body
     */
    @GetMapping("/admin-version-files")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<AdminVersionFile> getAllAdminVersionFiles() {
        log.debug("REST request to get all AdminVersionFiles");
        return adminVersionFileRepository.findAll();
    }

    /**
     * GET  /admin-version-files/:id : get the "id" adminVersionFile.
     *
     * @param id the id of the adminVersionFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adminVersionFile, or with status 404 (Not Found)
     */
    @GetMapping("/admin-version-files/{id}")
    @Timed
    public ResponseEntity<AdminVersionFile> getAdminVersionFile(@PathVariable Long id) {
        log.debug("REST request to get AdminVersionFile : {}", id);
        Optional<AdminVersionFile> adminVersionFile = adminVersionFileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(adminVersionFile);
    }

    /**
     * DELETE  /admin-version-files/:id : delete the "id" adminVersionFile.
     *
     * @param id the id of the adminVersionFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/admin-version-files/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAdminVersionFile(@PathVariable Long id) {
        log.debug("REST request to delete AdminVersionFile : {}", id);

        adminVersionFileRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 查询管理员发布的最新版本信息
     * @param versionReleaseState 版本发布状态（0：旧版本，1：新版本）
     * @return 返回最新版本信息
     */
    @GetMapping("/admin-version-files/versionReleaseState/{versionReleaseState}")
    @Timed
    public ResponseEntity<AdminVersionFile> getAdminVersionFileInfo(@PathVariable Long versionReleaseState) {
        log.debug("获取app版本信息请求参数为 : {}", versionReleaseState);
        Optional<AdminVersionFile> adminVersionFile = Optional.of(adminVersionFileRepository.findByVersionReleaseState(versionReleaseState));
        return ResponseUtil.wrapOrNotFound(adminVersionFile);
    }
}
