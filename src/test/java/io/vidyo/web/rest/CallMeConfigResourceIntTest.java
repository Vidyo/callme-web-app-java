package io.vidyo.web.rest;

import io.vidyo.VidyoioadminApp;

import io.vidyo.domain.CallMeConfig;
import io.vidyo.repository.CallMeConfigRepository;
import io.vidyo.service.dto.CallMeConfigDTO;
import io.vidyo.service.mapper.CallMeConfigMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CallMeConfigResource REST controller.
 *
 * @see CallMeConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VidyoioadminApp.class)
public class CallMeConfigResourceIntTest {

    private static final String DEFAULT_APP_KEY = "AAAAAAAAAA";
    private static final String UPDATED_APP_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APP_ID = "AAAAAAAAAA";
    private static final String UPDATED_APP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEV_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DEV_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_REFERRERS = "AAAAAAAAAA";
    private static final String UPDATED_REFERRERS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_TO = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_TO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_FROM = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_SMS_TO = "AAAAAAAAAA";
    private static final String UPDATED_SMS_TO = "BBBBBBBBBB";

    @Inject
    private CallMeConfigRepository callMeConfigRepository;

    @Inject
    private CallMeConfigMapper callMeConfigMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCallMeConfigMockMvc;

    private CallMeConfig callMeConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CallMeConfigResource callMeConfigResource = new CallMeConfigResource();
        ReflectionTestUtils.setField(callMeConfigResource, "callMeConfigRepository", callMeConfigRepository);
        ReflectionTestUtils.setField(callMeConfigResource, "callMeConfigMapper", callMeConfigMapper);
        this.restCallMeConfigMockMvc = MockMvcBuilders.standaloneSetup(callMeConfigResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CallMeConfig createEntity(EntityManager em) {
        CallMeConfig callMeConfig = new CallMeConfig()
                .appKey(DEFAULT_APP_KEY)
                .name(DEFAULT_NAME)
                .appId(DEFAULT_APP_ID)
                .devKey(DEFAULT_DEV_KEY)
                .referrers(DEFAULT_REFERRERS)
                .emailTo(DEFAULT_EMAIL_TO)
                .emailFrom(DEFAULT_EMAIL_FROM)
                .emailSubject(DEFAULT_EMAIL_SUBJECT)
                .smsTo(DEFAULT_SMS_TO);
        return callMeConfig;
    }

    @Before
    public void initTest() {
        callMeConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createCallMeConfig() throws Exception {
        int databaseSizeBeforeCreate = callMeConfigRepository.findAll().size();

        // Create the CallMeConfig
        CallMeConfigDTO callMeConfigDTO = callMeConfigMapper.callMeConfigToCallMeConfigDTO(callMeConfig);

        restCallMeConfigMockMvc.perform(post("/api/call-me-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(callMeConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the CallMeConfig in the database
        List<CallMeConfig> callMeConfigs = callMeConfigRepository.findAll();
        assertThat(callMeConfigs).hasSize(databaseSizeBeforeCreate + 1);
        CallMeConfig testCallMeConfig = callMeConfigs.get(callMeConfigs.size() - 1);
        assertThat(testCallMeConfig.getAppKey()).isEqualTo(DEFAULT_APP_KEY);
        assertThat(testCallMeConfig.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCallMeConfig.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testCallMeConfig.getDevKey()).isEqualTo(DEFAULT_DEV_KEY);
        assertThat(testCallMeConfig.getReferrers()).isEqualTo(DEFAULT_REFERRERS);
        assertThat(testCallMeConfig.getEmailTo()).isEqualTo(DEFAULT_EMAIL_TO);
        assertThat(testCallMeConfig.getEmailFrom()).isEqualTo(DEFAULT_EMAIL_FROM);
        assertThat(testCallMeConfig.getEmailSubject()).isEqualTo(DEFAULT_EMAIL_SUBJECT);
        assertThat(testCallMeConfig.getSmsTo()).isEqualTo(DEFAULT_SMS_TO);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = callMeConfigRepository.findAll().size();
        // set the field null
        callMeConfig.setName(null);

        // Create the CallMeConfig, which fails.
        CallMeConfigDTO callMeConfigDTO = callMeConfigMapper.callMeConfigToCallMeConfigDTO(callMeConfig);

        restCallMeConfigMockMvc.perform(post("/api/call-me-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(callMeConfigDTO)))
            .andExpect(status().isBadRequest());

        List<CallMeConfig> callMeConfigs = callMeConfigRepository.findAll();
        assertThat(callMeConfigs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAppIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = callMeConfigRepository.findAll().size();
        // set the field null
        callMeConfig.setAppId(null);

        // Create the CallMeConfig, which fails.
        CallMeConfigDTO callMeConfigDTO = callMeConfigMapper.callMeConfigToCallMeConfigDTO(callMeConfig);

        restCallMeConfigMockMvc.perform(post("/api/call-me-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(callMeConfigDTO)))
            .andExpect(status().isBadRequest());

        List<CallMeConfig> callMeConfigs = callMeConfigRepository.findAll();
        assertThat(callMeConfigs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDevKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = callMeConfigRepository.findAll().size();
        // set the field null
        callMeConfig.setDevKey(null);

        // Create the CallMeConfig, which fails.
        CallMeConfigDTO callMeConfigDTO = callMeConfigMapper.callMeConfigToCallMeConfigDTO(callMeConfig);

        restCallMeConfigMockMvc.perform(post("/api/call-me-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(callMeConfigDTO)))
            .andExpect(status().isBadRequest());

        List<CallMeConfig> callMeConfigs = callMeConfigRepository.findAll();
        assertThat(callMeConfigs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCallMeConfigs() throws Exception {
        // Initialize the database
        callMeConfigRepository.saveAndFlush(callMeConfig);

        // Get all the callMeConfigs
        restCallMeConfigMockMvc.perform(get("/api/call-me-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(callMeConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].appKey").value(hasItem(DEFAULT_APP_KEY.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].appId").value(hasItem(DEFAULT_APP_ID.toString())))
            .andExpect(jsonPath("$.[*].devKey").value(hasItem(DEFAULT_DEV_KEY.toString())))
            .andExpect(jsonPath("$.[*].referrers").value(hasItem(DEFAULT_REFERRERS.toString())))
            .andExpect(jsonPath("$.[*].emailTo").value(hasItem(DEFAULT_EMAIL_TO.toString())))
            .andExpect(jsonPath("$.[*].emailFrom").value(hasItem(DEFAULT_EMAIL_FROM.toString())))
            .andExpect(jsonPath("$.[*].emailSubject").value(hasItem(DEFAULT_EMAIL_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].smsTo").value(hasItem(DEFAULT_SMS_TO.toString())));
    }

    @Test
    @Transactional
    public void getCallMeConfig() throws Exception {
        // Initialize the database
        callMeConfigRepository.saveAndFlush(callMeConfig);

        // Get the callMeConfig
        restCallMeConfigMockMvc.perform(get("/api/call-me-configs/{id}", callMeConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(callMeConfig.getId().intValue()))
            .andExpect(jsonPath("$.appKey").value(DEFAULT_APP_KEY.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.appId").value(DEFAULT_APP_ID.toString()))
            .andExpect(jsonPath("$.devKey").value(DEFAULT_DEV_KEY.toString()))
            .andExpect(jsonPath("$.referrers").value(DEFAULT_REFERRERS.toString()))
            .andExpect(jsonPath("$.emailTo").value(DEFAULT_EMAIL_TO.toString()))
            .andExpect(jsonPath("$.emailFrom").value(DEFAULT_EMAIL_FROM.toString()))
            .andExpect(jsonPath("$.emailSubject").value(DEFAULT_EMAIL_SUBJECT.toString()))
            .andExpect(jsonPath("$.smsTo").value(DEFAULT_SMS_TO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCallMeConfig() throws Exception {
        // Get the callMeConfig
        restCallMeConfigMockMvc.perform(get("/api/call-me-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCallMeConfig() throws Exception {
        // Initialize the database
        callMeConfigRepository.saveAndFlush(callMeConfig);
        int databaseSizeBeforeUpdate = callMeConfigRepository.findAll().size();

        // Update the callMeConfig
        CallMeConfig updatedCallMeConfig = callMeConfigRepository.findOne(callMeConfig.getId());
        updatedCallMeConfig
                .appKey(UPDATED_APP_KEY)
                .name(UPDATED_NAME)
                .appId(UPDATED_APP_ID)
                .devKey(UPDATED_DEV_KEY)
                .referrers(UPDATED_REFERRERS)
                .emailTo(UPDATED_EMAIL_TO)
                .emailFrom(UPDATED_EMAIL_FROM)
                .emailSubject(UPDATED_EMAIL_SUBJECT)
                .smsTo(UPDATED_SMS_TO);
        CallMeConfigDTO callMeConfigDTO = callMeConfigMapper.callMeConfigToCallMeConfigDTO(updatedCallMeConfig);

        restCallMeConfigMockMvc.perform(put("/api/call-me-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(callMeConfigDTO)))
            .andExpect(status().isOk());

        // Validate the CallMeConfig in the database
        List<CallMeConfig> callMeConfigs = callMeConfigRepository.findAll();
        assertThat(callMeConfigs).hasSize(databaseSizeBeforeUpdate);
        CallMeConfig testCallMeConfig = callMeConfigs.get(callMeConfigs.size() - 1);
        assertThat(testCallMeConfig.getAppKey()).isEqualTo(UPDATED_APP_KEY);
        assertThat(testCallMeConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCallMeConfig.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testCallMeConfig.getDevKey()).isEqualTo(UPDATED_DEV_KEY);
        assertThat(testCallMeConfig.getReferrers()).isEqualTo(UPDATED_REFERRERS);
        assertThat(testCallMeConfig.getEmailTo()).isEqualTo(UPDATED_EMAIL_TO);
        assertThat(testCallMeConfig.getEmailFrom()).isEqualTo(UPDATED_EMAIL_FROM);
        assertThat(testCallMeConfig.getEmailSubject()).isEqualTo(UPDATED_EMAIL_SUBJECT);
        assertThat(testCallMeConfig.getSmsTo()).isEqualTo(UPDATED_SMS_TO);
    }

    @Test
    @Transactional
    public void deleteCallMeConfig() throws Exception {
        // Initialize the database
        callMeConfigRepository.saveAndFlush(callMeConfig);
        int databaseSizeBeforeDelete = callMeConfigRepository.findAll().size();

        // Get the callMeConfig
        restCallMeConfigMockMvc.perform(delete("/api/call-me-configs/{id}", callMeConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CallMeConfig> callMeConfigs = callMeConfigRepository.findAll();
        assertThat(callMeConfigs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
