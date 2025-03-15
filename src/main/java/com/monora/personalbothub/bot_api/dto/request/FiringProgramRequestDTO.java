package com.monora.personalbothub.bot_api.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FiringProgramRequestDTO(

        @NotBlank
        String name,

        @NotNull
        @Size(min = 1, max = 5, message = "Program must have from 1 to 5 steps")
        List<@Valid FiringStepRequestDTO> steps

) {}
