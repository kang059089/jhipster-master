package com.kang.jhipster.web.rest;

import com.kang.jhipster.JhipsterMasterApp;

import com.kang.jhipster.domain.UserVersionFile;
import com.kang.jhipster.repository.UserVersionFileRepository;
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
 * Test class for the UserVersionFileResource REST controller.
 *
 * @see UserVersionFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMasterApp.class)
public class UserVersionFileResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_VERSION_NO = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_NO = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION_INFO = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_INFO = "BBBBBBBBBB";

    private static final Instant DEFAULT_VERSION_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERSION_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UserVersionFileRepository userVersionFileRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserVersionFileMockMvc;

    private UserVersionFile userVersionFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserVersionFileResource userVersionFileResource = new UserVersionFileResource(userVersionFileRepository);
        this.restUserVersionFileMockMvc = MockMvcBuilders.standaloneSetup(userVersionFileResource)
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
    public static UserVersionFile createEntity(EntityManager em) {
        UserVersionFile userVersionFile = new UserVersionFile()
            .userId(DEFAULT_USER_ID)
            .versionNo(DEFAULT_VERSION_NO)
            .versionInfo(DEFAULT_VERSION_INFO)
            .versionReleaseDate(DEFAULT_VERSION_RELEASE_DATE);
        return userVersionFile;
    }

    @Before
    public void initTest() {
        userVersionFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserVersionFile() throws Exception {
        int databaseSizeBeforeCreate = userVersionFileRepository.findAll().size();

        // Create the UserVersionFile
        restUserVersionFileMockMvc.perform(post("/api/user-version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userVersionFile)))
            .andExpect(status().isCreated());

        // Validate the UserVersionFile in the database
        List<UserVersionFile> userVersionFileList = userVersionFileRepository.findAll();
        assertThat(userVersionFileList).hasSize(databaseSizeBeforeCreate + 1);
        UserVersionFile testUserVersionFile = userVersionFileList.get(userVersionFileList.size() - 1);
        assertThat(testUserVersionFile.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserVersionFile.getVersionNo()).isEqualTo(DEFAULT_VERSION_NO);
        assertThat(testUserVersionFile.getVersionInfo()).isEqualTo(DEFAULT_VERSION_INFO);
        assertThat(testUserVersionFile.getVersionReleaseDate()).isEqualTo(DEFAULT_VERSION_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void createUserVersionFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userVersionFileRepository.findAll().size();

        // Create the UserVersionFile with an existing ID
        userVersionFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserVersionFileMockMvc.perform(post("/api/user-version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userVersionFile)))
            .andExpect(status().isBadRequest());

        // Validate the UserVersionFile in the database
        List<UserVersionFile> userVersionFileList = userVersionFileRepository.findAll();
        assertThat(userVersionFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserVersionFiles() throws Exception {
        // Initialize the database
        userVersionFileRepository.saveAndFlush(userVersionFile);

        // Get all the userVersionFileList
        restUserVersionFileMockMvc.perform(get("/api/user-version-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userVersionFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].versionNo").value(hasItem(DEFAULT_VERSION_NO.toString())))
            .andExpect(jsonPath("$.[*].versionInfo").value(hasItem(DEFAULT_VERSION_INFO.toString())))
            .andExpect(jsonPath("$.[*].versionReleaseDate").value(hasItem(DEFAULT_VERSION_RELEASE_DATE.toString())));
    }


    @Test
    @Transactional
    public void getUserVersionFile() throws Exception {
        // Initialize the database
        userVersionFileRepository.saveAndFlush(userVersionFile);

        // Get the userVersionFile
        restUserVersionFileMockMvc.perform(get("/api/user-version-files/{id}", userVersionFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userVersionFile.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.versionNo").value(DEFAULT_VERSION_NO.toString()))
            .andExpect(jsonPath("$.versionInfo").value(DEFAULT_VERSION_INFO.toString()))
            .andExpect(jsonPath("$.versionReleaseDate").value(DEFAULT_VERSION_RELEASE_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingUserVersionFile() throws Exception {
        // Get the userVersionFile
        restUserVersionFileMockMvc.perform(get("/api/user-version-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserVersionFile() throws Exception {
        // Initialize the database
        userVersionFileRepository.saveAndFlush(userVersionFile);

        int databaseSizeBeforeUpdate = userVersionFileRepository.findAll().size();

        // Update the userVersionFile
        UserVersionFile updatedUserVersionFile = userVersionFileRepository.findById(userVersionFile.getId()).get();
        // Disconnect from session so that the updates on updatedUserVersionFile are not directly saved in db
        em.detach(updatedUserVersionFile);
        updatedUserVersionFile
            .userId(UPDATED_USER_ID)
            .versionNo(UPDATED_VERSION_NO)
            .versionInfo(UPDATED_VERSION_INFO)
            .versionReleaseDate(UPDATED_VERSION_RELEASE_DATE);

        restUserVersionFileMockMvc.perform(put("/api/user-version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserVersionFile)))
            .andExpect(status().isOk());

        // Validate the UserVersionFile in the database
        List<UserVersionFile> userVersionFileList = userVersionFileRepository.findAll();
        assertThat(userVersionFileList).hasSize(databaseSizeBeforeUpdate);
        UserVersionFile testUserVersionFile = userVersionFileList.get(userVersionFileList.size() - 1);
        assertThat(testUserVersionFile.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserVersionFile.getVersionNo()).isEqualTo(UPDATED_VERSION_NO);
        assertThat(testUserVersionFile.getVersionInfo()).isEqualTo(UPDATED_VERSION_INFO);
        assertThat(testUserVersionFile.getVersionReleaseDate()).isEqualTo(UPDATED_VERSION_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserVersionFile() throws Exception {
        int databaseSizeBeforeUpdate = userVersionFileRepository.findAll().size();

        // Create the UserVersionFile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserVersionFileMockMvc.perform(put("/api/user-version-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userVersionFile)))
            .andExpect(status().isBadRequest());

        // Validate the UserVersionFile in the database
        List<UserVersionFile> userVersionFileList = userVersionFileRepository.findAll();
        assertThat(userVersionFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserVersionFile() throws Exception {
        // Initialize the database
        userVersionFileRepository.saveAndFlush(userVersionFile);

        int databaseSizeBeforeDelete = userVersionFileRepository.findAll().size();

        // Get the userVersionFile
        restUserVersionFileMockMvc.perform(delete("/api/user-version-files/{id}", userVersionFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserVersionFile> userVersionFileList = userVersionFileRepository.findAll();
        assertThat(userVersionFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserVersionFile.class);
        UserVersionFile userVersionFile1 = new UserVersionFile();
        userVersionFile1.setId(1L);
        UserVersionFile userVersionFile2 = new UserVersionFile();
        userVersionFile2.setId(userVersionFile1.getId());
        assertThat(userVersionFile1).isEqualTo(userVersionFile2);
        userVersionFile2.setId(2L);
        assertThat(userVersionFile1).isNotEqualTo(userVersionFile2);
        userVersionFile1.setId(null);
        assertThat(userVersionFile1).isNotEqualTo(userVersionFile2);
    }
}
