package com.tpaWorkers.SurveyService.dao.model;
import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table( name = "RespondentAnswer", uniqueConstraints={@UniqueConstraint(columnNames={"respondent_id","answer_id","question_id"})})
public class RespondentAnswer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "respondent_id", referencedColumnName = "id")
    private Respondent respondent;
    @ManyToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "id")
    private Answer answer;
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;
    // Getter-Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Respondent getRespondent() {
        return respondent;
    }
    public void setRespondent(Respondent respondent) {
        this.respondent = respondent;
    }
    public Answer getAnswer() {
        return answer;
    }
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
}
