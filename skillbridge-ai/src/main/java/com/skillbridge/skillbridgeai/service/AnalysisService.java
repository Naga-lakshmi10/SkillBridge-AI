package com.skillbridge.skillbridgeai.service;

import com.skillbridge.skillbridgeai.model.AnalysisRequest;
import com.skillbridge.skillbridgeai.model.AnalysisResult;
import com.skillbridge.skillbridgeai.model.Roadmap;
import com.skillbridge.skillbridgeai.model.RoadmapPhase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisService {

    private final GeminiService geminiService;

    public AnalysisService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public AnalysisResult analyze(AnalysisRequest request) {

        String resumeSkills =
                request.getResume().getSkills();

        String jobTitle =
                request.getJobDescription().getJobTitle();

        String requiredSkills =
                request.getJobDescription().getRequiredSkills();

        String prompt = """
                Analyze this candidate for the target job and create
                a personalized learning roadmap, project plan, and
                resume improvement plan.

                Candidate profile:
                %s

                Target job:
                %s

                Required skills:
                %s

                Return valid JSON only.

                Use exactly this structure:

                {
                  "matchedSkills": [
                    "skill1",
                    "skill2"
                  ],
                  "missingSkills": [
                    "skill1",
                    "skill2"
                  ],
                  "resumeGaps": [
                    "gap1",
                    "gap2"
                  ],
                  "recommendations": [
                    "recommendation1",
                    "recommendation2"
                  ],
                  "projectRecommendations": [
                    "Project 1: description",
                    "Project 2: description",
                    "Project 3: description"
                  ],
                  "resumeImprovements": [
                    "Improvement 1",
                    "Improvement 2",
                    "Improvement 3"
                  ],
                  "roadmap": {
                    "targetRole": "Java Backend Developer",
                    "phases": [
                      {
                        "phase": "Phase 1",
                        "title": "Core Java",
                        "duration": "2 weeks",
                        "topics": [
                          "topic1",
                          "topic2"
                        ],
                        "tasks": [
                          "task1",
                          "task2"
                        ]
                      }
                    ]
                  }
                }

                Project recommendation requirements:
                - Recommend exactly 3 practical projects.
                - Projects must directly address the candidate's missing
                  skills.
                - Projects should be realistic for a student.
                - Projects should be strong enough to demonstrate skills
                  on a resume and GitHub.
                - Prefer projects that combine multiple missing skills.
                - Do not recommend projects unrelated to the target role.
                - Include a short description of what each project should
                  demonstrate.

                Resume improvement requirements:
                - Give practical changes the candidate can make to the
                  resume.
                - Focus on missing skills, weak evidence, project details,
                  measurable achievements, and relevant keywords.
                - Do not invent experience or qualifications.
                - Do not claim the candidate has a skill unless it appears
                  in the candidate profile.
                - Give at least 3 improvements.

                Roadmap requirements:
                - Create exactly 4 phases.
                - Focus the roadmap on the candidate's missing skills.
                - Make every phase practical and job-oriented.
                - Include topics and hands-on tasks.

                Rules:
                - Return JSON only.
                - Do not use Markdown.
                - Do not use code fences.
                - Each array item must be a separate JSON string.
                - Only use information provided by the candidate.
                - Do not invent qualifications, experience, projects,
                  certifications, or skills.
                """.formatted(
                resumeSkills,
                jobTitle,
                requiredSkills
        );

        String aiResponse;

        try {

            aiResponse =
                    geminiService.analyzeWithGemini(prompt);

        } catch (GeminiQuotaException e) {

            return createFallbackAnalysis(
                    resumeSkills,
                    jobTitle,
                    requiredSkills
            );
        }

        AnalysisResult result =
                new AnalysisResult();

        result.setMatchPercentage(
                calculateMatchPercentage(
                        resumeSkills,
                        requiredSkills
                )
        );

        result.setMatchedSkills(
                extractJsonArray(
                        aiResponse,
                        "matchedSkills"
                )
        );

        result.setMissingSkills(
                extractJsonArray(
                        aiResponse,
                        "missingSkills"
                )
        );

        result.setResumeGaps(
                extractJsonArray(
                        aiResponse,
                        "resumeGaps"
                )
        );

        result.setRecommendations(
                extractJsonArray(
                        aiResponse,
                        "recommendations"
                )
        );

        result.setProjectRecommendations(
                extractJsonArray(
                        aiResponse,
                        "projectRecommendations"
                )
        );

        result.setResumeImprovements(
                extractJsonArray(
                        aiResponse,
                        "resumeImprovements"
                )
        );

        result.setRoadmap(
                extractRoadmap(
                        aiResponse,
                        jobTitle
                )
        );

        return result;
    }

    private Roadmap extractRoadmap(
            String json,
            String fallbackRole) {

        Roadmap roadmap =
                new Roadmap();

        String targetRole =
                extractString(
                        json,
                        "targetRole"
                );

        if (targetRole.isEmpty()) {
            targetRole = fallbackRole;
        }

        roadmap.setTargetRole(targetRole);

        roadmap.setPhases(
                extractRoadmapPhases(json)
        );

        return roadmap;
    }

    private List<RoadmapPhase> extractRoadmapPhases(
            String json) {

        List<RoadmapPhase> phases =
                new ArrayList<>();

        int roadmapStart =
                json.indexOf("\"roadmap\"");

        if (roadmapStart == -1) {
            return phases;
        }

        int phasesStart =
                json.indexOf(
                        "\"phases\"",
                        roadmapStart
                );

        if (phasesStart == -1) {
            return phases;
        }

        int arrayStart =
                json.indexOf(
                        "[",
                        phasesStart
                );

        int arrayEnd =
                findArrayEnd(
                        json,
                        arrayStart
                );

        if (arrayStart == -1 ||
                arrayEnd == -1) {

            return phases;
        }

        String phasesText =
                json.substring(
                        arrayStart + 1,
                        arrayEnd
                );

        List<String> objects =
                splitJsonObjects(phasesText);

        for (String object : objects) {

            RoadmapPhase phase =
                    new RoadmapPhase();

            phase.setPhase(
                    extractString(
                            object,
                            "phase"
                    )
            );

            phase.setTitle(
                    extractString(
                            object,
                            "title"
                    )
            );

            phase.setDuration(
                    extractString(
                            object,
                            "duration"
                    )
            );

            phase.setTopics(
                    extractJsonArray(
                            object,
                            "topics"
                    )
            );

            phase.setTasks(
                    extractJsonArray(
                            object,
                            "tasks"
                    )
            );

            phases.add(phase);
        }

        return phases;
    }

    private List<String> splitJsonObjects(
            String text) {

        List<String> objects =
                new ArrayList<>();

        boolean insideString = false;
        boolean escaped = false;

        int depth = 0;
        int objectStart = -1;

        for (int i = 0;
             i < text.length();
             i++) {

            char current =
                    text.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (current == '\\' &&
                    insideString) {

                escaped = true;
                continue;
            }

            if (current == '"') {

                insideString =
                        !insideString;

                continue;
            }

            if (insideString) {
                continue;
            }

            if (current == '{') {

                if (depth == 0) {
                    objectStart = i;
                }

                depth++;
            }

            if (current == '}') {

                depth--;

                if (depth == 0 &&
                        objectStart != -1) {

                    objects.add(
                            text.substring(
                                    objectStart,
                                    i + 1
                            )
                    );

                    objectStart = -1;
                }
            }
        }

        return objects;
    }

    private String extractString(
            String json,
            String fieldName) {

        String marker =
                "\"" + fieldName + "\"";

        int fieldStart =
                json.indexOf(marker);

        if (fieldStart == -1) {
            return "";
        }

        int colon =
                json.indexOf(
                        ":",
                        fieldStart
                );

        if (colon == -1) {
            return "";
        }

        int firstQuote =
                json.indexOf(
                        "\"",
                        colon + 1
                );

        if (firstQuote == -1) {
            return "";
        }

        StringBuilder result =
                new StringBuilder();

        boolean escaped = false;

        for (int i = firstQuote + 1;
             i < json.length();
             i++) {

            char current =
                    json.charAt(i);

            if (escaped) {

                switch (current) {

                    case 'n' ->
                            result.append('\n');

                    case 'r' ->
                            result.append('\r');

                    case 't' ->
                            result.append('\t');

                    case '"' ->
                            result.append('"');

                    case '\\' ->
                            result.append('\\');

                    default ->
                            result.append(current);
                }

                escaped = false;

            } else if (current == '\\') {

                escaped = true;

            } else if (current == '"') {

                break;

            } else {

                result.append(current);
            }
        }

        return result.toString().trim();
    }

    private List<String> extractJsonArray(
            String json,
            String fieldName) {

        List<String> items =
                new ArrayList<>();

        String marker =
                "\"" + fieldName + "\"";

        int fieldStart =
                json.indexOf(marker);

        if (fieldStart == -1) {
            return items;
        }

        int arrayStart =
                json.indexOf(
                        "[",
                        fieldStart
                );

        int arrayEnd =
                findArrayEnd(
                        json,
                        arrayStart
                );

        if (arrayStart == -1 ||
                arrayEnd == -1) {

            return items;
        }

        String content =
                json.substring(
                        arrayStart + 1,
                        arrayEnd
                );

        boolean insideString = false;
        boolean escaped = false;

        StringBuilder currentItem =
                new StringBuilder();

        for (int i = 0;
             i < content.length();
             i++) {

            char current =
                    content.charAt(i);

            if (escaped) {

                switch (current) {

                    case 'n' ->
                            currentItem.append('\n');

                    case 'r' ->
                            currentItem.append('\r');

                    case 't' ->
                            currentItem.append('\t');

                    case '"' ->
                            currentItem.append('"');

                    case '\\' ->
                            currentItem.append('\\');

                    default ->
                            currentItem.append(current);
                }

                escaped = false;

                continue;
            }

            if (current == '\\' &&
                    insideString) {

                escaped = true;

                continue;
            }

            if (current == '"') {

                insideString =
                        !insideString;

                continue;
            }

            if (current == ',' &&
                    !insideString) {

                addItem(
                        items,
                        currentItem
                );

                currentItem.setLength(0);

                continue;
            }

            currentItem.append(current);
        }

        addItem(
                items,
                currentItem
        );

        return items;
    }

    private void addItem(
            List<String> result,
            StringBuilder item) {

        String cleanItem =
                item.toString().trim();

        if (!cleanItem.isEmpty()) {
            result.add(cleanItem);
        }
    }

    private int findArrayEnd(
            String json,
            int arrayStart) {

        if (arrayStart == -1) {
            return -1;
        }

        boolean insideString = false;
        boolean escaped = false;

        int depth = 0;

        for (int i = arrayStart;
             i < json.length();
             i++) {

            char current =
                    json.charAt(i);

            if (escaped) {

                escaped = false;

                continue;
            }

            if (current == '\\' &&
                    insideString) {

                escaped = true;

                continue;
            }

            if (current == '"') {

                insideString =
                        !insideString;

                continue;
            }

            if (!insideString) {

                if (current == '[') {
                    depth++;
                }

                if (current == ']') {

                    depth--;

                    if (depth == 0) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    private int calculateMatchPercentage(
            String resumeSkills,
            String requiredSkills) {

        String[] required =
                requiredSkills.split(",");

        String candidateSkills =
                resumeSkills.toLowerCase();

        int matched = 0;

        for (String skill : required) {

            if (candidateSkills.contains(
                    skill.trim().toLowerCase()
            )) {

                matched++;
            }
        }

        if (required.length == 0) {
            return 0;
        }

        return (matched * 100) /
                required.length;
    }

    private AnalysisResult createFallbackAnalysis(
            String resumeSkills,
            String jobTitle,
            String requiredSkills) {

        AnalysisResult result =
                new AnalysisResult();

        String candidateSkills =
                resumeSkills.toLowerCase();

        String[] required =
                requiredSkills.split(",");

        List<String> matched =
                new ArrayList<>();

        List<String> missing =
                new ArrayList<>();

        for (String skill : required) {

            String cleanSkill =
                    skill.trim();

            if (cleanSkill.isEmpty()) {
                continue;
            }

            if (candidateSkills.contains(
                    cleanSkill.toLowerCase()
            )) {

                matched.add(cleanSkill);

            } else {

                missing.add(cleanSkill);
            }
        }

        result.setMatchPercentage(
                calculateMatchPercentage(
                        resumeSkills,
                        requiredSkills
                )
        );

        result.setMatchedSkills(matched);
        result.setMissingSkills(missing);

        List<String> resumeGaps =
                new ArrayList<>();

        List<String> recommendations =
                new ArrayList<>();

        if (!missing.isEmpty()) {

            resumeGaps.add(
                    "The resume does not clearly demonstrate " +
                    "the following target-job skills: " +
                    String.join(", ", missing) + "."
            );

            for (String skill : missing) {

                recommendations.add(
                        "Learn " + skill +
                        " and build a practical project " +
                        "that demonstrates it."
                );
            }

        } else {

            recommendations.add(
                    "Your listed skills cover the current " +
                    "job requirements. Strengthen your profile " +
                    "with practical projects, measurable achievements, " +
                    "and strong GitHub evidence."
            );
        }

        if (resumeSkills.length() < 100) {

            resumeGaps.add(
                    "The provided profile contains limited " +
                    "detail about projects and practical experience."
            );

            recommendations.add(
                    "Add measurable project details, " +
                    "technologies used, and GitHub links."
            );
        }

        result.setResumeGaps(resumeGaps);
        result.setRecommendations(recommendations);

        result.setProjectRecommendations(
                createFallbackProjects(
                        jobTitle,
                        missing
                )
        );

        result.setResumeImprovements(
                createFallbackResumeImprovements(
                        missing
                )
        );

        result.setRoadmap(
                createFallbackRoadmap(
                        jobTitle,
                        missing,
                        matched
                )
        );

        return result;
    }

    private List<String> createFallbackResumeImprovements(
            List<String> missingSkills) {

        List<String> improvements =
                new ArrayList<>();

        if (!missingSkills.isEmpty()) {

            improvements.add(
                    "Add practical project evidence for " +
                    String.join(", ", missingSkills) +
                    " after building projects that demonstrate these skills."
            );

            improvements.add(
                    "Update the Technical Skills section with " +
                    "only the target technologies you can demonstrate " +
                    "through projects or practical work."
            );
        }

        improvements.add(
                "Rewrite project descriptions using action verbs, " +
                "technologies used, and measurable results where available."
        );

        improvements.add(
                "Add GitHub repository links for relevant projects " +
                "so recruiters can verify your implementation."
        );

        improvements.add(
                "Keep the resume focused on skills and projects " +
                "that directly relate to the target job."
        );

        return improvements;
    }

    private List<String> createFallbackProjects(
            String jobTitle,
            List<String> missingSkills) {

        List<String> projects =
                new ArrayList<>();

        String missing =
                String.join(
                        ", ",
                        missingSkills
                );

        if (missingSkills.isEmpty()) {

            projects.add(
                    "Java Backend API: Build a production-style " +
                    "backend application for " + jobTitle +
                    " using Java, Spring Boot, MySQL, and REST APIs."
            );

            projects.add(
                    "Job Application Tracker: Create a full CRUD " +
                    "application with authentication, database " +
                    "integration, and REST endpoints."
            );

            projects.add(
                    "Developer Portfolio API: Build a backend service " +
                    "that manages projects, skills, and achievements " +
                    "with documented REST endpoints."
            );

        } else {

            projects.add(
                    "Career Skill Tracker: Build a Spring Boot and " +
                    "MySQL application that tracks learning progress " +
                    "for " + missing + "."
            );

            projects.add(
                    "Student Management REST API: Build a complete " +
                    "CRUD backend using Java, Spring Boot, REST APIs, " +
                    "and MySQL."
            );

            projects.add(
                    "Job Application Tracker API: Build a backend " +
                    "application with REST endpoints, MySQL " +
                    "persistence, validation, and API testing."
            );
        }

        return projects;
    }

    private Roadmap createFallbackRoadmap(
            String jobTitle,
            List<String> missingSkills,
            List<String> matchedSkills) {

        Roadmap roadmap =
                new Roadmap();

        roadmap.setTargetRole(jobTitle);

        List<RoadmapPhase> phases =
                new ArrayList<>();

        RoadmapPhase phase1 =
                new RoadmapPhase();

        phase1.setPhase("Phase 1");
        phase1.setTitle(
                missingSkills.isEmpty()
                        ? "Strengthen the technical foundation"
                        : "Close the skill gaps"
        );
        phase1.setDuration("2 weeks");

        phase1.setTopics(
                missingSkills.isEmpty()
                        ? new ArrayList<>(
                                List.of(
                                        "Core job skills",
                                        "Framework concepts",
                                        "API fundamentals"
                                )
                        )
                        : new ArrayList<>(missingSkills)
        );

        phase1.setTasks(
                missingSkills.isEmpty()
                        ? List.of(
                                "Review the core technologies required for the role",
                                "Practice coding and backend exercises",
                                "Build small examples using the target technologies"
                        )
                        : List.of(
                                "Study the missing skills",
                                "Practice small coding exercises",
                                "Build one small example for each missing skill"
                        )
        );

        phases.add(phase1);

        RoadmapPhase phase2 =
                new RoadmapPhase();

        phase2.setPhase("Phase 2");
        phase2.setTitle(
                "Build a job-ready project"
        );
        phase2.setDuration("2 weeks");

        phase2.setTopics(
                List.of(
                        "Project architecture",
                        "REST API development",
                        "Database integration",
                        "Validation and error handling"
                )
        );

        phase2.setTasks(
                List.of(
                        "Build a complete backend project using Java and Spring Boot",
                        "Create REST endpoints and connect the application to MySQL",
                        "Add validation and meaningful error responses",
                        "Push the project to GitHub with a clear README"
                )
        );

        phases.add(phase2);

        RoadmapPhase phase3 =
                new RoadmapPhase();

        phase3.setPhase("Phase 3");
        phase3.setTitle(
                "Build interview-ready evidence"
        );
        phase3.setDuration("1 week");

        phase3.setTopics(
                List.of(
                        "DSA practice",
                        "Java and OOP interview questions",
                        "SQL questions",
                        "Project explanation"
                )
        );

        phase3.setTasks(
                List.of(
                        "Solve role-relevant DSA problems",
                        "Practice Java, OOP, SQL, and REST API questions",
                        "Prepare a clear 2-minute explanation of your project",
                        "Prepare answers for technical decisions made in the project"
                )
        );

        phases.add(phase3);

        RoadmapPhase phase4 =
                new RoadmapPhase();

        phase4.setPhase("Phase 4");
        phase4.setTitle(
                "Strengthen the resume and portfolio"
        );
        phase4.setDuration("1 week");

        phase4.setTopics(
                List.of(
                        "Resume optimization",
                        "Project achievements",
                        "GitHub profile",
                        "Interview preparation"
                )
        );

        phase4.setTasks(
                List.of(
                        "Rewrite project bullets with action verbs and measurable impact",
                        "Add relevant GitHub links to the resume",
                        "Improve the project README with setup and API documentation",
                        "Practice explaining how your project solves a real problem"
                )
        );

        phases.add(phase4);

        roadmap.setPhases(phases);

        return roadmap;
    }
}