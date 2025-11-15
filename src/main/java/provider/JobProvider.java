package provider;

import java.util.ArrayList;
import java.util.List;

import model.Identity;
import model.Job;
import model.User;
import model.identity.Doctor;
import model.identity.LogoDesigner;
import model.identity.Programmer;
import model.identity.Typist;

// Builds the list of jobs the player can pick from.
public class JobProvider {

    private static ArrayList<Job> ALL_JOBS = new ArrayList<Job>();

    static {
        // 10-hour run tuning: floor raised ~10x so the first skill
        // costs ~2 jobs instead of ~35. Profession gap compressed to
        // ~150$ (start) -> ~3500$ (end) so every path finishes in ~200 jobs.
        // --- Typist Jobs ---
        ALL_JOBS.add(new Job("Transcribe Audio Clip", "Transcribe a short audio file.", 150, 1000, new Typist(), makeEmptyList()));
        ALL_JOBS.add(new Job("Basic Data Entry", "Enter data from a scanned document.", 300, 1200, new Typist(), makeList1("Typing Proficiency")));
        ALL_JOBS.add(new Job("Write a Short Article", "Write a 500-word article on a simple topic.", 600, 1800, new Typist(), makeList1("Content Writing")));
        ALL_JOBS.add(new Job("Proofread a Document", "Review and correct a 10-page document for errors.", 1200, 2500, new Typist(), makeList2("Typing Proficiency", "Content Writing")));

        // --- Logo Designer Jobs ---
        ALL_JOBS.add(new Job("Simple Logo Design", "Design a basic logo for a startup.", 200, 1500, new LogoDesigner(), makeEmptyList()));
        ALL_JOBS.add(new Job("Business Card Design", "Design a professional business card.", 400, 1800, new LogoDesigner(), makeList1("Basic Graphic Design")));
        ALL_JOBS.add(new Job("Vectorize an Image", "Convert a raster image to a scalable vector graphic.", 700, 2200, new LogoDesigner(), makeList1("Basic Graphic Design")));
        ALL_JOBS.add(new Job("Website Mockup", "Design the layout for a new website.", 1500, 4000, new LogoDesigner(), makeList2("Basic Graphic Design", "Web Development Basics")));

        // --- Programmer Jobs ---
        ALL_JOBS.add(new Job("Fix a CSS Bug", "Fix a simple CSS layout issue.", 400, 5000, new Programmer(), makeEmptyList()));
        ALL_JOBS.add(new Job("Build a Static Web Page", "Create a single-page static website from a design.", 1000, 8000, new Programmer(), makeList1("Web Development Basics")));
        ALL_JOBS.add(new Job("SEO Audit", "Analyze a website and provide SEO recommendations.", 2000, 10000, new Programmer(), makeList2("Web Development Basics", "Advanced SEO")));
        ALL_JOBS.add(new Job("Build a Small Company Website", "Develop a multi-page website for a client.", 3500, 15000, new Programmer(), makeList2("Web Development Basics", "Project Management")));

        // --- Doctor Jobs ---
        ALL_JOBS.add(new Job("Online Consultation", "Provide a medical consultation via video call.", 600, 15000, new Doctor(), makeEmptyList()));
        ALL_JOBS.add(new Job("Write Medical Article", "Write an article for a health blog.", 1200, 16000, new Doctor(), makeList1("Content Writing")));
        ALL_JOBS.add(new Job("Review Medical Records", "Review a patient's medical history for a second opinion.", 2000, 18000, new Doctor(), makeList1("Typing Proficiency")));
        ALL_JOBS.add(new Job("Manage a Health Seminar", "Organize and lead an online health seminar.", 3500, 25000, new Doctor(), makeList2("Project Management", "Content Writing")));
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
    public static List<Job> getAllJobs() {
        ArrayList<Job> copy = new ArrayList<Job>();
        for (int i = 0; i < ALL_JOBS.size(); i++) {
            copy.add(ALL_JOBS.get(i));
        }
        return copy;
    }

    // Return jobs for one profession.
    public static List<Job> getJobsForIdentity(Identity identity) {
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
