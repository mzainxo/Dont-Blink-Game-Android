package com.example.dontblinkappx;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    private String username;
    private int highScore;
    private int bestTime;

    public LeaderboardEntry( String username, int highScore, int bestTime) {
        this.username = username;
        this.highScore = highScore;
        this.bestTime = bestTime;
    }

    public String getUsername() {
        return username;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getBestTime() {
        return bestTime;
    }

    @Override
    public int compareTo(LeaderboardEntry other) {
        // Compare based on high score
        if (this.highScore != other.highScore) {
            return Integer.compare(other.highScore, this.highScore); // descending order
        }
        // If high scores are equal, compare based on best time
        return Integer.compare(this.bestTime, other.bestTime);
    }
}
