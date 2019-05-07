package com.tpaWorkers.SurveyService.service.view_model;

public class AnswerSingleVM {
    private Long Id;
    private String Content;
    private Long SelectedCount;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Long getSelectedCount() {
        return SelectedCount;
    }

    public void setSelectedCount(Long selectedCount) {
        SelectedCount = selectedCount;
    }
}
