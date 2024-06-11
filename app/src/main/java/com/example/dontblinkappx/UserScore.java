package com.example.dontblinkappx;

public class UserScore implements Comparable<UserScore> {
    public String userId;
    public int highScore;

    public UserScore(String userId, int highScore) {
        this.userId = userId;
        this.highScore = highScore;
    }

    @Override
    public int compareTo(UserScore other) {
        return Integer.compare(this.highScore, other.highScore);
    }
}
