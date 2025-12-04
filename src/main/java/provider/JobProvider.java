package provider;

import provider.contract.JobCatalog;

import java.util.ArrayList;
import java.util.List;

import model.career.Profession;
import model.career.Job;
import utils.Lists;
import utils.Require;

// Builds the job catalog the player picks from.
// Extends AbstractCatalog; injected into JobController/JobRewardService.
public final class JobProvider extends AbstractCatalog<Job> implements JobCatalog {

    // ----- Construction -----
    // The profession registry comes from the composition root; this catalog
    // never builds a second copy of it.
    public JobProvider(ProfessionRegistry professions) {
        super(buildDefaultCatalog(Require.notNull(professions, "professions")));
    }

    // Tuned for a 10-hour run: ~200 jobs from start to end.
    private static ArrayList<Job> buildDefaultCatalog(ProfessionRegistry professions) {
        ArrayList<Job> jobs = new ArrayList<Job>();
        // --- Typist Jobs ---
        jobs.add(new Job("Transcribe Audio Clip", "Transcribe a short audio file.", 150, 1000, professions.createProfession("Typist"), makeEmptyList()));
        jobs.add(new Job("Basic Data Entry", "Enter data from a scanned document.", 300, 1200, professions.createProfession("Typist"), makeList1("Typing Proficiency")));
        jobs.add(new Job("Write a Short Article", "Write a 500-word article on a simple topic.", 600, 1800, professions.createProfession("Typist"), makeList1("Content Writing")));
        jobs.add(new Job("Proofread a Document", "Review and correct a 10-page document for errors.", 1200, 2500, professions.createProfession("Typist"), makeList2("Typing Proficiency", "Content Writing")));

        // --- Logo Designer Jobs ---
        jobs.add(new Job("Simple Logo Design", "Design a basic logo for a startup.", 200, 1500, professions.createProfession("LogoDesigner"), makeEmptyList()));
        jobs.add(new Job("Business Card Design", "Design a professional business card.", 400, 1800, professions.createProfession("LogoDesigner"), makeList1("Basic Graphic Design")));
        jobs.add(new Job("Vectorize an Image", "Convert a raster image to a scalable vector graphic.", 700, 2200, professions.createProfession("LogoDesigner"), makeList1("Basic Graphic Design")));
        jobs.add(new Job("Website Mockup", "Design the layout for a new website.", 1500, 4000, professions.createProfession("LogoDesigner"), makeList2("Basic Graphic Design", "Web Development Basics")));

        // --- Programmer Jobs ---
        jobs.add(new Job("Fix a CSS Bug", "Fix a simple CSS layout issue.", 400, 5000, professions.createProfession("Programmer"), makeEmptyList()));
        jobs.add(new Job("Build a Static Web Page", "Create a single-page static website from a design.", 1000, 8000, professions.createProfession("Programmer"), makeList1("Web Development Basics")));
        jobs.add(new Job("SEO Audit", "Analyze a website and provide SEO recommendations.", 2000, 10000, professions.createProfession("Programmer"), makeList2("Web Development Basics", "Advanced SEO")));
        jobs.add(new Job("Build a Small Company Website", "Develop a multi-page website for a client.", 3500, 15000, professions.createProfession("Programmer"), makeList2("Web Development Basics", "Project Management")));

        // --- Doctor Jobs ---
        jobs.add(new Job("Online Consultation", "Provide a medical consultation via video call.", 600, 15000, professions.createProfession("Doctor"), makeEmptyList()));
        jobs.add(new Job("Write Medical Article", "Write an article for a health blog.", 1200, 16000, professions.createProfession("Doctor"), makeList1("Content Writing")));
        jobs.add(new Job("Review Medical Records", "Review a patient's medical history for a second opinion.", 2000, 18000, professions.createProfession("Doctor"), makeList1("Typing Proficiency")));
        jobs.add(new Job("Manage a Health Seminar", "Organize and lead an online health seminar.", 3500, 25000, professions.createProfession("Doctor"), makeList2("Project Management", "Content Writing")));
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

    // ----- Template hook -----
    @Override
    protected String nameOf(Job entry) {
        return entry.getName();
    }

    // ----- Queries -----
    // Returns jobs for one profession.
    public List<Job> getJobsForProfession(Profession profession) {
        if (profession == null) {
            return new ArrayList<Job>();
        }
        return Lists.filter(snapshot(), job -> job.isForProfession(profession));
    }
}
