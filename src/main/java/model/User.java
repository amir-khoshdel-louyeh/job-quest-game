package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private Identity identity;
    private int balance;
    private int health;      // 0 تا 100
    private int energy;      // 0 تا 100,000
    private Inventory inventory; // Use the Inventory object
    private List<Skill> skills;

    private long blockedUntil = 0;      // timestamp پایان بلاک در میلی‌ثانیه
    private long lastSicknessTime = 0;  // timestamp آخرین مریضی
    private long totalPlayTime = 0;     // Total time played in milliseconds

    public User(String username, String password, Identity identity, int balance) {
        this.username = username;
        this.password = password;
        this.identity = identity;
        this.balance = balance;
        this.health = 100;
        this.energy = 100_000; // انرژی اولیه
        this.inventory = new Inventory(); // Initialize the Inventory object
        this.skills = new ArrayList<>();
    }

    // ---------------- Getters & Setters ----------------
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Identity getIdentity() { return identity; }
    public void setIdentity(Identity identity) { this.identity = identity; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = Math.max(0, Math.min(100, health)); }

    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = Math.max(0, Math.min(100_000, energy)); }

    public Inventory getInventory() { return inventory; } // Return the Inventory object

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    public long getTotalPlayTime() { return totalPlayTime; }
    public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }

    // ---------------- Block / Unblock ----------------
    public void setBlockedUntil(long time) { this.blockedUntil = time; }
    public long getBlockedUntil() { return blockedUntil; }
    public boolean isBlocked() { return System.currentTimeMillis() < blockedUntil; }

    // ---------------- Last Sickness ----------------
    public void setLastSicknessTime(long time) { this.lastSicknessTime = time; }
    public long getLastSicknessTime() { return lastSicknessTime; }
}
