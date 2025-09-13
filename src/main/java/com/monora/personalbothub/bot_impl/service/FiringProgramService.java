package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.FiringProgramRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.FiringProgramResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.FiringSessionEntity;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;

import java.util.List;
import java.util.Optional;

public interface FiringProgramService {
    FiringProgramResponseDTO create(FiringProgramRequestDTO requestDTO);

    FiringProgramResponseDTO update(Integer id, FiringProgramRequestDTO requestDTO);

    void delete(Integer id);

    FiringProgramResponseDTO findById(Integer id);

    List<FiringProgramResponseDTO> findAll();

    List<FiringProgramResponseDTO> getAllTechProgramParametersAsFiringProgramDTO(int unitId);

    FiringProgramResponseDTO getOneTechProgramParametersAsFiringProgramDTO(int programNumber, int unitId);


}
