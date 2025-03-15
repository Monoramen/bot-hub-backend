package com.monora.personalbothub.bot_db.entity.modbus;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FiringStep {

    @Column(name = "step_number")
    private Integer stepNumber;

    @Column(name = "target_temperature_c")
    private Double targetTemperatureC;

    @Column(name = "ramp_time_minutes")
    private Integer rampTimeMinutes;

    @Column(name = "hold_time_minutes")
    private Integer holdTimeMinutes;
}

