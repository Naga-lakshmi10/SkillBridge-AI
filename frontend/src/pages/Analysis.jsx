
import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./Analysis.css";

function Analysis() {
  const [resumeText, setResumeText] = useState("");
  const [fileName, setFileName] = useState("");
  const [uploading, setUploading] = useState(false);

  const [jobTitle, setJobTitle] = useState("");
  const [jobDescription, setJobDescription] = useState("");

  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  /* ================================
     PDF UPLOAD
  ================================= */

  const handleUpload = async (event) => {
    const file = event.target.files?.[0];

    if (!file) {
      return;
    }

    if (!file.name.toLowerCase().endsWith(".pdf")) {
      alert("Please upload a PDF resume.");
      event.target.value = "";
      return;
    }

    if (file.size > 10 * 1024 * 1024) {
      alert("Please upload a PDF smaller than 10MB.");
      event.target.value = "";
      return;
    }

    setUploading(true);
    setFileName(file.name);
    setResumeText("");
    setResult(null);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(
        "http://localhost:8080/api/pdf/extract",
        {
          method: "POST",
          body: formData,
        }
      );

      const extractedText = await response.text();

      if (!response.ok) {
        throw new Error(
          extractedText || "Could not extract the PDF."
        );
      }

      if (!extractedText.trim()) {
        throw new Error(
          "No readable text was found in the PDF."
        );
      }

      setResumeText(extractedText);
    } catch (error) {
      console.error("PDF upload error:", error);

      setFileName("");
      setResumeText("");

      alert(
        error.message ||
          "Could not extract your resume. Please make sure the backend is running."
      );
    } finally {
      setUploading(false);
    }
  };

  /* ================================
     ANALYZE
  ================================= */

  const handleAnalyze = async () => {
    if (!resumeText.trim()) {
      alert("Please upload your resume PDF first.");
      return;
    }

    if (!jobTitle.trim()) {
      alert("Please enter your target job role.");
      return;
    }

    if (!jobDescription.trim()) {
      alert(
        "Please enter the job description or required skills."
      );
      return;
    }

    setLoading(true);

    const requestBody = {
      resume: {
        name: "Candidate",
        email: "",
        phone: "",
        education: resumeText,
        skills: resumeText,
        projects: resumeText,
        experience: resumeText,
      },

      jobDescription: {
        jobTitle: jobTitle.trim(),
        company: "",
        description: jobDescription.trim(),
        requiredSkills: jobDescription.trim(),
      },
    };

    try {
      const response = await fetch(
        "http://localhost:8080/api/analysis",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(requestBody),
        }
      );

      const rawData = await response.text();

      let data;

      try {
        data = JSON.parse(rawData);
      } catch {
        data = rawData;
      }

      if (!response.ok) {
        throw new Error(
          typeof data === "string"
            ? data
            : "Analysis failed."
        );
      }

      setResult(data);
    } catch (error) {
      console.error("Analysis error:", error);

      alert(
        error.message ||
          "Something went wrong while analyzing your profile."
      );
    } finally {
      setLoading(false);
    }
  };

  /* ================================
     RE-ANALYZE
  ================================= */

  const handleReAnalyze = () => {
    handleAnalyze();
  };

  /* ================================
     HELPERS
  ================================= */

  const getProjectParts = (project) => {
    const parts = String(project).split(":");

    return {
      title: parts[0]?.trim() || "Recommended Project",
      description:
        parts.length > 1
          ? parts.slice(1).join(":").trim()
          : "Build this project to demonstrate relevant job-ready skills.",
    };
  };

  const getMatchMessage = (percentage) => {
    if (percentage >= 80) {
      return "Strong alignment with the target role.";
    }

    if (percentage >= 60) {
      return "Good foundation with a few areas to strengthen.";
    }

    if (percentage >= 40) {
      return "Some relevant skills are present, but several gaps remain.";
    }

    return "Your profile needs more preparation for this target role.";
  };

  const getMatchClass = (percentage) => {
    if (percentage >= 80) {
      return "strong";
    }

    if (percentage >= 60) {
      return "moderate";
    }

    return "developing";
  };

  return (
    <div className="analysis-page">

      {/* HEADER */}

      <header className="analysis-header">
        <span>AI CAREER ANALYSIS</span>

        <h1>Analyze your career fit</h1>

        <p>
          Upload your resume, enter your target role, and
          let SkillBridge AI identify the skills you already
          have and the gaps you need to close.
        </p>

        <Link
          to="/"
          style={{
            display: "inline-block",
            marginTop: "18px",
            color: "#5b5ff0",
            textDecoration: "none",
            fontSize: "13px",
            fontWeight: "700",
          }}
        >
          ← Back to Home
        </Link>
      </header>

      {/* MAIN */}

      <main className="analysis-container">

        {/* LEFT — USER INPUT */}

        <section className="input-card">

          <h2>Your Profile</h2>

          <p className="card-description">
            Provide your resume and target job information.
          </p>

          <label>Resume PDF</label>

          <div className="upload-box">

            <input
              type="file"
              accept=".pdf,application/pdf"
              onChange={handleUpload}
            />

            <div className="upload-status">
              {uploading
                ? "Extracting your resume..."
                : fileName
                ? `Selected: ${fileName}`
                : "Choose your PDF resume to begin."}
            </div>

            {fileName && resumeText && !uploading && (
              <div className="upload-status success">
                ✓ Resume uploaded and extracted successfully.
              </div>
            )}

          </div>

          {resumeText && (
            <details className="extracted-resume">

              <summary>
                View extracted resume text
              </summary>

              <div className="extracted-resume-text">
                {resumeText}
              </div>

            </details>
          )}

          <label htmlFor="job-title">
            Target Job Role
          </label>

          <input
            id="job-title"
            type="text"
            placeholder="e.g. Java Backend Developer"
            value={jobTitle}
            onChange={(event) =>
              setJobTitle(event.target.value)
            }
          />

          <label htmlFor="job-description">
            Job Description / Required Skills
          </label>

          <textarea
            id="job-description"
            rows="9"
            placeholder={`Paste the job description or required skills...

Example:
Java, Spring Boot, REST APIs, MySQL, SQL, Git, OOP

Develop backend services, build REST APIs,
work with databases and write clean code.`}
            value={jobDescription}
            onChange={(event) =>
              setJobDescription(event.target.value)
            }
          />

          <button
            className="analyze-button"
            onClick={handleAnalyze}
            disabled={loading || uploading}
          >
            {loading
              ? "Analyzing..."
              : result
              ? "Re-analyze My Profile →"
              : "Analyze My Career Fit →"}
          </button>

          {loading && (
            <div className="analysis-loading">
              AI is analyzing your profile...
            </div>
          )}

        </section>

        {/* RIGHT — AI RESULTS */}

        <section className="result-card">

          {!result ? (

            /* EMPTY STATE */

            <div className="empty-result">

              <div className="empty-icon">
                ✦
              </div>

              <span className="empty-label">
                AI CAREER RESULTS
              </span>

              <h2>
                Your career analysis
                <br />
                will appear here.
              </h2>

              <p>
                Upload your resume and enter your target role.
                SkillBridge AI will analyze your profile and
                show your career match, skill gaps, resume
                gaps, recommendations, projects, and
                personalized roadmap.
              </p>

              <div className="preview-items">
                <span>Career Match</span>
                <span>Skill Gaps</span>
                <span>Resume Gaps</span>
                <span>Roadmap</span>
              </div>

            </div>

          ) : (

            /* RESULTS */

            <div>

              {/* CAREER MATCH */}

              <div className="result-top">

                <div className="career-match-content">

                  <span className="result-label">
                    CAREER MATCH
                  </span>

                  <h2>
                    {jobTitle}
                  </h2>

                  <p>
                    Based on your resume and target
                    job requirements.
                  </p>

                  <div
                    className={`match-status ${getMatchClass(
                      result.matchPercentage || 0
                    )}`}
                  >
                    <span className="match-status-dot">
                      ●
                    </span>

                    {getMatchMessage(
                      result.matchPercentage || 0
                    )}
                  </div>

                  <div className="match-meta">

                    <span>
                      <strong>
                        {result.matchedSkills?.length || 0}
                      </strong>{" "}
                      matched skills
                    </span>

                    <span>
                      <strong>
                        {result.missingSkills?.length || 0}
                      </strong>{" "}
                      missing skills
                    </span>

                  </div>

                </div>

                <div
                  className="match-score"
                  style={{
                    background: `conic-gradient(
                      #5b5ff0 ${
                        Math.max(
                          0,
                          Math.min(
                            100,
                            result.matchPercentage || 0
                          )
                        )
                      }%,
                      #e8eaf2 0
                    )`,
                  }}
                >
                  <div className="match-score-inner">
                    <span>
                      {result.matchPercentage ?? 0}%
                    </span>
                  </div>
                </div>

              </div>

              {/* SUMMARY */}

              <div className="summary-dashboard">

                <div className="summary-card">
                  <span className="summary-number">
                    {result.matchedSkills?.length || 0}
                  </span>

                  <span className="summary-label">
                    Matched Skills
                  </span>
                </div>

                <div className="summary-card">
                  <span className="summary-number">
                    {result.missingSkills?.length || 0}
                  </span>

                  <span className="summary-label">
                    Missing Skills
                  </span>
                </div>

                <div className="summary-card">
                  <span className="summary-number">
                    {result.resumeGaps?.length || 0}
                  </span>

                  <span className="summary-label">
                    Resume Gaps
                  </span>
                </div>

                <div className="summary-card">
                  <span className="summary-number">
                    {result.projectRecommendations?.length || 0}
                  </span>

                  <span className="summary-label">
                    Projects
                  </span>
                </div>

                <div className="summary-card">
                  <span className="summary-number">
                    {result.roadmap?.phases?.length || 0}
                  </span>

                  <span className="summary-label">
                    Roadmap Phases
                  </span>
                </div>

              </div>

              {/* MATCHED SKILLS */}

              <div className="result-section">

                <h3>
                  01. Matched Skills
                </h3>

                <p>
                  Skills from your profile that align
                  with the target role.
                </p>

                <div className="skill-badges">

                  {result.matchedSkills?.length > 0 ? (
                    result.matchedSkills.map(
                      (skill, index) => (
                        <span
                          className="skill-badge matched"
                          key={`matched-${index}`}
                        >
                          ✓ {skill}
                        </span>
                      )
                    )
                  ) : (
                    <p>
                      No matched skills detected.
                    </p>
                  )}

                </div>

              </div>

              {/* MISSING SKILLS */}

              <div className="result-section missing-skills-section">

                <h3>
                  02. Missing Skills
                </h3>

                <p>
                  Skills that could strengthen your fit
                  for the target role. Use these gaps to
                  decide what to learn and practice next.
                </p>

                {result.missingSkills?.length > 0 ? (

                  <div className="missing-skills-list">

                    {result.missingSkills.map(
                      (skill, index) => (

                        <div
                          className="missing-skill-card"
                          key={`missing-${index}`}
                        >

                          <div className="missing-skill-header">

                            <div className="missing-skill-number">
                              {String(index + 1).padStart(2, "0")}
                            </div>

                            <div className="missing-skill-title">

                              <h4>
                                {skill}
                              </h4>

                              <span>
                                Skill gap identified
                              </span>

                            </div>

                            <div className="missing-skill-badge">
                              Priority
                            </div>

                          </div>

                          <div className="missing-skill-body">

                            <div className="missing-skill-column">

                              <span className="missing-skill-label">
                                WHY IT MATTERS
                              </span>

                              <p>
                                {skill} is relevant to the
                                target role and can strengthen
                                your ability to meet the job
                                requirements.
                              </p>

                            </div>

                            <div className="missing-skill-column">

                              <span className="missing-skill-label">
                                WHAT TO DO
                              </span>

                              <p>
                                Learn the fundamentals of {skill},
                                practice with small exercises,
                                and apply it in a practical
                                project.
                              </p>

                            </div>

                          </div>

                          <div className="missing-skill-action">

                            <span>
                              NEXT STEP
                            </span>

                            <strong>
                              Add {skill} to your learning
                              roadmap and demonstrate it
                              through a project.
                            </strong>

                          </div>

                        </div>

                      )
                    )}

                  </div>

                ) : (

                  <div className="no-skill-gaps">

                    <span>
                      ✓
                    </span>

                    <div>

                      <strong>
                        No major skill gaps detected.
                      </strong>

                      <p>
                        Your current skills appear aligned
                        with the requirements you provided.
                      </p>

                    </div>

                  </div>

                )}

              </div>

              {/* RESUME GAPS */}

              <div className="result-section">

                <h3>
                  03. Resume Gaps
                </h3>

                <p>
                  Areas where your resume could provide
                  stronger evidence.
                </p>

                {result.resumeGaps?.length > 0 ? (
                  <ul className="result-list">
                    {result.resumeGaps.map(
                      (gap, index) => (
                        <li key={`gap-${index}`}>
                          {gap}
                        </li>
                      )
                    )}
                  </ul>
                ) : (
                  <p>
                    ✓ No major resume gaps detected.
                  </p>
                )}

              </div>

              {/* RECOMMENDATIONS */}

              <div className="result-section">

                <h3>
                  04. Recommendations
                </h3>

                <p>
                  Practical actions to improve your
                  career readiness.
                </p>

                {result.recommendations?.length > 0 ? (
                  <ul className="result-list">
                    {result.recommendations.map(
                      (recommendation, index) => (
                        <li
                          key={`recommendation-${index}`}
                        >
                          {recommendation}
                        </li>
                      )
                    )}
                  </ul>
                ) : (
                  <p>
                    No additional recommendations.
                  </p>
                )}

              </div>

              {/* RESUME IMPROVEMENTS */}

              {result.resumeImprovements?.length > 0 && (
                <div className="resume-improvements-section">

                  <div className="result-section">

                    <h3>
                      05. Resume Improvements
                    </h3>

                    <p>
                      Changes you can make to strengthen
                      your resume.
                    </p>

                    <div className="resume-improvements">

                      {result.resumeImprovements.map(
                        (item, index) => (
                          <div
                            className="improvement-card"
                            key={`improvement-${index}`}
                          >

                            <div className="improvement-number">
                              {String(index + 1).padStart(2, "0")}
                            </div>

                            <div className="improvement-content">
                              <p>
                                {item}
                              </p>
                            </div>

                          </div>
                        )
                      )}

                    </div>

                  </div>

                </div>
              )}

              {/* PROJECTS */}

              {result.projectRecommendations?.length > 0 && (
                <div className="projects-section">

                  <div className="result-section">

                    <h3>
                      06. Recommended Projects
                    </h3>

                    <p className="section-description">
                      Projects that can help demonstrate
                      the skills you need.
                    </p>

                    <div className="project-recommendations">

                      {result.projectRecommendations.map(
                        (project, index) => {
                          const {
                            title,
                            description,
                          } = getProjectParts(project);

                          return (
                            <div
                              className="project-card"
                              key={`project-${index}`}
                            >

                              <div className="project-number">
                                {String(index + 1).padStart(2, "0")}
                              </div>

                              <div className="project-content">

                                <h4>
                                  {title}
                                </h4>

                                <span>
                                  {description}
                                </span>

                              </div>

                            </div>
                          );
                        }
                      )}

                    </div>

                  </div>

                </div>
              )}

              {/* ROADMAP */}

              {result.roadmap?.phases?.length > 0 && (
                <div className="roadmap-section">

                  <div className="result-section">

                    <h3>
                      07. Personalized Roadmap
                    </h3>

                    <p>
                      A practical path from your current
                      profile toward your target role.
                    </p>

                    <div className="roadmap-container">

                      {result.roadmap.phases.map(
                        (phase, index) => (
                          <div
                            className="roadmap-phase"
                            key={`phase-${index}`}
                          >

                            <div className="roadmap-phase-header">

                              <div>

                                <span className="roadmap-phase-number">
                                  Phase {index + 1}
                                </span>

                                <h4>
                                  {phase.title ||
                                    `Roadmap Phase ${
                                      index + 1
                                    }`}
                                </h4>

                              </div>

                              <span className="roadmap-duration">
                                {phase.duration ||
                                  "Flexible"}
                              </span>

                            </div>

                            {phase.topics?.length > 0 && (
                              <div className="roadmap-topics">

                                <h5>
                                  Topics
                                </h5>

                                <ul>
                                  {phase.topics.map(
                                    (topic, topicIndex) => (
                                      <li
                                        key={`topic-${topicIndex}`}
                                      >
                                        {topic}
                                      </li>
                                    )
                                  )}
                                </ul>

                              </div>
                            )}

                            {phase.tasks?.length > 0 && (
                              <div className="roadmap-tasks">

                                <h5>
                                  Practical Tasks
                                </h5>

                                <ul>
                                  {phase.tasks.map(
                                    (task, taskIndex) => (
                                      <li
                                        key={`task-${taskIndex}`}
                                      >
                                        {task}
                                      </li>
                                    )
                                  )}
                                </ul>

                              </div>
                            )}

                          </div>
                        )
                      )}

                    </div>

                  </div>

                </div>
              )}

              {/* RE-ANALYSIS */}

              <div className="reanalysis-section">

                <h3>
                  Keep improving your career fit
                </h3>

                <p>
                  Improve your skills or update your
                  resume, then analyze again to measure
                  your updated profile.
                </p>

                <button
                  className="reanalyze-button"
                  onClick={handleReAnalyze}
                  disabled={loading || uploading}
                >
                  {loading
                    ? "Analyzing..."
                    : "Re-analyze My Profile →"}
                </button>

              </div>

            </div>
          )}

        </section>

      </main>
    </div>
  );
}

export default Analysis;

