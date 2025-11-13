package model;

import java.util.ArrayList;
import java.util.List;

// This class represents one job that the player can do.
// Example: "Fix computer" for Programmer.
public class Job {
    // job name
    private String name;
    // job description
    private String description;
    // how much money player gets
    private int payment;
    // how much energy it costs
    private int energyCost;
    // which profession can do this job, example: "Programmer"
    private String requiredIdentity;
    // list of skill names needed for this job
    // We use ArrayList because it is simple for beginners
    private ArrayList<String> requiredSkills;

    // Constructor: create a new job
    public Job(String name, String description, int payment, int energyCost, String requiredIdentity, List<String> requiredSkills) {
        this.name = name;
        this.description = description;
        this.payment = payment;
        this.energyCost = energyCost;
        this.requiredIdentity = requiredIdentity;
        // if no skills given, use empty list
        if (requiredSkills == null) {
            this.requiredSkills = new ArrayList<String>();
        } else {
            this.requiredSkills = new ArrayList<String>(requiredSkills);
        }
    }

    // Getters: simple methods to read private fields (Encapsulation)
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPayment() {
        return payment;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public String getRequiredIdentity() {
        return requiredIdentity;
    }

    // Return copy of required skills
    // Uses simple for loop, no stream
    public List<String> getRequiredSkills() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < requiredSkills.size(); i++) {
            copy.add(requiredSkills.get(i));
        }
        return copy;
    }

    // Return required skills as Skill objects.
    // Uses simple for loop, no stream.
    public List<Skill> getRequiredSkillsAsObjects() {
        ArrayList<Skill> result = new ArrayList<Skill>();
        for (int i = 0; i < requiredSkills.size(); i++) {
            String skillName = requiredSkills.get(i);
            result.add(new Skill(skillName));
        }
        return result;
    }

    // Check if this job is for this profession name
    public boolean isForIdentity(String userIdentity) {
        return requiredIdentity.equals(userIdentity);
    }

    // Check if this job is for this Identity object.
    // This shows Polymorphism: we use Identity interface.
    public boolean isForIdentity(Identity identity) {
        if (identity == null) {
            return false;
        }
        return requiredIdentity.equals(identity.getProfessionName());
    }

    // Check if player can do this job (by names)
    // Uses simple for loop, no containsAll
    public boolean isAvailableFor(String userIdentity, List<String> userSkills) {
        if (!userIdentity.equals(requiredIdentity)) {
            return false;
        }
        // check that user has all required skills
        for (int i = 0; i < requiredSkills.size(); i++) {
            String need = requiredSkills.get(i);
            if (!userSkills.contains(need)) {
                return false;
            }
        }
        return true;
    }

    // Check if player can do this job (by objects)
    // Uses simple for loop
    public boolean isAvailableFor(Identity identity, List<Skill> userSkills) {
        if (identity == null || userSkills == null) {
            return false;
        }
        if (!requiredIdentity.equals(identity.getProfessionName())) {
            return false;
        }
        List<Skill> needed = getRequiredSkillsAsObjects();
        for (int i = 0; i < needed.size(); i++) {
            if (!userSkills.contains(needed.get(i))) {
                return false;
            }
        }
        return true;
    }

    // Check if a User object can do this job.
    public boolean isAvailableFor(User user) {
        if (user == null || user.getIdentity() == null) {
            return false;
        }
        return isAvailableFor(user.getIdentity(), user.getSkills());
    }
}