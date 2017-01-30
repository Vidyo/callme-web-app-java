package io.vidyo.repository;

import io.vidyo.domain.CallMeConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CallMeConfig entity.
 */
@SuppressWarnings("unused")
public interface CallMeConfigRepository extends JpaRepository<CallMeConfig,Long> {

    @Query("select callMeConfig from CallMeConfig callMeConfig where callMeConfig.owner.login = ?#{principal.username}")
    List<CallMeConfig> findByOwnerIsCurrentUser();


    @Query("select callMeConfig from CallMeConfig callMeConfig where callMeConfig.owner.login = ?#{principal.username}")
    Page<CallMeConfig> findByOwnerIsCurrentUser(Pageable pageable);

    CallMeConfig findByAppKey(String appKey);
}
