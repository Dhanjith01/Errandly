package com.errandly.Errandly.user.dto;

import com.errandly.Errandly.user.entity.AvailabilityStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunnerResponseDTO {
    private String zone;
    private AvailabilityStatus availabilityStatus;
}
