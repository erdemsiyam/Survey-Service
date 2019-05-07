package com.tpaWorkers.SurveyService.service.view_model;

import java.util.List;

public class RespondentAnswersVM {
    private RespondentSingleVM Respondent;
    private SurveySingleVM Survey;
    private List<QuestionSingleVM> Questions;

    public RespondentSingleVM getRespondent() {
        return Respondent;
    }

    public void setRespondent(RespondentSingleVM respondent) {
        Respondent = respondent;
    }

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
