package com.skillbridge.skillbridgeai.controller;

import com.skillbridge.skillbridgeai.model.AnalysisRequest;
import com.skillbridge.skillbridgeai.model.AnalysisResult;
import com.skillbridge.skillbridgeai.service.AnalysisService;
import com.skillbridge.skillbridgeai.service.GeminiApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(
            AnalysisService analysisService) {

        this.analysisService = analysisService;
    }

    @PostMapping
    public ResponseEntity<?> analyze(
            @RequestBody AnalysisRequest request) {

        try {

            AnalysisResult result =
                    analysisService.analyze(request);

            return ResponseEntity.ok(result);

        } catch (GeminiApiException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            "An unexpected error occurred " +
                            "while analyzing the profile."
                    );
        }
    }
}