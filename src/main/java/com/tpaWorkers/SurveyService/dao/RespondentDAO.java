package com.tpaWorkers.SurveyService.dao;

import com.tpaWorkers.SurveyService.dao.model.Respondent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("RespondentDAO")
public class RespondentDAO {

    @Autowired
    protected SessionFactory sessionFactory;
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public long create(Respondent respondent) {
        getCurrentSession().save(respondent);
        return respondent.getId();
    }

    public Respondent get(long id){
        return getCurrentSession().get(Respondent.class,id);
    }
    public List<Respondent> list() {
        return getCurrentSession().createQuery("from Respondent").list();
    }
    public boolean remove(Respondent respondent){
        getCurrentSession().remove(respondent);
        return true;
    }

    //our funcs
    public boolean hasThisPersonAnsweredSurveyBefore(String respondentMail,long surveyId){

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<Respondent> root = criteriaQuery.from(Respondent.class);
        Predicate perdicateRespondentMailEquals = criteriaBuilder.equal(root.get("eMail"),respondentMail);
        Predicate perdicateSurveyIdEquals = criteriaBuilder.equal(root.get("survey"),surveyId);
        criteriaQuery.select(criteriaBuilder.count(root)).where(perdicateRespondentMailEquals,perdicateSurveyIdEquals);

        Query<Long> query = getCurrentSession().createQuery(criteriaQuery);
        Long count = query.getSingleResult();

        return (count>0)?true:false;
    }


    // To SurveyService
    public List<Respondent> getRespondentsBySurvey(long surveyId){
            // ankete göre katılımcıları çek
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Respondent> criteriaQuery = criteriaBuilder.createQuery(Respondent.class);

        Root<Respondent> root = criteriaQuery.from(Respondent.class);
        Predicate predicateEqualSurveyId = criteriaBuilder.equal(root.get("survey"),surveyId);
        criteriaQuery.select(root).where(predicateEqualSurveyId);

        Query<Respondent> query = getCurrentSession().createQuery(criteriaQuery);
        List<Respondent> surveyRespondents = query.getResultList();
        return surveyRespondents;
    }


}
