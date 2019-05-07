package com.tpaWorkers.SurveyService.service.post_model;

import com.tpaWorkers.SurveyService.dao.model.Answer;
import com.tpaWorkers.SurveyService.dao.model.Question;

import java.util.List;

public class AddQuestionModel {
    public List<QuestionModel> Questions;

    public class QuestionModel{
        public String Title;
        public List<AnswerModel> Answers;

        public String getTitle() {return Title;}
        public void setTitle(String title) {Title = title;}
        public List<AnswerModel> getAnswers() {return Answers;}
        public void setAnswers(List<AnswerModel> answers) {Answers = answers;}
        public Question getRealQuestionObject(){
            Question question = new Question();
            question.setTitle(Title.trim());
            return question;
        }
    }
    public class AnswerModel{
        public String Content;

        public String getContent() {return Content;}
        public void setContent(String content) {Content = content;}
        public Answer getRealAnswerObject(){
            Answer answer = new Answer();
            answer.setContent(Content.trim());
            return answer;
        }
    }

    public List<QuestionModel> getQuestions() {return Questions;}
    public void setQuestions(List<QuestionModel> questions) {Questions = questions;}
}
