package provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import model.Job;
import model.Skill;
import model.User;

/**
 * Provides a centralized catalog of all available jobs in the game.
 */
public class JobProvider {

    private static final List<Job> ALL_JOBS = new ArrayList<>();

    static {
        // To add a new job, just add a new line here.

        // --- Typist Jobs ---
        ALL_JOBS.add(new Job("Transcribe Audio Clip", "Transcribe a short audio file.", 15, 1000, "Typist", Collections.emptyList()));
        ALL_JOBS.add(new Job("Basic Data Entry", "Enter data from a scanned document.", 50, 1200, "Typist", List.of("Typing Proficiency")));
        ALL_JOBS.add(new Job("Write a Short Article", "Write a 500-word article on a simple topic.", 150, 1800, "Typist", List.of("Content Writing")));
        ALL_JOBS.add(new Job("Proofread a Document", "Review and correct a 10-page document for errors.", 300, 2500, "Typist", List.of("Typing Proficiency", "Content Writing")));

        // --- Logo Designer Jobs ---
        ALL_JOBS.add(new Job("Simple Logo Design", "Design a basic logo for a startup.", 25, 1500, "LogoDesigner", Collections.emptyList()));
        ALL_JOBS.add(new Job("Business Card Design", "Design a professional business card.", 100, 1800, "LogoDesigner", List.of("Basic Graphic Design")));
        ALL_JOBS.add(new Job("Vectorize an Image", "Convert a raster image to a scalable vector graphic.", 200, 2200, "LogoDesigner", List.of("Basic Graphic Design")));
        ALL_JOBS.add(new Job("Website Mockup", "Design the layout for a new website.", 800, 4000, "LogoDesigner", List.of("Basic Graphic Design", "Web Development Basics")));

        // --- Programmer Jobs ---
        ALL_JOBS.add(new Job("Fix a CSS Bug", "Fix a simple CSS layout issue.", 250, 5000, "Programmer", Collections.emptyList()));
        ALL_JOBS.add(new Job("Build a Static Web Page", "Create a single-page static website from a design.", 1000, 8000, "Programmer", List.of("Web Development Basics")));
        ALL_JOBS.add(new Job("SEO Audit", "Analyze a website and provide SEO recommendations.", 2500, 10000, "Programmer", List.of("Web Development Basics", "Advanced SEO")));
        ALL_JOBS.add(new Job("Build a Small Company Website", "Develop a multi-page website for a client.", 8000, 15000, "Programmer", List.of("Web Development Basics", "Project Management")));

        // --- Doctor Jobs ---
        ALL_JOBS.add(new Job("Online Consultation", "Provide a medical consultation via video call.", 500, 15000, "Doctor", Collections.emptyList()));
        ALL_JOBS.add(new Job("Write Medical Article", "Write an article for a health blog.", 1500, 16000, "Doctor", List.of("Content Writing")));
        ALL_JOBS.add(new Job("Review Medical Records", "Review a patient's medical history for a second opinion.", 2500, 18000, "Doctor", List.of("Typing Proficiency"))); // Requires careful documentation
        ALL_JOBS.add(new Job("Manage a Health Seminar", "Organize and lead an online health seminar.", 10000, 25000, "Doctor", List.of("Project Management", "Content Writing")));
    }

    /** Get the list of jobs available for the user */
    public static List<Job> getAvailableJobsForUser(User user) {
        final List<String> userSkills = user.getSkills().stream().map(Skill::getName).toList();
        final String userIdentity = user.getIdentity().getClass().getSimpleName();

        return ALL_JOBS.stream()
                .filter(job -> job.isAvailableFor(userIdentity, userSkills))
                .collect(Collectors.toList());
    }
}
