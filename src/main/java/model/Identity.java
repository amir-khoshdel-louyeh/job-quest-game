package model;

public abstract class Identity {
    protected String name;

    public Identity(String name) { this.name = name; }

    public abstract void performDailyWork(User user); // کار روزانه
}
