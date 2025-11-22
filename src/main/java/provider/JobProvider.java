package provider;

import java.util.ArrayList;
import java.util.List;

import model.Identity;
import model.career.Job;

// Builds the list of jobs the player can pick from.
// Instance repository (not static holder): the catalog comes from outside,
// so callers depend on an object they can inject/mock (DIP) —
// same pattern as ChallengeService/AchievementService.
public class JobProvider {

    private final ArrayList<Job> allJobs;

    public JobProvider() {
        this(buildDefaultCatalog());
    }

    // Test seam: custom catalog makes every run reproducible.
    public JobProvider(List<Job> jobs) {
        if (jobs == null) {
            throw new IllegalArgumentException("jobs must not be null");
        }
        this.allJobs = new ArrayList<Job>(jobs);
    }

    private static ArrayList<Job> buildDefaultCatalog() {
        ArrayList<Job> jobs = new ArrayList<Job>();
        IdentityProvider identities = new IdentityProvider();
        // 10-hour run tuning: floor raised ~10x so the first skill
        // costs ~2 jobs instead of ~35. Profession gap compressed to
        // ~150$ (start) -> ~3500$ (end) so every path finishes in ~200 jobs.
        // --- Typist Jobs ---
        jobs.add(new Job("Transcribe Audio Clip", "Transcribe a short audio file.", 150, 1000, identities.createIdentity("Typist"), makeEmptyList()));
        jobs.add(new Job("Basic Data Entry", "Enter data from a scanned document.", 300, 1200, identities.createIdentity("Typist"), makeList1("Typing Proficiency")));
        jobs.add(new Job("Write a Short Article", "Write a 500-word article on a simple topic.", 600, 1800, identities.createIdentity("Typist"), makeList1("Content Writing")));
        jobs.add(new Job("Proofread a Document", "Review and correct a 10-page document for errors.", 1200, 2500, identities.createIdentity("Typist"), makeList2("Typing Proficiency", "Content Writing")));

        // --- Logo Designer Jobs ---
        jobs.add(new Job("Simple Logo Design", "Design a basic logo for a startup.", 200, 1500, identities.createIdentity("LogoDesigner"), makeEmptyList()));
        jobs.add(new Job("Business Card Design", "Design a professional business card.", 400, 1800, identities.createIdentity("LogoDesigner"), makeList1("Basic Graphic Design")));
        jobs.add(new Job("Vectorize an Image", "Convert a raster image to a scalable vector graphic.", 700, 2200, identities.createIdentity("LogoDesigner"), makeList1("Basic Graphic Design")));
        jobs.add(new Job("Website Mockup", "Design the layout for a new website.", 1500, 4000, identities.createIdentity("LogoDesigner"), makeList2("Basic Graphic Design", "Web Development Basics")));

        // --- Programmer Jobs ---
        jobs.add(new Job("Fix a CSS Bug", "Fix a simple CSS layout issue.", 400, 5000, identities.createIdentity("Programmer"), makeEmptyList()));
        jobs.add(new Job("Build a Static Web Page", "Create a single-page static website from a design.", 1000, 8000, identities.createIdentity("Programmer"), makeList1("Web Development Basics")));
        jobs.add(new Job("SEO Audit", "Analyze a website and provide SEO recommendations.", 2000, 10000, identities.createIdentity("Programmer"), makeList2("Web Development Basics", "Advanced SEO")));
        jobs.add(new Job("Build a Small Company Website", "Develop a multi-page website for a client.", 3500, 15000, identities.createIdentity("Programmer"), makeList2("Web Development Basics", "Project Management")));

        // --- Doctor Jobs ---
        jobs.add(new Job("Online Consultation", "Provide a medical consultation via video call.", 600, 15000, identities.createIdentity("Doctor"), makeEmptyList()));
        jobs.add(new Job("Write Medical Article", "Write an article for a health blog.", 1200, 16000, identities.createIdentity("Doctor"), makeList1("Content Writing")));
        jobs.add(new Job("Review Medical Records", "Review a patient's medical history for a second opinion.", 2000, 18000, identities.createIdentity("Doctor"), makeList1("Typing Proficiency")));
        jobs.add(new Job("Manage a Health Seminar", "Organize and lead an online health seminar.", 3500, 25000, identities.createIdentity("Doctor"), makeList2("Project Management", "Content Writing")));
        return jobs;
    }

    private static ArrayList<String> makeEmptyList() {
        return new ArrayList<String>();
    }

    private static ArrayList<String> makeList1(String s1) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(s1);
        return list;
    }

    private static ArrayList<String> makeList2(String s1, String s2) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(s1);
        list.add(s2);
        return list;
    }

    // Return a copy so callers can't change the original list.
    public List<Job> getAllJobs() {
        ArrayList<Job> copy = new ArrayList<Job>();
        for (int i = 0; i < allJobs.size(); i++) {
            copy.add(allJobs.get(i));
        }
        return copy;
    }

    // Return jobs for one profession.
    public List<Job> getJobsForIdentity(Identity identity) {
        ArrayList<Job> result = new ArrayList<Job>();
        if (identity == null) {
            return result;
        }
        for (int i = 0; i < allJobs.size(); i++) {
            Job job = allJobs.get(i);
            if (job.isForIdentity(identity)) {
                result.add(job);
            }
        }
        return result;
    }
}
