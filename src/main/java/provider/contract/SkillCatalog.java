package provider.contract;

import java.util.List;

import model.career.Skill;
import model.career.Profession;

// Read-only skill catalog contract: name lookup plus the profession cut.
public interface SkillCatalog extends NamedCatalog<Skill> {
    List<Skill> getSkillsForProfession(Profession profession);
}
