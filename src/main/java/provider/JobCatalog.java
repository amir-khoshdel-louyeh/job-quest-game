package provider;

import java.util.List;

import model.Identity;
import model.career.Job;

// Read-only job catalog contract.
public interface JobCatalog {
    List<Job> getAll();

    List<Job> getJobsForIdentity(Identity identity);
}
