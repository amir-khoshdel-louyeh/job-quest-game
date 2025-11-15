package model;

import java.util.ArrayList;
import java.util.List;

// Represents one player and links money, health, career, and skills.
public class User {
    private String username;
    private Identity identity;
    private Wallet wallet;
    private Vitals vitals;
    private CareerProgress progress;
    private ArrayList<Skill> skills;

    private long blockedUntil = 0;
    private long lastSicknessTime = 0;
    private long totalPlayTime = 0;

    private int totalJobsCompleted = 0;
    private int jobBonus = 0;
    private ArrayList<String> unlockedAchievements = new ArrayList<String>();
    private ArrayList<Challenge> activeChallenges = new ArrayList<Challenge>();

    public User(String username, Identity identity, int balance) {
        this.username = username;
        this.identity = identity;
        this.wallet = new Wallet(balance);
        this.vitals = new Vitals();
        this.progress = new CareerProgress();
        this.skills = new ArrayList<Skill>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public int getBalance() {
        return wallet.getBalance();
    }

    public void deposit(int amount) {
        wallet.deposit(amount);
    }

    public void withdraw(int amount) {
        wallet.withdraw(amount);
    }

    public int getHealth() {
        return vitals.getHealth();
    }

    public void gainHealth(int amount) {
        vitals.gainHealth(amount);
    }

    public void loseHealth(int amount) {
        vitals.loseHealth(amount);
    }

    public int getEnergy() {
        return vitals.getEnergy();
    }

    public void gainEnergy(int amount) {
        vitals.gainEnergy(amount);
    }

    public void loseEnergy(int amount) {
        vitals.loseEnergy(amount);
    }

    // Returns a copy so the stored list can't be changed from outside.
    public List<Skill> getSkills() {
        ArrayList<Skill> copy = new ArrayList<Skill>();
        for (int i = 0; i < skills.size(); i++) {
            copy.add(skills.get(i));
        }
        return copy;
    }

    public void setSkills(List<Skill> newSkills) {
        skills = new ArrayList<Skill>();
        if (newSkills != null) {
            for (int i = 0; i < newSkills.size(); i++) {
                skills.add(newSkills.get(i));
            }
        }
    }

    // Adds the skill unless it is missing or already learned.
    public boolean addSkill(Skill skill) {
        if (skill == null) {
            return false;
        }
        if (skills.contains(skill)) {
            return false;
        }
        skills.add(skill);
        return true;
    }

    public boolean hasSkill(Skill skill) {
        if (skill == null) {
            return false;
        }
        return skills.contains(skill);
    }

    public boolean hasSkill(String skillName) {
        if (skillName == null) {
            return false;
        }
        for (int i = 0; i < skills.size(); i++) {
            Skill s = skills.get(i);
            if (s.getName().equals(skillName)) {
                return true;
            }
        }
        return false;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public void setBlockedUntil(long time) {
        this.blockedUntil = time;
    }

    public long getBlockedUntil() {
        return blockedUntil;
    }

    // Blocked while the current time is before the stored time.
    public boolean isBlocked() {
        long now = System.currentTimeMillis();
        if (now < blockedUntil) {
            return true;
        } else {
            return false;
        }
    }

    public void setLastSicknessTime(long time) {
        this.lastSicknessTime = time;
    }

    public long getLastSicknessTime() {
        return lastSicknessTime;
    }

    public int getLevel() {
        return progress.getLevel();
    }

    public void setLevel(int level) {
        progress.setLevel(level);
    }

    public int getExperience() {
        return progress.getExperience();
    }

    // Refills health and energy on level up.
    public void setExperience(int experience) {
        boolean levelUp = progress.setExperience(experience);
        if (levelUp) {
            vitals.restoreFull();
        }
    }

    // Refills health and energy on level up.
    public void addExperience(int amount) {
        boolean levelUp = progress.addExperience(amount);
        if (levelUp) {
            vitals.restoreFull();
        }
    }

    public int getRequiredExperienceForNextLevel() {
        return progress.getRequiredExperienceForNextLevel();
    }

    public int getExperiencePercentage() {
        return progress.getExperiencePercentage();
    }

    public int getReputation() {
        return progress.getReputation();
    }

    public void setReputation(int reputation) {
        progress.setReputation(reputation);
    }

    public void addReputation(int amount) {
        progress.addReputation(amount);
    }

    public String getReputationTitle() {
        return progress.getReputationTitle();
    }

    public int getTotalMoneyEarned() {
        return wallet.getTotalMoneyEarned();
    }

    public void setTotalMoneyEarned(int total) {
        wallet.setTotalMoneyEarned(total);
    }

    public void addToTotalMoneyEarned(int amount) {
        wallet.addToTotalMoneyEarned(amount);
    }

    public int getTotalJobsCompleted() {
        return totalJobsCompleted;
    }

    public void setTotalJobsCompleted(int total) {
        this.totalJobsCompleted = total;
    }

    public void incrementJobsCompleted() {
        this.totalJobsCompleted++;
    }

    public int getJobBonus() {
        return jobBonus;
    }

    public void setJobBonus(int jobBonus) {
        this.jobBonus = jobBonus;
    }

    // Returns a copy so the stored list can't be changed from outside.
    public List<String> getUnlockedAchievements() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < unlockedAchievements.size(); i++) {
            copy.add(unlockedAchievements.get(i));
        }
        return copy;
    }

    public void setUnlockedAchievements(List<String> list) {
        unlockedAchievements = new ArrayList<String>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                unlockedAchievements.add(list.get(i));
            }
        }
    }

    public void unlockAchievement(String id) {
        if (!unlockedAchievements.contains(id)) {
            unlockedAchievements.add(id);
        }
    }

    public boolean hasAchievement(String id) {
        return unlockedAchievements.contains(id);
    }

    // Returns a copy so the stored list can't be changed from outside.
    public List<Challenge> getActiveChallenges() {
        ArrayList<Challenge> copy = new ArrayList<Challenge>();
        for (int i = 0; i < activeChallenges.size(); i++) {
            copy.add(activeChallenges.get(i));
        }
        return copy;
    }

    public void setActiveChallenges(List<Challenge> list) {
        activeChallenges = new ArrayList<Challenge>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                activeChallenges.add(list.get(i));
            }
        }
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
            Challenge c = list.get(i);
            addChallenge(c);
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

    // Removes finished daily challenges.
    public void pruneCompletedDailyChallenges() {
        for (int i = activeChallenges.size() - 1; i >= 0; i--) {
            Challenge c = activeChallenges.get(i);
            if (c.getId().startsWith("daily_") && c.isCompleted()) {
                activeChallenges.remove(i);
            }
        }
    }

    public void removeChallenge(Challenge challenge) {
        activeChallenges.remove(challenge);
    }
}
