package com.tpaWorkers.SurveyService.dao.model;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table( name = "Answer")
public class Answer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name="content", nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id") // bu tablodaki alan adı , o tablodaki alan adı
    private Question question;
    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespondentAnswer> respondentAnswers;
    // Getter-Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
    public List<RespondentAnswer> getRespondentAnswers() {
        return respondentAnswers;
    }
    public void setRespondentAnswers(List<RespondentAnswer> respondentAnswers) {
        this.respondentAnswers = respondentAnswers;
    }
}
