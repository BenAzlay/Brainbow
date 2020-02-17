package com.efrei.brainbow.Model;

import java.util.List;

public class Question {
    private String question;
    private String category;
    private String type;
    private String diffuculty;
    private String correct_answer;
    private List<String> incorrect_answers;

    public String getCorrect_answer() {
        return correct_answer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }
}
