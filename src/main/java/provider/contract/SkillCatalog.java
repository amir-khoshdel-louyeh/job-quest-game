package provider.contract;

import java.util.List;

import model.career.LearnableSkill;
import model.career.Profession;

// Read-only skill catalog contract: name lookup plus the profession cut.
public interface SkillCatalog extends NamedCatalog<LearnableSkill> {
    List<LearnableSkill> getSkillsForProfession(Profession profession);
}
