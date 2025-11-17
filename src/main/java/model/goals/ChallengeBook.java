package model.goals;

import java.util.ArrayList;
import java.util.List;

// Owns active challenges. Extracted from User for SRP:
// the "daily_" pruning rule lives next to Challenge, not in User.
public class ChallengeBook {
    private ArrayList<Challenge> activeChallenges = new ArrayList<Challenge>();

    // Deep copy: each Challenge is snapshotted so callers cannot mutate
    // the stored progress via the returned list (Challenge is mutable).
    // Mutation goes through updateProgress() inside this aggregate.
    public List<Challenge> getActiveChallenges() {
        ArrayList<Challenge> copy = new ArrayList<Challenge>();
        for (int i = 0; i < activeChallenges.size(); i++) {
            copy.add(activeChallenges.get(i).copySnapshot());
        }
        return copy;
    }

    // Rich behavior: adds progress to stored challenges of this type.
    // Returns snapshots of the newly completed ones (for rewards/display).
    public List<Challenge> updateProgress(Challenge.ChallengeType type, int amount) {
        ArrayList<Challenge> finished = new ArrayList<Challenge>();
        if (type == null || amount <= 0) {
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

    public void pruneCompletedDailyChallenges() {
        for (int i = activeChallenges.size() - 1; i >= 0; i--) {
            Challenge c = activeChallenges.get(i);
            if (c.getId().startsWith("daily_") && c.isCompleted()) {
                activeChallenges.remove(i);
            }
        }
    }

    public void removeChallengeById(String challengeId) {
        if (challengeId == null) {
            return;
        }
        for (int i = activeChallenges.size() - 1; i >= 0; i--) {
            if (challengeId.equals(activeChallenges.get(i).getId())) {
                activeChallenges.remove(i);
            }
        }
    }
}
