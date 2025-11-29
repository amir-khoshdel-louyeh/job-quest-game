package model.player;

import model.Identity;

// Who the player is.
public interface PlayerIdentityView {
    String getUsername();

    Identity getIdentity();

    String getIdentityName();
}
