package com.tpaWorkers.SurveyService.dao.model;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Entity
@Table( name = "Survey")
public class Survey implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name="title", nullable = false, unique = true)
    private String title;
    @Column(name="explanation", nullable = false)
    @Type(type = "org.hibernate.type.TextType") //  text length more than 255
    private String explanation;
    @Column(name="startDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // hem tarih hem zaman
    private Date startDate;
    @Column(name="endDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name="isReady")
    private Boolean isReady;
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // mapped by = many deki bizim kompozisyon alanının adı
    private List<Respondent> respondents;
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
    // Getter-Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getExplanation() {
        return explanation;
    }
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public List<Respondent> getRespondents() {
        return respondents;
    }
    public void setRespondents(List<Respondent> respondents) {
        this.respondents = respondents;
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    public Boolean getReady() {
        return isReady;
    }
    public void setReady(Boolean ready) {
        isReady = ready;
    }
}
