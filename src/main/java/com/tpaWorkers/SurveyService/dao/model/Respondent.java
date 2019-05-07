package com.tpaWorkers.SurveyService.dao.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "Respondent")
public class Respondent implements Serializable { // katılımcı
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name="nameAndSurname", nullable = false)
    private String nameAndSurname;
    @Column(name="eMail", nullable = false)
    private String eMail;
    @Column(name="phone", nullable = false)
    private String phone;
    @Column(name="registerDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // hem tarih hem zaman
    private Date registerDate;
    @ManyToOne
    @JoinColumn(name = "survey_id", referencedColumnName = "id") // bu tablodaki alan adı , o tablodaki alan adı
    private Survey survey;
    @OneToMany(mappedBy = "respondent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespondentAnswer> respondentAnswers;
    // Getter-Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNameAndSurname() {
        return nameAndSurname;
    }
    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }
    public String geteMail() {
        return eMail;
    }
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Date getRegisterDate() {
        return registerDate;
    }
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
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
}
