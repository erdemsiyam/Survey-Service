package com.tpaWorkers.SurveyService.service.view_model;

import java.util.List;

public class SurveyResultVM {
    private SurveySingleVM Survey;
    private Long RespondentCount;
    private List<QuestionSingleVM> Questions;

    public SurveySingleVM getSurvey() {
        return Survey;
    }

    public void setSurvey(SurveySingleVM survey) {
        Survey = survey;
    }

    public Long getRespondentCount() {
        return RespondentCount;
    }

    public void setRespondentCount(Long respondentCount) {
        RespondentCount = respondentCount;
    }

    public List<QuestionSingleVM> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<QuestionSingleVM> questions) {
        Questions = questions;
    }
}
