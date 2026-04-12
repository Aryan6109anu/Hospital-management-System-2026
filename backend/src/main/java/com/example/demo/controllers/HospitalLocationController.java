package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.HospitalLocationDTO;
import com.example.demo.services.HospitalLocationService;

@RestController
@RequestMapping("/api/hospital")
public class HospitalLocationController {

    private final HospitalLocationService locationService;

    public HospitalLocationController(HospitalLocationService locationService) {
        this.locationService = locationService;
    }

    // GET hospital location
    @GetMapping("/location")
    public ResponseEntity<?> getLocation() {
        HospitalLocationDTO location = locationService.getLocation();
        return ResponseEntity.ok(location); // location null bhi ho sakta hai
    }


    // POST save/update hospital location
    @PostMapping("/location")
    public ResponseEntity<?> saveLocation(@RequestBody HospitalLocationDTO locationDTO) {
        if (locationDTO.getLat() == null || locationDTO.getLng() == null) {
            return ResponseEntity.badRequest().body("Latitude and Longitude are required");
        }
        HospitalLocationDTO saved = locationService.saveOrUpdateLocation(locationDTO);
        return ResponseEntity.ok(saved);
    }
}   
