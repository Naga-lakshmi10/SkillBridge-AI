package com.skillbridge.skillbridgeai.service;

import com.skillbridge.skillbridgeai.model.Roadmap;
import com.skillbridge.skillbridgeai.model.RoadmapPhase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoadmapService {

    private final GeminiService geminiService;

    public RoadmapService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public Roadmap generateRoadmap(
            String targetRole,
            List<String> missingSkills) {

        String prompt = """
                Create a personalized learning roadmap for a candidate
                targeting this role:

                Target role:
                %s

                Missing skills:
                %s

                Create a practical roadmap with 4 phases.

                Each phase must contain:
                - phase number
                - title
                - duration
                - topics
                - practical tasks

                Return valid JSON only using exactly this structure:

                {
                  "targetRole": "Java Backend Developer",
                  "phases": [
                    {
                      "phase": "Phase 1",
                      "title": "Core Java",
                      "duration": "2 weeks",
                      "topics": ["topic1", "topic2"],
                      "tasks": ["task1", "task2"]
                    }
                  ]
                }

                Rules:
                - Return JSON only.
                - Do not use Markdown.
                - Do not use code fences.
                - Focus on the candidate's missing skills.
                - Make the roadmap practical and job-oriented.
                """.formatted(
                targetRole,
                String.join(", ", missingSkills)
        );

        String aiResponse = geminiService.analyzeWithGemini(prompt);

        return parseRoadmap(aiResponse);
    }

    private Roadmap parseRoadmap(String json) {

        Roadmap roadmap = new Roadmap();

        roadmap.setTargetRole(
                extractString(json, "targetRole")
        );

        roadmap.setPhases(
                extractPhases(json)
        );

        return roadmap;
    }

    private List<RoadmapPhase> extractPhases(String json) {

        List<RoadmapPhase> phases = new ArrayList<>();

        int phasesStart = json.indexOf("\"phases\"");

        if (phasesStart == -1) {
            return phases;
        }

        int arrayStart = json.indexOf("[", phasesStart);
        int arrayEnd = json.lastIndexOf("]");

        if (arrayStart == -1 || arrayEnd == -1) {
            return phases;
        }

        String phasesText = json.substring(
                arrayStart + 1,
                arrayEnd
        );

        String[] phaseObjects =
                phasesText.split("\\},\\s*\\{");

        for (String phaseObject : phaseObjects) {

            String cleanObject = phaseObject
                    .replace("{", "")
                    .replace("}", "");

            RoadmapPhase phase = new RoadmapPhase();

            phase.setPhase(
                    extractString(cleanObject, "phase")
            );

            phase.setTitle(
                    extractString(cleanObject, "title")
            );

            phase.setDuration(
                    extractString(cleanObject, "duration")
            );

            phase.setTopics(
                    extractArray(cleanObject, "topics")
            );

            phase.setTasks(
                    extractArray(cleanObject, "tasks")
            );

            phases.add(phase);
        }

        return phases;
    }

    private String extractString(
            String json,
            String fieldName) {

        String marker = "\"" + fieldName + "\"";

        int fieldStart = json.indexOf(marker);

        if (fieldStart == -1) {
            return "";
        }

        int colon = json.indexOf(":", fieldStart);

        if (colon == -1) {
            return "";
        }

        int firstQuote = json.indexOf("\"", colon + 1);

        if (firstQuote == -1) {
            return "";
        }

        int secondQuote =
                json.indexOf("\"", firstQuote + 1);

        if (secondQuote == -1) {
            return "";
        }

        return json.substring(
                firstQuote + 1,
                secondQuote
        );
    }

    private List<String> extractArray(
            String json,
            String fieldName) {

        List<String> items = new ArrayList<>();

        String marker = "\"" + fieldName + "\"";

        int fieldStart = json.indexOf(marker);

        if (fieldStart == -1) {
            return items;
        }

        int arrayStart = json.indexOf("[", fieldStart);
        int arrayEnd = json.indexOf("]", arrayStart);

        if (arrayStart == -1 || arrayEnd == -1) {
            return items;
        }

        String content = json.substring(
                arrayStart + 1,
                arrayEnd
        );

        String[] values = content.split("\",\\s*\"");

        for (String value : values) {

            String cleanValue = value
                    .replace("\"", "")
                    .trim();

            if (!cleanValue.isEmpty()) {
                items.add(cleanValue);
            }
        }

        return items;
    }
}