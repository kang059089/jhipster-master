package com.kang.jhipster.web.rest;

import com.kang.jhipster.JhipsterMasterApp;

import com.kang.jhipster.domain.VersionFile;
import com.kang.jhipster.repository.VersionFileRepository;
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
import java.util.List;


import static com.kang.jhipster.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VersionFileResource REST controller.
 *
 * @see VersionFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMasterApp.class)
public class VersionFileResourceIntTest {

    private static final String DEFAULT_VERSION_NO = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_NO = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION_INFO = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_LATEST_VERSION_NO = "AAAAAAAAAA";
    private static final String UPDATED_LATEST_VERSION_NO = "BBBBBBBBBB";

    private static final String DEFAULT_LATEST_VERSION_INFO = "AAAAAAAAAA";
    private static final String UPDATED_LATEST_VERSION_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_LATEST_UPDATE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_LATEST_UPDATE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_APP_DOWNLOAD_URL = "AAAAAAAAAA";
    private static final String UPDATED_APP_DOWNLOAD_URL = "BBBBBBBBBB";

    @Autowired
    private VersionFileRepository versionFileRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVersionFileMockMvc;

    private VersionFile versionFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VersionFileResource versionFileResource = new VersionFileResource(versionFileRepository);
        this.restVersionFileMockMvc = MockMvcBuilders.standaloneSetup(versionFileResource)
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
    public static VersionFile createEntity(EntityManager em) {
        VersionFile versionFile = new VersionFile()
            .versionNo(DEFAULT_VERSION_NO)
            .versionInfo(DEFAULT_VERSION_INFO)
            .updateDate(DEFAULT_UPDATE_DATE)
            .latestVersionNo(DEFAULT_LATEST_VERSION_NO)
            .latestVersionInfo(DEFAULT_LATEST_VERSION_INFO)
            .latestUpdateDate(DEFAULT_LATEST_UPDATE_DATE)
            .appDownloadUrl(DEFAULT_APP_DOWNLOAD_URL);
        return versionFile;
    }

