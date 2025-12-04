package provider.contract;

import java.util.List;

import model.Identity;
import model.career.LearnableSkill;

// Read-only skill catalog contract.
public interface SkillCatalog {
    LearnableSkill getSkill(String name);

    List<LearnableSkill> getSkillsForIdentity(Identity identity);
}
