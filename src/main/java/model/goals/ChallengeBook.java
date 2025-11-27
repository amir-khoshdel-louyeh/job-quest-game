package model.goals;

import java.util.ArrayList;
import java.util.List;

// Owns active challenges.
// Used by model.player.User (sole owner).
public class ChallengeBook {
    private ArrayList<Challenge> activeChallenges = new ArrayList<Challenge>();

    // ----- Getter -----
    // Returns snapshots so callers cannot mutate the stored progress.
    public List<Challenge> getActiveChallenges() {
        ArrayList<Challenge> copy = new ArrayList<Challenge>();
        for (int i = 0; i < activeChallenges.size(); i++) {
            copy.add(activeChallenges.get(i).copySnapshot());
        }
        return copy;
    }

    // ----- Commands -----
    // Adds progress to challenges of this type; returns newly completed ones.
    public List<Challenge> updateProgress(Challenge.ChallengeType type, int amount) {
        ArrayList<Challenge> finished = new ArrayList<Challenge>();
        if (type == null) {
            return finished;
        }
        for (int i = 0; i < activeChallenges.size(); i++) {
            Challenge c = activeChallenges.get(i);
            if (c.getType() == type && !c.isCompleted()) {
                c.addProgress(amount);
                if (c.isCompleted()) {
                    finished.add(c.copySnapshot());
                }
            }
        }
        return finished;
    }

    public void addChallenge(Challenge challenge) {
        if (challenge == null) {
            return;
        }
        if (!hasChallenge(challenge.getId())) {
            activeChallenges.add(challenge);
        }
    }

    public void addChallenges(List<Challenge> list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            addChallenge(list.get(i));
        }
    }

    // ----- Queries -----
    public boolean hasChallenge(String challengeId) {
        if (challengeId == null) {
            return false;
        }
        for (int i = 0; i < activeChallenges.size(); i++) {
            Challenge c = activeChallenges.get(i);
            if (challengeId.equals(c.getId())) {
                return true;
            }
        }
        return false;
    }
}
