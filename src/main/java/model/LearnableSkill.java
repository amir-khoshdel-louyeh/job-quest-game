package model;

import java.util.ArrayList;
import java.util.List;

// This class represents a skill that can be learned at school.
// Example: "Java" for Programmer.
public class LearnableSkill {
    private String name;
    private String description;
    private int cost;
    private long requiredPlayTimeMinutes; // minutes of play needed
    private ArrayList<String> identities; // which professions can learn it

    // Constructor
    // Uses simple for loop to copy list, no Collections.unmodifiableList
    public LearnableSkill(String name, String description, int cost, long requiredPlayTimeMinutes,
                          List<String> identities) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.requiredPlayTimeMinutes = requiredPlayTimeMinutes;
        // copy list to keep Encapsulation
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

    // Return copy of identities
    // Uses simple for loop
    public List<String> getIdentities() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < identities.size(); i++) {
            copy.add(identities.get(i));
        }
        return copy;
    }

    // Check if this skill is for this profession name
    public boolean isForIdentity(String identityName) {
        return identities.contains(identityName);
    }

    // Check if this skill is for this Identity object (Polymorphism)
    public boolean isForIdentity(Identity identity) {
        if (identity == null) {
            return false;
        }
        return identities.contains(identity.getProfessionName());
    }

}