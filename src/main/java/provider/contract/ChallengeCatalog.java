package provider.contract;

import model.goals.Challenge;

// Read-only challenge-template contract: one list, no extra reads.
public interface ChallengeCatalog extends Catalog<Challenge> {
}
