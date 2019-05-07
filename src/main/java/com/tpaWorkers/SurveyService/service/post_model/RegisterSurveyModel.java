package com.tpaWorkers.SurveyService.service.post_model;


import com.tpaWorkers.SurveyService.dao.model.Respondent;

import java.util.List;

public class RegisterSurveyModel {
    public RespondentModel Respondent;
    public List<RespondentAnswerModel> RespondentAnswers;

    public class RespondentModel{
        public String NameAndSurname;
        public String EMail;
        public String Phone;

        public String getNameAndSurname() {return NameAndSurname;}
        public void setNameAndSurname(String nameAndSurname) {NameAndSurname = nameAndSurname;}
        public String getEMail() {return EMail;}
        public void setEMail(String EMail) {this.EMail = EMail;}
        public String getPhone() {return Phone;}
        public void setPhone(String phone) {Phone = phone;}
        public Respondent getRealRespondentObject(){
            Respondent respondent = new Respondent();
            respondent.setNameAndSurname(NameAndSurname.trim());
            respondent.seteMail(EMail.trim());
            respondent.setPhone(Phone.trim());
            return respondent;
        }
    }
    public class RespondentAnswerModel{
        public long QuestionId;
        public long AnswerId;

        public long getQuestionId() {return QuestionId;}
        public void setQuestionId(long questionId) {QuestionId = questionId;}
        public long getAnswerId() {return AnswerId;}
        public void setAnswerId(long answerId) {AnswerId = answerId;}
    }

    public RespondentModel getRespondent() {return Respondent;}
    public void setRespondent(RespondentModel respondent) {Respondent = respondent;}
    public List<RespondentAnswerModel> getRespondentAnswers() {return RespondentAnswers;}
    public void setRespondentAnswers(List<RespondentAnswerModel> respondentAnswers) {RespondentAnswers = respondentAnswers;}
}
