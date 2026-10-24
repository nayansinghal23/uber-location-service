package com.example.uberlocationservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveDriverLocationRequestDTO {
    private String driverId;
    private Double latitude;
    private Double longitude;
}
