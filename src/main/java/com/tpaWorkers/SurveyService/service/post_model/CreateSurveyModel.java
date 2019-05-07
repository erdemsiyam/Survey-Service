package com.tpaWorkers.SurveyService.service.post_model;

import com.tpaWorkers.SurveyService.dao.model.Answer;
import com.tpaWorkers.SurveyService.dao.model.Question;
import com.tpaWorkers.SurveyService.dao.model.Survey;

import java.util.Date;
import java.util.List;

public class CreateSurveyModel { // Json Post Model
    public SurveyModel Survey;
    public List<QuestionModel> Questions;

    public static class SurveyModel{
        private String Title;
        private String Explanation;
        private String StartDate;
        private String EndDate;
        private Boolean IsReady;

        public String getTitle() {return Title;}
        public void setTitle(String title) {Title = title;}
        public String getExplanation() {return Explanation;}
        public void setExplanation(String explanation) {Explanation = explanation;}
        public String getStartDate() {return StartDate;}
        public void setStartDate(String startDate) {StartDate = startDate;}
        public String getEndDate() {return EndDate;}
        public void setEndDate(String endDate) {EndDate = endDate;}
        public Boolean getReady() {return IsReady;}
        public void setReady(Boolean ready) {IsReady = ready;}
        public Survey getRealSurveyObject(){
            Survey survey = new Survey();
            survey.setTitle(Title.trim());
            survey.setExplanation(Explanation.trim());
            if(StartDate == null || StartDate.trim().equalsIgnoreCase(""))
                survey.setStartDate(new Date());
            else
                survey.setStartDate(new Date(StartDate));
            survey.setEndDate(new Date(EndDate));
            survey.setReady(IsReady);
            return survey;
        }
    }
    public static class QuestionModel{
        private String Title;
        private List<AnswerModel> Answers;

        public List<AnswerModel> getAnswers() {return Answers;}
        public void setAnswers(List<AnswerModel> answers) {Answers = answers;}
        public String getTitle() {return Title;}
        public void setTitle(String title) {Title = title;}
        public Question getRealQuestionObject(){
            Question question = new Question();
            question.setTitle(Title.trim());
            return question;
        }
    }
    public static class AnswerModel{
        private String Content;

        public String getContent() {return Content;}
        public void setContent(String content) {Content = content;}
        public Answer getRealAnswerObject(){
            Answer answer = new Answer();
            answer.setContent(Content.trim());
            return answer;
        }
    }

    public SurveyModel getSurvey() {return Survey;}
    public void setSurvey(SurveyModel survey) {Survey = survey;}
    public List<QuestionModel> getQuestions() {return Questions;}
    public void setQuestions(List<QuestionModel> questions) {Questions = questions;}

}
