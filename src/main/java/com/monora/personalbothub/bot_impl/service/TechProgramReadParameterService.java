package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TechProgramReadParameterService {
    CompletableFuture<Map<String, Object>> loadAllProgramParameters(int unitId);
    CompletableFuture<Map<String, Object>> loadProgramParameters(int programNumber, int unitId);
    CompletableFuture<FiringStatus> readStatusDevice(int unitId);
    Optional<Double> readStoredDotParameterFromMap(Map<String, Object> parameters, String parameterName);
    Optional<Integer> readInt16ParameterFromMap(Map<String, Object> parameters, String parameterName);
    CompletableFuture<Optional<Integer>> readProgramNumberDevice(int unitId);
    CompletableFuture<Optional<Integer>> readCurrentPowerDevice(int unitId);
}
