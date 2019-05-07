package com.tpaWorkers.SurveyService.service.view_model;

public class RespondentSingleVM {
    private Long Id;
    private String NameAndSurname;
    private String EMail;
    private String Phone;
    private String RegisterDate;

    public static class WithSurveyId extends RespondentSingleVM{
        private Long JoinedSurveyId;

        public Long getJoinedSurveyId() {
            return JoinedSurveyId;
        }

        public void setJoinedSurveyId(Long joinedSurveyId) {
            JoinedSurveyId = joinedSurveyId;
        }
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNameAndSurname() {
        return NameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        NameAndSurname = nameAndSurname;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(String registerDate) {
        RegisterDate = registerDate;
    }
}
