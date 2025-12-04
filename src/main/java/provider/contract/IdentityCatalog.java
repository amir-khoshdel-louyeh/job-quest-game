package provider.contract;

import java.util.List;

import model.Identity;

// Read-only career catalog contract used by the start screen.
public interface IdentityCatalog {

    Identity createIdentity(String identityName);

    List<Identity> getAvailableOptions();

    String imagePathFor(Identity option);
}
