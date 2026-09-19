import React from "react";
import { useNavigate } from "react-router-dom";
import "./Home.css";

function Home() {
  const navigate = useNavigate();

  const goToAnalysis = () => {
    navigate("/analysis");
  };

  return (
    <div className="home-page">
      {/* Navigation */}
      <header className="home-navbar">
        <div className="home-nav-inner">
          <button
            className="home-brand"
            onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
          >
            <span className="brand-name">SkillBridge AI</span>
            <span className="brand-tagline">Bridge the gap. Build the career.</span>
          </button>

          <nav className="home-nav-links">
            <a href="#how-it-works">How it works</a>
            <a href="#features">Features</a>
            <button onClick={goToAnalysis}>Get Started</button>
          </nav>
        </div>
      </header>

      {/* Hero */}
      <main>
        <section className="hero-section">
          <div className="hero-inner">
            <div className="hero-content">
              <div className="hero-badge">
                <span className="badge-dot"></span>
                AI-Powered Career Guidance
              </div>

              <h1>
                Bridge the gap between your skills and your{" "}
                <span>dream career.</span>
              </h1>

              <p className="hero-description">
                SkillBridge AI analyzes your resume against your target job,
                identifies skill gaps, and creates a personalized roadmap to
                help you become job-ready.
              </p>

              <div className="hero-actions">
                <button className="primary-button" onClick={goToAnalysis}>
                  Analyze My Resume <span>→</span>
                </button>

                <a className="secondary-button" href="#how-it-works">
                  See How It Works
                </a>
              </div>

              <div className="hero-highlights">
                <span>Resume Analysis</span>
                <span>Skill Gaps</span>
                <span>Roadmap</span>
                <span>Projects</span>
              </div>
            </div>

            {/* Career Analysis Preview */}
            <div className="analysis-preview-wrapper">
              <div className="analysis-preview">
                <div className="preview-top">
                  <div>
                    <span className="preview-label">CAREER ANALYSIS</span>
                    <h3>Java Backend Developer</h3>
                  </div>

                  <div className="ai-status">
                    <span></span>
                    AI Analysis
                  </div>
                </div>

                <div className="preview-match">
                  <div className="preview-score">
                    <strong>72%</strong>
                    <span>Match</span>
                  </div>

                  <div className="preview-match-text">
                    <span>Based on your resume &amp; target role</span>
                    <div className="match-line">
                      <div className="match-line-fill"></div>
                    </div>
                  </div>
                </div>

                <div className="preview-divider"></div>

                <div className="preview-skills">
                  <div className="skill-column">
                    <span className="skill-heading">Skills matched</span>

                    <div className="skill-pills">
                      <span>Java</span>
                      <span>SQL</span>
                      <span>Git</span>
                      <span>OOP</span>
                    </div>
                  </div>

                  <div className="skill-column">
                    <span className="skill-heading">Skills to develop</span>

                    <div className="missing-skills">
                      <span>Spring Boot</span>
                      <span>REST APIs</span>
                    </div>
                  </div>
                </div>

                <div className="preview-footer">
                  <span>Skill gap identified</span>
                  <span className="footer-arrow">→</span>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Features */}
        <section className="features-section" id="features">
          <div className="section-inner">
            <div className="section-heading">
              <span className="section-label">ONE PLATFORM</span>
              <h2>Everything you need to become job-ready.</h2>
              <p>
                From understanding your current profile to building the skills
                your target role requires.
              </p>
            </div>

            <div className="feature-grid">
              <article className="feature-card">
                <div className="feature-number">01</div>
                <div className="feature-icon">✦</div>
                <h3>AI Resume Analysis</h3>
                <p>
                  Understand how closely your current profile matches your
                  target role.
                </p>
              </article>

              <article className="feature-card">
                <div className="feature-number">02</div>
                <div className="feature-icon">◈</div>
                <h3>Skill Gap Detection</h3>
                <p>
                  Discover the technical skills and evidence missing from your
                  profile.
                </p>
              </article>

              <article className="feature-card">
                <div className="feature-number">03</div>
                <div className="feature-icon">↗</div>
                <h3>Personalized Roadmap</h3>
                <p>
                  Get a practical learning path based on your current skills
                  and career goal.
                </p>
              </article>

              <article className="feature-card">
                <div className="feature-number">04</div>
                <div className="feature-icon">◇</div>
                <h3>Project Recommendations</h3>
                <p>
                  Build relevant projects that strengthen the gaps identified
                  in your profile.
                </p>
              </article>
            </div>
          </div>
        </section>

        {/* How it works */}
        <section className="workflow-section" id="how-it-works">
          <div className="section-inner">
            <div className="section-heading workflow-heading">
              <span className="section-label">HOW IT WORKS</span>
              <h2>From profile to career roadmap.</h2>
              <p>
                A simple four-step process that turns your current profile into
                an actionable career plan.
              </p>
            </div>

            <div className="workflow-grid">
              <div className="workflow-step">
                <span className="workflow-number">01</span>
                <div className="workflow-line"></div>
                <h3>Upload</h3>
                <p>Upload your resume and enter your target job.</p>
              </div>

              <div className="workflow-step">
                <span className="workflow-number">02</span>
                <div className="workflow-line"></div>
                <h3>Analyze</h3>
                <p>AI compares your profile with the target role.</p>
              </div>

              <div className="workflow-step">
                <span className="workflow-number">03</span>
                <div className="workflow-line"></div>
                <h3>Improve</h3>
                <p>Follow your roadmap and strengthen your profile.</p>
              </div>

              <div className="workflow-step">
                <span className="workflow-number">04</span>
                <div className="workflow-line"></div>
                <h3>Re-analyze</h3>
                <p>Measure your progress and identify what remains.</p>
              </div>
            </div>

            <div className="final-cta">
              <div>
                <span className="final-cta-label">READY TO START?</span>
                <h3>Turn your career gap into a career plan.</h3>
              </div>

              <button onClick={goToAnalysis}>
                Analyze My Career Fit <span>→</span>
              </button>
            </div>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="home-footer">
        <div className="footer-inner">
          <div>
            <div className="footer-brand">SkillBridge AI</div>
            <p>Bridge the gap. Build the career.</p>
          </div>

          <div className="footer-right">
            AI-powered career guidance
          </div>
        </div>
      </footer>
    </div>
  );
}

export default Home;