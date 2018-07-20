package com.kang.jhipster.web.rest;

import com.kang.jhipster.JhipsterMasterApp;

import com.kang.jhipster.domain.AdminVersionFile;
import com.kang.jhipster.repository.AdminVersionFileRepository;
import com.kang.jhipster.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.kang.jhipster.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AdminVersionFileResource REST controller.
 *
 * @see AdminVersionFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMasterApp.class)
public class AdminVersionFileResourceIntTest {

    private static final String DEFAULT_VERSION_NO = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_NO = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION_INFO = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_INFO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FORCE_UPDATE_STATE = false;
    private static final Boolean UPDATED_FORCE_UPDATE_STATE = true;

    private static final Boolean DEFAULT_VERSION_RELEASE_STATE = false;
    private static final Boolean UPDATED_VERSION_RELEASE_STATE = true;

    private static final String DEFAULT_APP_DOWNLOAD_URL = "AAAAAAAAAA";
    private static final String UPDATED_APP_DOWNLOAD_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_VERSION_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERSION_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AdminVersionFileRepository adminVersionFileRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdminVersionFileMockMvc;

    private AdminVersionFile adminVersionFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdminVersionFileResource adminVersionFileResource = new AdminVersionFileResource(adminVersionFileRepository);
        this.restAdminVersionFileMockMvc = MockMvcBuilders.standaloneSetup(adminVersionFileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminVersionFile createEntity(EntityManager em) {
        AdminVersionFile adminVersionFile = new AdminVersionFile()
            .versionNo(DEFAULT_VERSION_NO)
            .versionInfo(DEFAULT_VERSION_INFO)
            .forceUpdateState(DEFAULT_FORCE_UPDATE_STATE)
            .versionReleaseState(DEFAULT_VERSION_RELEASE_STATE)
            .appDownloadUrl(DEFAULT_APP_DOWNLOAD_URL)
            .versionReleaseDate(DEFAULT_VERSION_RELEASE_DATE);
        return adminVersionFile;
    }

    @Before
    public void initTest() {
        adminVersionFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdminVersionFile() throws Exception {
        int databaseSizeBeforeCreate = adminVersionFileRepository.findAll().size();

        // Create the AdminVersionFile
        restAdminVersionFileMockMvc.perform(post("/api/admin-version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminVersionFile)))
            .andExpect(status().isCreated());

        // Validate the AdminVersionFile in the database
        List<AdminVersionFile> adminVersionFileList = adminVersionFileRepository.findAll();
        assertThat(adminVersionFileList).hasSize(databaseSizeBeforeCreate + 1);
        AdminVersionFile testAdminVersionFile = adminVersionFileList.get(adminVersionFileList.size() - 1);
        assertThat(testAdminVersionFile.getVersionNo()).isEqualTo(DEFAULT_VERSION_NO);
        assertThat(testAdminVersionFile.getVersionInfo()).isEqualTo(DEFAULT_VERSION_INFO);
        assertThat(testAdminVersionFile.isForceUpdateState()).isEqualTo(DEFAULT_FORCE_UPDATE_STATE);
        assertThat(testAdminVersionFile.isVersionReleaseState()).isEqualTo(DEFAULT_VERSION_RELEASE_STATE);
        assertThat(testAdminVersionFile.getAppDownloadUrl()).isEqualTo(DEFAULT_APP_DOWNLOAD_URL);
        assertThat(testAdminVersionFile.getVersionReleaseDate()).isEqualTo(DEFAULT_VERSION_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void createAdminVersionFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adminVersionFileRepository.findAll().size();

        // Create the AdminVersionFile with an existing ID
        adminVersionFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminVersionFileMockMvc.perform(post("/api/admin-version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminVersionFile)))
            .andExpect(status().isBadRequest());

        // Validate the AdminVersionFile in the database
        List<AdminVersionFile> adminVersionFileList = adminVersionFileRepository.findAll();
        assertThat(adminVersionFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAdminVersionFiles() throws Exception {
        // Initialize the database
        adminVersionFileRepository.saveAndFlush(adminVersionFile);

        // Get all the adminVersionFileList
        restAdminVersionFileMockMvc.perform(get("/api/admin-version-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminVersionFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNo").value(hasItem(DEFAULT_VERSION_NO.toString())))
            .andExpect(jsonPath("$.[*].versionInfo").value(hasItem(DEFAULT_VERSION_INFO.toString())))
            .andExpect(jsonPath("$.[*].forceUpdateState").value(hasItem(DEFAULT_FORCE_UPDATE_STATE.booleanValue())))
            .andExpect(jsonPath("$.[*].versionReleaseState").value(hasItem(DEFAULT_VERSION_RELEASE_STATE.booleanValue())))
            .andExpect(jsonPath("$.[*].appDownloadUrl").value(hasItem(DEFAULT_APP_DOWNLOAD_URL.toString())))
            .andExpect(jsonPath("$.[*].versionReleaseDate").value(hasItem(DEFAULT_VERSION_RELEASE_DATE.toString())));
    }
    

    @Test
    @Transactional
    public void getAdminVersionFile() throws Exception {
        // Initialize the database
        adminVersionFileRepository.saveAndFlush(adminVersionFile);

        // Get the adminVersionFile
        restAdminVersionFileMockMvc.perform(get("/api/admin-version-files/{id}", adminVersionFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adminVersionFile.getId().intValue()))
            .andExpect(jsonPath("$.versionNo").value(DEFAULT_VERSION_NO.toString()))
            .andExpect(jsonPath("$.versionInfo").value(DEFAULT_VERSION_INFO.toString()))
            .andExpect(jsonPath("$.forceUpdateState").value(DEFAULT_FORCE_UPDATE_STATE.booleanValue()))
            .andExpect(jsonPath("$.versionReleaseState").value(DEFAULT_VERSION_RELEASE_STATE.booleanValue()))
            .andExpect(jsonPath("$.appDownloadUrl").value(DEFAULT_APP_DOWNLOAD_URL.toString()))
            .andExpect(jsonPath("$.versionReleaseDate").value(DEFAULT_VERSION_RELEASE_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAdminVersionFile() throws Exception {
        // Get the adminVersionFile
        restAdminVersionFileMockMvc.perform(get("/api/admin-version-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdminVersionFile() throws Exception {
        // Initialize the database
        adminVersionFileRepository.saveAndFlush(adminVersionFile);

        int databaseSizeBeforeUpdate = adminVersionFileRepository.findAll().size();

        // Update the adminVersionFile
        AdminVersionFile updatedAdminVersionFile = adminVersionFileRepository.findById(adminVersionFile.getId()).get();
        // Disconnect from session so that the updates on updatedAdminVersionFile are not directly saved in db
        em.detach(updatedAdminVersionFile);
        updatedAdminVersionFile
            .versionNo(UPDATED_VERSION_NO)
            .versionInfo(UPDATED_VERSION_INFO)
            .forceUpdateState(UPDATED_FORCE_UPDATE_STATE)
            .versionReleaseState(UPDATED_VERSION_RELEASE_STATE)
            .appDownloadUrl(UPDATED_APP_DOWNLOAD_URL)
            .versionReleaseDate(UPDATED_VERSION_RELEASE_DATE);

        restAdminVersionFileMockMvc.perform(put("/api/admin-version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdminVersionFile)))
            .andExpect(status().isOk());

        // Validate the AdminVersionFile in the database
        List<AdminVersionFile> adminVersionFileList = adminVersionFileRepository.findAll();
        assertThat(adminVersionFileList).hasSize(databaseSizeBeforeUpdate);
        AdminVersionFile testAdminVersionFile = adminVersionFileList.get(adminVersionFileList.size() - 1);
        assertThat(testAdminVersionFile.getVersionNo()).isEqualTo(UPDATED_VERSION_NO);
        assertThat(testAdminVersionFile.getVersionInfo()).isEqualTo(UPDATED_VERSION_INFO);
        assertThat(testAdminVersionFile.isForceUpdateState()).isEqualTo(UPDATED_FORCE_UPDATE_STATE);
        assertThat(testAdminVersionFile.isVersionReleaseState()).isEqualTo(UPDATED_VERSION_RELEASE_STATE);
        assertThat(testAdminVersionFile.getAppDownloadUrl()).isEqualTo(UPDATED_APP_DOWNLOAD_URL);
        assertThat(testAdminVersionFile.getVersionReleaseDate()).isEqualTo(UPDATED_VERSION_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAdminVersionFile() throws Exception {
        int databaseSizeBeforeUpdate = adminVersionFileRepository.findAll().size();

        // Create the AdminVersionFile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdminVersionFileMockMvc.perform(put("/api/admin-version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminVersionFile)))
            .andExpect(status().isBadRequest());

        // Validate the AdminVersionFile in the database
        List<AdminVersionFile> adminVersionFileList = adminVersionFileRepository.findAll();
        assertThat(adminVersionFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAdminVersionFile() throws Exception {
        // Initialize the database
        adminVersionFileRepository.saveAndFlush(adminVersionFile);

        int databaseSizeBeforeDelete = adminVersionFileRepository.findAll().size();

        // Get the adminVersionFile
        restAdminVersionFileMockMvc.perform(delete("/api/admin-version-files/{id}", adminVersionFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AdminVersionFile> adminVersionFileList = adminVersionFileRepository.findAll();
        assertThat(adminVersionFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminVersionFile.class);
        AdminVersionFile adminVersionFile1 = new AdminVersionFile();
        adminVersionFile1.setId(1L);
        AdminVersionFile adminVersionFile2 = new AdminVersionFile();
        adminVersionFile2.setId(adminVersionFile1.getId());
        assertThat(adminVersionFile1).isEqualTo(adminVersionFile2);
        adminVersionFile2.setId(2L);
        assertThat(adminVersionFile1).isNotEqualTo(adminVersionFile2);
        adminVersionFile1.setId(null);
        assertThat(adminVersionFile1).isNotEqualTo(adminVersionFile2);
    }
}
