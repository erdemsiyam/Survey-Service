package com.tpaWorkers.SurveyService.service.view_model;

import java.util.List;

public class QuestionSingleVM {
    private Long Id;
    private String Title;
    private List<AnswerSingleVM> Answers;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<AnswerSingleVM> getAnswers() {
        return Answers;
    }

    public void setAnswers(List<AnswerSingleVM> answers) {
        Answers = answers;
    }
}
