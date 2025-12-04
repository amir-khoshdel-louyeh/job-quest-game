package provider.contract;

import model.goals.Achievement;

// Read-only achievement-template contract: one list, no extra reads.
public interface AchievementCatalog extends Catalog<Achievement> {
}
