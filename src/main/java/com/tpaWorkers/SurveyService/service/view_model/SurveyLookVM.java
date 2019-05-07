package com.tpaWorkers.SurveyService.service.view_model;

import java.util.List;

public class SurveyLookVM {

    private SurveySingleVM Survey;
    private List<QuestionSingleVM> Questions;

    public SurveySingleVM getSurvey() {
        return Survey;
    }

    public void setSurvey(SurveySingleVM survey) {
        Survey = survey;
    }

    public List<QuestionSingleVM> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<QuestionSingleVM> questions) {
        Questions = questions;
    }
}
