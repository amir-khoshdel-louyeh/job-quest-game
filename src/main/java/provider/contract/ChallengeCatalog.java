package provider.contract;

import java.util.List;

import model.goals.Challenge;

// Read-only challenge-template contract.
public interface ChallengeCatalog {
    List<Challenge> getChallenges();
}
