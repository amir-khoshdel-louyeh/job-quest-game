package provider.contract;

import java.util.List;

import model.career.Job;
import model.career.Profession;

// Read-only job catalog contract: all jobs, plus the profession cut.
public interface JobCatalog extends Catalog<Job> {
    List<Job> getJobsForProfession(Profession profession);
}
