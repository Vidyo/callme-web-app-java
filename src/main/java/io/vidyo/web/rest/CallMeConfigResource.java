package io.vidyo.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.vidyo.domain.CallMeConfig;

import io.vidyo.domain.User;
import io.vidyo.repository.CallMeConfigRepository;
import io.vidyo.repository.UserRepository;
import io.vidyo.security.SecurityUtils;
import io.vidyo.service.mapper.UserMapper;
import io.vidyo.service.util.RandomUtil;
import io.vidyo.web.rest.util.HeaderUtil;
import io.vidyo.web.rest.util.PaginationUtil;
import io.vidyo.service.dto.CallMeConfigDTO;
import io.vidyo.service.mapper.CallMeConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing CallMeConfig.
 */
@RestController
@RequestMapping("/api")
public class CallMeConfigResource {

    private final Logger log = LoggerFactory.getLogger(CallMeConfigResource.class);

    @Inject
    private CallMeConfigRepository callMeConfigRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private CallMeConfigMapper callMeConfigMapper;


    /**
     * POST  /call-me-configs : Create a new callMeConfig.
     *
     * @param callMeConfigDTO the callMeConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new callMeConfigDTO, or with status 400 (Bad Request) if the callMeConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/call-me-configs")
    @Timed
    public ResponseEntity<CallMeConfigDTO> createCallMeConfig(@Valid @RequestBody CallMeConfigDTO callMeConfigDTO) throws URISyntaxException {
        log.debug("REST request to save CallMeConfig : {}", callMeConfigDTO);
        if (callMeConfigDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("callMeConfig", "idexists", "A new callMeConfig cannot already have an ID")).body(null);
        }
        CallMeConfig callMeConfig = callMeConfigMapper.callMeConfigDTOToCallMeConfig(callMeConfigDTO);
        callMeConfig.setAppKey(RandomUtil.generateRandomAlphaNumeric(8));

        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (currentUser.isPresent()) {
            callMeConfig.setOwner(currentUser.get());
        }
        callMeConfig = callMeConfigRepository.save(callMeConfig);
        CallMeConfigDTO result = callMeConfigMapper.callMeConfigToCallMeConfigDTO(callMeConfig);
        return ResponseEntity.created(new URI("/api/call-me-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("callMeConfig", result.getName().toString()))
            .body(result);
    }

    /**
     * PUT  /call-me-configs : Updates an existing callMeConfig.
     *
     * @param callMeConfigDTO the callMeConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated callMeConfigDTO,
     * or with status 400 (Bad Request) if the callMeConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the callMeConfigDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/call-me-configs")
    @Timed
    public ResponseEntity<CallMeConfigDTO> updateCallMeConfig(@Valid @RequestBody CallMeConfigDTO callMeConfigDTO) throws URISyntaxException {
        log.debug("REST request to update CallMeConfig : {}", callMeConfigDTO);
        if (callMeConfigDTO.getId() == null) {
            return createCallMeConfig(callMeConfigDTO);
        }
        CallMeConfig callMeConfig = callMeConfigMapper.callMeConfigDTOToCallMeConfig(callMeConfigDTO);

        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (currentUser.isPresent()) {
            callMeConfig.setOwner(currentUser.get());
        }

        // do not update appKey on update
        CallMeConfig callMeConfigOrig = callMeConfigRepository.findOne(callMeConfigDTO.getId());
        callMeConfig.setAppKey(callMeConfigOrig.getAppKey());

        callMeConfig = callMeConfigRepository.save(callMeConfig);
        CallMeConfigDTO result = callMeConfigMapper.callMeConfigToCallMeConfigDTO(callMeConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("callMeConfig", callMeConfigDTO.getName().toString()))
            .body(result);
    }

    /**
     * GET  /call-me-configs : get all the callMeConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of callMeConfigs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/call-me-configs")
    @Timed
    public ResponseEntity<List<CallMeConfigDTO>> getAllCallMeConfigs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CallMeConfigs");
        Page<CallMeConfig> page = callMeConfigRepository.findByOwnerIsCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/call-me-configs");
        return new ResponseEntity<>(callMeConfigMapper.callMeConfigsToCallMeConfigDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /call-me-configs/:id : get the "id" callMeConfig.
     *
     * @param id the id of the callMeConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the callMeConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/call-me-configs/{id}")
    @Timed
    public ResponseEntity<CallMeConfigDTO> getCallMeConfig(@PathVariable Long id) {
        log.debug("REST request to get CallMeConfig : {}", id);
        CallMeConfig callMeConfig = callMeConfigRepository.findOne(id);
        User owner = callMeConfig.getOwner();
        if (!owner.getLogin().equals(SecurityUtils.getCurrentUserLogin())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CallMeConfigDTO callMeConfigDTO = callMeConfigMapper.callMeConfigToCallMeConfigDTO(callMeConfig);
        return Optional.ofNullable(callMeConfigDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /call-me-configs/:id : delete the "id" callMeConfig.
     *
     * @param id the id of the callMeConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/call-me-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCallMeConfig(@PathVariable Long id) {
        log.debug("REST request to delete CallMeConfig : {}", id);

        CallMeConfig callMeConfig = callMeConfigRepository.findOne(id);
        User owner = callMeConfig.getOwner();
        if (!owner.getLogin().equals(SecurityUtils.getCurrentUserLogin())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        callMeConfigRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("callMeConfig", callMeConfig.getName())).build();
    }

}