    @Before
    public void initTest() {
        versionFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createVersionFile() throws Exception {
        int databaseSizeBeforeCreate = versionFileRepository.findAll().size();

        // Create the VersionFile
        restVersionFileMockMvc.perform(post("/api/version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionFile)))
            .andExpect(status().isCreated());

        // Validate the VersionFile in the database
        List<VersionFile> versionFileList = versionFileRepository.findAll();
        assertThat(versionFileList).hasSize(databaseSizeBeforeCreate + 1);
        VersionFile testVersionFile = versionFileList.get(versionFileList.size() - 1);
        assertThat(testVersionFile.getVersionNo()).isEqualTo(DEFAULT_VERSION_NO);
        assertThat(testVersionFile.getVersionInfo()).isEqualTo(DEFAULT_VERSION_INFO);
        assertThat(testVersionFile.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testVersionFile.getLatestVersionNo()).isEqualTo(DEFAULT_LATEST_VERSION_NO);
        assertThat(testVersionFile.getLatestVersionInfo()).isEqualTo(DEFAULT_LATEST_VERSION_INFO);
        assertThat(testVersionFile.getLatestUpdateDate()).isEqualTo(DEFAULT_LATEST_UPDATE_DATE);
        assertThat(testVersionFile.getAppDownloadUrl()).isEqualTo(DEFAULT_APP_DOWNLOAD_URL);
    }

    @Test
    @Transactional
    public void createVersionFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = versionFileRepository.findAll().size();

        // Create the VersionFile with an existing ID
        versionFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVersionFileMockMvc.perform(post("/api/version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionFile)))
            .andExpect(status().isBadRequest());

        // Validate the VersionFile in the database
        List<VersionFile> versionFileList = versionFileRepository.findAll();
        assertThat(versionFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVersionFiles() throws Exception {
        // Initialize the database
        versionFileRepository.saveAndFlush(versionFile);

        // Get all the versionFileList
        restVersionFileMockMvc.perform(get("/api/version-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(versionFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNo").value(hasItem(DEFAULT_VERSION_NO.toString())))
            .andExpect(jsonPath("$.[*].versionInfo").value(hasItem(DEFAULT_VERSION_INFO.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].latestVersionNo").value(hasItem(DEFAULT_LATEST_VERSION_NO.toString())))
            .andExpect(jsonPath("$.[*].latestVersionInfo").value(hasItem(DEFAULT_LATEST_VERSION_INFO.toString())))
            .andExpect(jsonPath("$.[*].latestUpdateDate").value(hasItem(DEFAULT_LATEST_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].appDownloadUrl").value(hasItem(DEFAULT_APP_DOWNLOAD_URL.toString())));
    }
    

    @Test
    @Transactional
    public void getVersionFile() throws Exception {
        // Initialize the database
        versionFileRepository.saveAndFlush(versionFile);

        // Get the versionFile
        restVersionFileMockMvc.perform(get("/api/version-files/{id}", versionFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(versionFile.getId().intValue()))
            .andExpect(jsonPath("$.versionNo").value(DEFAULT_VERSION_NO.toString()))
            .andExpect(jsonPath("$.versionInfo").value(DEFAULT_VERSION_INFO.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.latestVersionNo").value(DEFAULT_LATEST_VERSION_NO.toString()))
            .andExpect(jsonPath("$.latestVersionInfo").value(DEFAULT_LATEST_VERSION_INFO.toString()))
            .andExpect(jsonPath("$.latestUpdateDate").value(DEFAULT_LATEST_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.appDownloadUrl").value(DEFAULT_APP_DOWNLOAD_URL.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingVersionFile() throws Exception {
        // Get the versionFile
        restVersionFileMockMvc.perform(get("/api/version-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVersionFile() throws Exception {
        // Initialize the database
        versionFileRepository.saveAndFlush(versionFile);

        int databaseSizeBeforeUpdate = versionFileRepository.findAll().size();

        // Update the versionFile
        VersionFile updatedVersionFile = versionFileRepository.findById(versionFile.getId()).get();
        // Disconnect from session so that the updates on updatedVersionFile are not directly saved in db
        em.detach(updatedVersionFile);
        updatedVersionFile
            .versionNo(UPDATED_VERSION_NO)
            .versionInfo(UPDATED_VERSION_INFO)
            .updateDate(UPDATED_UPDATE_DATE)
            .latestVersionNo(UPDATED_LATEST_VERSION_NO)
            .latestVersionInfo(UPDATED_LATEST_VERSION_INFO)
            .latestUpdateDate(UPDATED_LATEST_UPDATE_DATE)
            .appDownloadUrl(UPDATED_APP_DOWNLOAD_URL);

        restVersionFileMockMvc.perform(put("/api/version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVersionFile)))
            .andExpect(status().isOk());

        // Validate the VersionFile in the database
        List<VersionFile> versionFileList = versionFileRepository.findAll();
        assertThat(versionFileList).hasSize(databaseSizeBeforeUpdate);
        VersionFile testVersionFile = versionFileList.get(versionFileList.size() - 1);
        assertThat(testVersionFile.getVersionNo()).isEqualTo(UPDATED_VERSION_NO);
        assertThat(testVersionFile.getVersionInfo()).isEqualTo(UPDATED_VERSION_INFO);
        assertThat(testVersionFile.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testVersionFile.getLatestVersionNo()).isEqualTo(UPDATED_LATEST_VERSION_NO);
        assertThat(testVersionFile.getLatestVersionInfo()).isEqualTo(UPDATED_LATEST_VERSION_INFO);
        assertThat(testVersionFile.getLatestUpdateDate()).isEqualTo(UPDATED_LATEST_UPDATE_DATE);
        assertThat(testVersionFile.getAppDownloadUrl()).isEqualTo(UPDATED_APP_DOWNLOAD_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingVersionFile() throws Exception {
        int databaseSizeBeforeUpdate = versionFileRepository.findAll().size();

        // Create the VersionFile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVersionFileMockMvc.perform(put("/api/version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionFile)))
            .andExpect(status().isBadRequest());

        // Validate the VersionFile in the database
        List<VersionFile> versionFileList = versionFileRepository.findAll();
        assertThat(versionFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVersionFile() throws Exception {
        // Initialize the database
        versionFileRepository.saveAndFlush(versionFile);

        int databaseSizeBeforeDelete = versionFileRepository.findAll().size();

        // Get the versionFile
        restVersionFileMockMvc.perform(delete("/api/version-files/{id}", versionFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VersionFile> versionFileList = versionFileRepository.findAll();
        assertThat(versionFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VersionFile.class);
        VersionFile versionFile1 = new VersionFile();
        versionFile1.setId(1L);
        VersionFile versionFile2 = new VersionFile();
        versionFile2.setId(versionFile1.getId());
        assertThat(versionFile1).isEqualTo(versionFile2);
        versionFile2.setId(2L);
        assertThat(versionFile1).isNotEqualTo(versionFile2);
        versionFile1.setId(null);
        assertThat(versionFile1).isNotEqualTo(versionFile2);
    }
}
