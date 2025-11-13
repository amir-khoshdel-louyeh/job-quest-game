package provider;

import java.util.ArrayList;
import java.util.List;

import model.Job;
import model.User;

// This class gives us all jobs in the game.
// It is like a catalog: all jobs are stored here in one place.
public class JobProvider {

    // list of all jobs
    private static ArrayList<Job> ALL_JOBS = new ArrayList<Job>();

    // static block runs once when class is loaded, fills the list
    static {
        // --- Typist Jobs ---
        ALL_JOBS.add(new Job("Transcribe Audio Clip", "Transcribe a short audio file.", 15, 1000, "Typist", makeEmptyList()));
        ALL_JOBS.add(new Job("Basic Data Entry", "Enter data from a scanned document.", 50, 1200, "Typist", makeList1("Typing Proficiency")));
        ALL_JOBS.add(new Job("Write a Short Article", "Write a 500-word article on a simple topic.", 150, 1800, "Typist", makeList1("Content Writing")));
        ALL_JOBS.add(new Job("Proofread a Document", "Review and correct a 10-page document for errors.", 300, 2500, "Typist", makeList2("Typing Proficiency", "Content Writing")));

        // --- Logo Designer Jobs ---
        ALL_JOBS.add(new Job("Simple Logo Design", "Design a basic logo for a startup.", 25, 1500, "LogoDesigner", makeEmptyList()));
        ALL_JOBS.add(new Job("Business Card Design", "Design a professional business card.", 100, 1800, "LogoDesigner", makeList1("Basic Graphic Design")));
        ALL_JOBS.add(new Job("Vectorize an Image", "Convert a raster image to a scalable vector graphic.", 200, 2200, "LogoDesigner", makeList1("Basic Graphic Design")));
        ALL_JOBS.add(new Job("Website Mockup", "Design the layout for a new website.", 800, 4000, "LogoDesigner", makeList2("Basic Graphic Design", "Web Development Basics")));

        // --- Programmer Jobs ---
        ALL_JOBS.add(new Job("Fix a CSS Bug", "Fix a simple CSS layout issue.", 250, 5000, "Programmer", makeEmptyList()));
        ALL_JOBS.add(new Job("Build a Static Web Page", "Create a single-page static website from a design.", 1000, 8000, "Programmer", makeList1("Web Development Basics")));
        ALL_JOBS.add(new Job("SEO Audit", "Analyze a website and provide SEO recommendations.", 2500, 10000, "Programmer", makeList2("Web Development Basics", "Advanced SEO")));
        ALL_JOBS.add(new Job("Build a Small Company Website", "Develop a multi-page website for a client.", 8000, 15000, "Programmer", makeList2("Web Development Basics", "Project Management")));

        // --- Doctor Jobs ---
        ALL_JOBS.add(new Job("Online Consultation", "Provide a medical consultation via video call.", 500, 15000, "Doctor", makeEmptyList()));
        ALL_JOBS.add(new Job("Write Medical Article", "Write an article for a health blog.", 1500, 16000, "Doctor", makeList1("Content Writing")));
        ALL_JOBS.add(new Job("Review Medical Records", "Review a patient's medical history for a second opinion.", 2500, 18000, "Doctor", makeList1("Typing Proficiency")));
        ALL_JOBS.add(new Job("Manage a Health Seminar", "Organize and lead an online health seminar.", 10000, 25000, "Doctor", makeList2("Project Management", "Content Writing")));
    }

    // Helper: make empty skill list, beginner way (no Collections.emptyList)
    private static ArrayList<String> makeEmptyList() {
        return new ArrayList<String>();
    }

    // Helper: make list with one skill
    private static ArrayList<String> makeList1(String s1) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(s1);
        return list;
    }

    // Helper: make list with two skills
    private static ArrayList<String> makeList2(String s1, String s2) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(s1);
        list.add(s2);
        return list;
    }

    // Return all jobs (a copy so nobody can break original list)
    public static List<Job> getAllJobs() {
        ArrayList<Job> copy = new ArrayList<Job>();
        for (int i = 0; i < ALL_JOBS.size(); i++) {
            copy.add(ALL_JOBS.get(i));
        }
        return copy;
    }

    // Return jobs for one profession name, example "Doctor".
    // Uses simple for loop, no stream.
    public static List<Job> getJobsForIdentity(String identityName) {
        ArrayList<Job> result = new ArrayList<Job>();
        for (int i = 0; i < ALL_JOBS.size(); i++) {
            Job job = ALL_JOBS.get(i);
            if (job.isForIdentity(identityName)) {
                result.add(job);
            }
        }
        return result;
    }

    // Return jobs for one Identity object (Polymorphism).
    // Uses simple for loop.
    public static List<Job> getJobsForIdentity(model.Identity identity) {
        ArrayList<Job> result = new ArrayList<Job>();
        if (identity == null) {
            return result;
        }
        for (int i = 0; i < ALL_JOBS.size(); i++) {
            Job job = ALL_JOBS.get(i);
            if (job.isForIdentity(identity)) {
                result.add(job);
            }
        }
        return result;
    }

    // Return jobs that this user can do now.
    // Uses simple for loop.
    public static List<Job> getAvailableJobsForUser(User user) {
        ArrayList<Job> result = new ArrayList<Job>();
        if (user == null || user.getIdentity() == null) {
            return result;
        }
        for (int i = 0; i < ALL_JOBS.size(); i++) {
            Job job = ALL_JOBS.get(i);
            if (job.isAvailableFor(user)) {
                result.add(job);
            }
        }
        return result;
    }
}
