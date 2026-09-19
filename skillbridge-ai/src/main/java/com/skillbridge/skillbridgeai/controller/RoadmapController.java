package com.skillbridge.skillbridgeai.controller;

import com.skillbridge.skillbridgeai.model.Roadmap;
import com.skillbridge.skillbridgeai.service.GeminiApiException;
import com.skillbridge.skillbridgeai.service.GeminiQuotaException;
import com.skillbridge.skillbridgeai.service.RoadmapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/roadmap")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @PostMapping
    public ResponseEntity<?> generateRoadmap(
            @RequestParam String targetRole,
            @RequestBody List<String> missingSkills) {

        try {

            Roadmap roadmap =
                    roadmapService.generateRoadmap(
                            targetRole,
                            missingSkills
                    );

            return ResponseEntity.ok(roadmap);

        } catch (GeminiQuotaException e) {

            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(e.getMessage());

        } catch (GeminiApiException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(e.getMessage());
        }
    }
}