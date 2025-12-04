package provider.contract;

import model.career.Profession;

// The career catalog: the four starting options, plus the factory and artwork
// the start screen needs. Implemented by ProfessionRegistry.
public interface ProfessionCatalog extends Catalog<Profession> {

    Profession createProfession(String professionName);

    String imagePathFor(Profession option);
}
