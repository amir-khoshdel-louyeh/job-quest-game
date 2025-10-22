package services;

import model.Quest;
import model.User;
import java.util.ArrayList;
import java.util.List;
import provider.QuestProvider;

/**
 * Manages quests and tracks player progress.
 */
public class QuestService {
    private static QuestService instance;
    
    private QuestService() {}
    
    public static QuestService getInstance() {
        if (instance == null) {
            instance = new QuestService();
        }
        return instance;
    }
    
    /**
     * Initialize daily and main quests for a new user.
     */
    public void initializeQuests(User user) {
        if (user.getActiveQuests().isEmpty()) {
            // Add main story quests
            user.getActiveQuests().addAll(provider.QuestProvider.getMainQuests());
            
            // Add daily quests
            refreshDailyQuests(user);
        }
    }
    
    /**
     * Refresh daily quests (should be called once per day).
     */
    public void refreshDailyQuests(User user) {
        // Remove completed daily quests
        user.getActiveQuests().removeIf(q -> 
            q.getId().startsWith("daily_") && q.isCompleted()
        );
        
        // Add new daily quests if not already active
            for (Quest dailyQuest : QuestProvider.getDailyQuests()) {
            boolean alreadyActive = user.getActiveQuests().stream()
                .anyMatch(q -> q.getId().equals(dailyQuest.getId()));
            
            if (!alreadyActive) {
                user.addQuest(new Quest(
                    dailyQuest.getId(),
                    dailyQuest.getName(),
                    dailyQuest.getDescription(),
                    dailyQuest.getType(),
                    dailyQuest.getTargetValue(),
                    dailyQuest.getRewardMoney(),
                    dailyQuest.getRewardExperience()
                ));
            }
        }
    }
    
    /**
     * Update quest progress based on an action.
     */
    public List<Quest> updateQuestProgress(User user, Quest.QuestType type, int amount) {
        List<Quest> completedQuests = new ArrayList<>();
        
        for (Quest quest : user.getActiveQuests()) {
            if (quest.getType() == type && !quest.isCompleted()) {
                quest.addProgress(amount);
                
                // Check if quest was just completed
                if (quest.isCompleted()) {
                    grantQuestReward(user, quest);
                    completedQuests.add(quest);
                }
            }
        }
        
        return completedQuests;
    }
    
    /**
     * Grant rewards for completing a quest.
     */
    private void grantQuestReward(User user, Quest quest) {
        user.setBalance(user.getBalance() + quest.getRewardMoney());
        user.addExperience(quest.getRewardExperience());
        user.addReputation(2); // Quests give reputation
    }
    
    /**
     * Get active quests that are not yet completed.
     */
    public List<Quest> getActiveIncompleteQuests(User user) {
        return user.getActiveQuests().stream()
            .filter(q -> !q.isCompleted())
            .toList();
    }
    
    /**
     * Get completed quests.
     */
    public List<Quest> getCompletedQuests(User user) {
        return user.getActiveQuests().stream()
            .filter(Quest::isCompleted)
            .toList();
    }
}
