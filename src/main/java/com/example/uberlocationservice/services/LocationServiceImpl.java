package com.example.uberlocationservice.services;

import com.example.uberlocationservice.dtos.DriverLocationDTO;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final StringRedisTemplate stringRedisTemplate;
    private static final String DRIVER_GEO_OPS_KEY = "drivers";
    private static final double SEARCH_RADIUS = 5.0;

    public LocationServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Boolean saveDriverLocation(String driverId, Double latitude, Double longitude) {
        try {
            GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
            geoOps.add(
                    DRIVER_GEO_OPS_KEY,
                    new Point(longitude, latitude),
                    driverId
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<DriverLocationDTO> getNearByDrivers(Double latitude, Double longitude) {
        try {
            GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
            Distance radius = new Distance(SEARCH_RADIUS, Metrics.KILOMETERS);
            Circle within = new Circle(new Point(longitude, latitude), radius);
            GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.radius(DRIVER_GEO_OPS_KEY, within, RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates());

            List<DriverLocationDTO> drivers = new ArrayList<>();
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
                drivers.add(
                        DriverLocationDTO
                                .builder()
                                .driverId(result.getContent().getName())
                                .latitude(result.getContent().getPoint().getY())
                                .longitude(result.getContent().getPoint().getX())
                                .build()
                );
            }
            return drivers;
        } catch (Exception e) {
            return List.of();
        }
    }
}
