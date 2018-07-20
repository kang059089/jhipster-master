package com.kang.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kang.jhipster.domain.UserVersionFile;
import com.kang.jhipster.repository.UserVersionFileRepository;
import com.kang.jhipster.web.rest.errors.BadRequestAlertException;
import com.kang.jhipster.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserVersionFile.
 */
@RestController
@RequestMapping("/api")
public class UserVersionFileResource {

    private final Logger log = LoggerFactory.getLogger(UserVersionFileResource.class);

    private static final String ENTITY_NAME = "userVersionFile";

    private final UserVersionFileRepository userVersionFileRepository;

    public UserVersionFileResource(UserVersionFileRepository userVersionFileRepository) {
        this.userVersionFileRepository = userVersionFileRepository;
    }

    /**
     * POST  /user-version-files : Create a new userVersionFile.
     *
     * @param userVersionFile the userVersionFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userVersionFile, or with status 400 (Bad Request) if the userVersionFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-version-files")
    @Timed
    public ResponseEntity<UserVersionFile> createUserVersionFile(@RequestBody UserVersionFile userVersionFile) throws URISyntaxException {
        log.debug("REST request to save UserVersionFile : {}", userVersionFile);
        if (userVersionFile.getId() != null) {
            throw new BadRequestAlertException("A new userVersionFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserVersionFile result = userVersionFileRepository.save(userVersionFile);
        return ResponseEntity.created(new URI("/api/user-version-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-version-files : Updates an existing userVersionFile.
     *
     * @param userVersionFile the userVersionFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userVersionFile,
     * or with status 400 (Bad Request) if the userVersionFile is not valid,
     * or with status 500 (Internal Server Error) if the userVersionFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-version-files")
    @Timed
    public ResponseEntity<UserVersionFile> updateUserVersionFile(@RequestBody UserVersionFile userVersionFile) throws URISyntaxException {
        log.debug("REST request to update UserVersionFile : {}", userVersionFile);
        if (userVersionFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserVersionFile result = userVersionFileRepository.save(userVersionFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userVersionFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-version-files : get all the userVersionFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userVersionFiles in body
     */
    @GetMapping("/user-version-files")
    @Timed
    public List<UserVersionFile> getAllUserVersionFiles() {
        log.debug("REST request to get all UserVersionFiles");
        return userVersionFileRepository.findAll();
    }

    /**
     * GET  /user-version-files/:id : get the "id" userVersionFile.
     *
     * @param id the id of the userVersionFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userVersionFile, or with status 404 (Not Found)
     */
    @GetMapping("/user-version-files/{id}")
    @Timed
    public ResponseEntity<UserVersionFile> getUserVersionFile(@PathVariable Long id) {
        log.debug("REST request to get UserVersionFile : {}", id);
        Optional<UserVersionFile> userVersionFile = userVersionFileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userVersionFile);
    }

    /**
     * DELETE  /user-version-files/:id : delete the "id" userVersionFile.
     *
     * @param id the id of the userVersionFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-version-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserVersionFile(@PathVariable Long id) {
        log.debug("REST request to delete UserVersionFile : {}", id);

        userVersionFileRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
