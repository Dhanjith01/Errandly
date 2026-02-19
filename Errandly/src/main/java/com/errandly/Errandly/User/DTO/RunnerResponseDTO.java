package com.errandly.Errandly.User.DTO;

import com.errandly.Errandly.User.Entity.AvailabilityStatus;

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
