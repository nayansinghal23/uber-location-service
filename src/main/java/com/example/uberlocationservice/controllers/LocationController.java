package com.example.uberlocationservice.controllers;

import com.example.uberlocationservice.dtos.DriverLocationDTO;
import com.example.uberlocationservice.dtos.NearbyDriversRequestDTO;
import com.example.uberlocationservice.dtos.SaveDriverLocationRequestDTO;
import com.example.uberlocationservice.services.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/drivers")
    public ResponseEntity<Boolean> saveDriverLocation(@RequestBody SaveDriverLocationRequestDTO dto) {
        try {
            boolean isLocationSaved = locationService.saveDriverLocation(
                    dto.getDriverId(),
                    dto.getLatitude(),
                    dto.getLongitude()
            );
            return ResponseEntity.status(isLocationSaved ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(isLocationSaved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping("/nearby/drivers")
    public ResponseEntity<List<DriverLocationDTO>> getNearbyDrivers(@RequestBody NearbyDriversRequestDTO dto) {
        try {
            List<DriverLocationDTO> drivers = locationService.getNearByDrivers(
                    dto.getLatitude(),
                    dto.getLongitude()
            );
            return ResponseEntity.ok(drivers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
}
