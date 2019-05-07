package com.tpaWorkers.SurveyService.service.view_model;

public class SurveySingleVM {
    private Long Id;
    private String Title;
    private String Explanation;
    private String StartDate;
    private String EndDate;
    private Boolean IsReady;

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

    public String getExplanation() {
        return Explanation;
    }

    public void setExplanation(String explanation) {
        Explanation = explanation;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public Boolean getReady() {
        return IsReady;
    }

    public void setReady(Boolean ready) {
        IsReady = ready;
    }
}
