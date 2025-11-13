package model;

import java.util.ArrayList;
import java.util.List;

// A skill offered at school with a cost and requirements.
public class LearnableSkill {
    private String name;
    private String description;
    private int cost;
    private long requiredPlayTimeMinutes;
    private ArrayList<String> identities;

    public LearnableSkill(String name, String description, int cost, long requiredPlayTimeMinutes,
                          List<String> identities) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.requiredPlayTimeMinutes = requiredPlayTimeMinutes;
        this.identities = new ArrayList<String>();
        if (identities != null) {
            for (int i = 0; i < identities.size(); i++) {
                this.identities.add(identities.get(i));
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public long getRequiredPlayTimeMinutes() {
        return requiredPlayTimeMinutes;
    }

    // Returns a copy so the stored list can't be changed from outside.
    public List<String> getIdentities() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < identities.size(); i++) {
            copy.add(identities.get(i));
        }
        return copy;
    }

    public boolean isForIdentity(String identityName) {
        return identities.contains(identityName);
    }

    public boolean isForIdentity(Identity identity) {
        if (identity == null) {
            return false;
        }
        return identities.contains(identity.getProfessionName());
    }

}
