package com.example.uberlocationservice.services;

import com.example.uberlocationservice.dtos.DriverLocationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {
    Boolean saveDriverLocation(String driverId, Double latitude, Double longitude);
    List<DriverLocationDTO> getNearByDrivers(Double latitude, Double longitude);
}
