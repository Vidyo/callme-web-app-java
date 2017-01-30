package io.vidyo.service.mapper;

import io.vidyo.domain.*;
import io.vidyo.service.dto.CallMeConfigDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CallMeConfig and its DTO CallMeConfigDTO.
 */
@Mapper(componentModel = "spring")
public interface CallMeConfigMapper {

    CallMeConfigDTO callMeConfigToCallMeConfigDTO(CallMeConfig callMeConfig);

    List<CallMeConfigDTO> callMeConfigsToCallMeConfigDTOs(List<CallMeConfig> callMeConfigs);

    CallMeConfig callMeConfigDTOToCallMeConfig(CallMeConfigDTO callMeConfigDTO);

    List<CallMeConfig> callMeConfigDTOsToCallMeConfigs(List<CallMeConfigDTO> callMeConfigDTOs);
}
