package com.tpaWorkers.SurveyService.dao.model;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table( name = "Question")
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name="title", nullable = false)
    private String title;
    @ManyToOne
    @JoinColumn(name = "survey_id", referencedColumnName = "id") // bu tablodaki alan adı , o tablodaki alan adı
    private Survey survey;
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespondentAnswer> respondentAnswers;
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
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
    public Survey getSurvey() {
        return survey;
    }
    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
    public List<RespondentAnswer> getRespondentAnswers() {
        return respondentAnswers;
    }
    public void setRespondentAnswers(List<RespondentAnswer> respondentAnswers) {
        this.respondentAnswers = respondentAnswers;
    }
    public List<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
