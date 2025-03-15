package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.response.FiringProgramResponseDTO;

public interface TechProgramWriteParameterService {
    void writeFiringProgram(FiringProgramResponseDTO program, int deviceProgramId, int unitId);

    void startStopTechProgram(boolean start, int unitId);

    void selectTechProgram(int programNumber, int unitId);
}
