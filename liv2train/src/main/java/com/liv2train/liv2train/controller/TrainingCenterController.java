package com.liv2train.liv2train.controller;

import com.liv2train.liv2train.dto.TrainingCenterRequest;
import com.liv2train.liv2train.dto.TrainingCenterResponse;
import com.liv2train.liv2train.service.TrainingCenterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training-centers")
public class TrainingCenterController {

    private final TrainingCenterService service;

    public TrainingCenterController(TrainingCenterService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TrainingCenterResponse> create(@Valid @RequestBody TrainingCenterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping
    public ResponseEntity<List<TrainingCenterResponse>> listAll(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String pincode,
            @RequestParam(required = false) String course,
            @RequestParam(required = false, name = "centerCode") String code
    ) {
        return ResponseEntity.ok(service.listAll(city, state, pincode, course, code));
    }
}
