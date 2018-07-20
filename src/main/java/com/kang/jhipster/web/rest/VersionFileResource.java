package com.kang.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kang.jhipster.domain.VersionFile;
import com.kang.jhipster.repository.VersionFileRepository;
import com.kang.jhipster.service.util.XmlUtil;
import com.kang.jhipster.web.rest.errors.BadRequestAlertException;
import com.kang.jhipster.web.rest.util.HeaderUtil;
import com.kang.jhipster.service.util.XmlUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VersionFile.
 */
@RestController
@RequestMapping("/api")
public class VersionFileResource {

    private final Logger log = LoggerFactory.getLogger(VersionFileResource.class);

    private static final String ENTITY_NAME = "versionFile";

    private final VersionFileRepository versionFileRepository;

    public VersionFileResource(VersionFileRepository versionFileRepository) {
        this.versionFileRepository = versionFileRepository;
    }

    /**
     * POST  /version-files : Create a new versionFile.
     *
     * @param versionFile the versionFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new versionFile, or with status 400 (Bad Request) if the versionFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/version-files")
    @Timed
    public ResponseEntity<VersionFile> createVersionFile(@RequestBody VersionFile versionFile) throws URISyntaxException {
        log.debug("REST request to save VersionFile : {}", versionFile);
        if (versionFile.getId() != null) {
            throw new BadRequestAlertException("A new versionFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VersionFile result = versionFileRepository.save(versionFile);
        return ResponseEntity.created(new URI("/api/version-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /version-files : Updates an existing versionFile.
     *
     * @param versionFile the versionFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated versionFile,
     * or with status 400 (Bad Request) if the versionFile is not valid,
     * or with status 500 (Internal Server Error) if the versionFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/version-files")
    @Timed
    public ResponseEntity<VersionFile> updateVersionFile(@RequestBody VersionFile versionFile) throws URISyntaxException {
        log.debug("REST request to update VersionFile : {}", versionFile);
        if (versionFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VersionFile result = versionFileRepository.save(versionFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, versionFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /version-files : get all the versionFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of versionFiles in body
     */
    @GetMapping("/version-files")
    @Timed
    public List<VersionFile> getAllVersionFiles() {
        log.debug("REST request to get all VersionFiles");
        return versionFileRepository.findAll();
    }

    /**
     * GET  /version-files/:id : get the "id" versionFile.
     *
     * @param id the id of the versionFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the versionFile, or with status 404 (Not Found)
     */
    @GetMapping("/version-files/{id}")
    @Timed
    public ResponseEntity<VersionFile> getVersionFile(@PathVariable Long id) {
        log.debug("REST request to get VersionFile : {}", id);
        Optional<VersionFile> versionFile = versionFileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(versionFile);
    }

    /**
     * DELETE  /version-files/:id : delete the "id" versionFile.
     *
     * @param id the id of the versionFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/version-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteVersionFile(@PathVariable Long id) {
        log.debug("REST request to delete VersionFile : {}", id);

        versionFileRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 获取app文件版本信息。读取app文件里的版本信息，组装成VersionFile对象保存并返回
     * @param appName app名称
     * @param appType app类型（android或ios）
     * @return 返回app版本信息
     */
    @GetMapping("/version-files/{appName}/{appType}")
    @Timed
    public ResponseEntity<VersionFile> getVersionInfo(@PathVariable String appName, @PathVariable String appType) {
        log.debug("获取app文件版本信息请求参数为 : {}, {}", appName, appType);
        //获取app版本信息xml文件路径
        String xmlPath = ClassUtils.getDefaultClassLoader().getResource("appDownload/" + appName + "/" + appType + "/appVersion.xml").getPath();
        //将File类型的xml文件转换成对象
        Optional<VersionFile> versionFile = Optional.of((VersionFile)XmlUtil.convertXmlFileToObject(VersionFile.class, xmlPath));
        log.debug("app版本信息xml文件转成VersionFile对象结果为 ：{}", versionFile);
        return ResponseUtil.wrapOrNotFound(versionFile);
    }
}
