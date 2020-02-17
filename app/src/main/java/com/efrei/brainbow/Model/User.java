package com.efrei.brainbow.Model;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String pwd;
    private int quizCategory;
    private int quizCurrentScore;
    private int quizGoal;
    private int runCurrentDistance;
    private int runGoal;
    private Date deadline;

    public User() {
    }

    public User(String username, String pwd, int quizCategory, int quizCurrentScore, int quizGoal, int runCurrentDistance, int runGoal, Date deadline) {
        this.username = username;
        this.pwd = pwd;
        this.quizCategory = quizCategory;
        this.quizCurrentScore = quizCurrentScore;
        this.quizGoal = quizGoal;
        this.runCurrentDistance = runCurrentDistance;
        this.runGoal = runGoal;
        this.deadline = deadline;
    }

    public User(int id, String username, String pwd, int quizCategory, int quizCurrentScore, int quizGoal, int runCurrentDistance, int runGoal, Date deadline) {
        this.id = id;
        this.username = username;
        this.pwd = pwd;
        this.quizCategory = quizCategory;
        this.quizCurrentScore = quizCurrentScore;
        this.quizGoal = quizGoal;
        this.runCurrentDistance = runCurrentDistance;
        this.runGoal = runGoal;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getQuizCategory() {
        return quizCategory;
    }

    public void setQuizCategory(int quizCategory) {
        this.quizCategory = quizCategory;
    }

    public int getQuizCurrentScore() {
        return quizCurrentScore;
    }

    public void setQuizCurrentScore(int quizCurrentScore) {
        this.quizCurrentScore = quizCurrentScore;
    }

    public int getQuizGoal() {
        return quizGoal;
    }

    public void setQuizGoal(int quizGoal) {
        this.quizGoal = quizGoal;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRunCurrentDistance() {
        return runCurrentDistance;
    }

    public void setRunCurrentDistance(int runCurrentDistance) {
        this.runCurrentDistance = runCurrentDistance;
    }

    public int getRunGoal() {
        return runGoal;
    }

    public void setRunGoal(int runGoal) {
        this.runGoal = runGoal;
    }
}
