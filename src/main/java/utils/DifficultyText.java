package utils;

// Single home for difficulty stars like "**-". Keeps view text out of TextTask.
public final class DifficultyText {
    private DifficultyText() {
    }

    public static String stars(int difficulty) {
        int level = difficulty;
        if (level < 1) {
            level = 1;
        }
        if (level > 3) {
            level = 3;
        }
        String result = "";
        for (int i = 0; i < level; i++) {
            result = result + "*";
        }
        for (int i = level; i < 3; i++) {
            result = result + "-";
        }
        return result;
    }
}
