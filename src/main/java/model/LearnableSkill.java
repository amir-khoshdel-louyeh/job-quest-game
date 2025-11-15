package model;

import java.util.ArrayList;
import java.util.List;

// A skill offered at school with a cost and requirements.
public class LearnableSkill {
    private String name;
    private String description;
    private int cost;
    private long requiredPlayTimeMinutes;
    private ArrayList<Identity> identities;

    public LearnableSkill(String name, String description, int cost, long requiredPlayTimeMinutes,
                          List<Identity> identities) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.requiredPlayTimeMinutes = requiredPlayTimeMinutes;
        this.identities = new ArrayList<Identity>();
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
    public List<Identity> getIdentities() {
        ArrayList<Identity> copy = new ArrayList<Identity>();
        for (int i = 0; i < identities.size(); i++) {
            copy.add(identities.get(i));
        }
        return copy;
    }

    public boolean isForIdentity(Identity identity) {
        if (identity == null) {
            return false;
        }
        for (int i = 0; i < identities.size(); i++) {
            if (identities.get(i).isSameProfession(identity)) {
                return true;
            }
        }
        return false;
    }

}
