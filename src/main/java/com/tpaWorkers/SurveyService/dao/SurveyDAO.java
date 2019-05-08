package com.tpaWorkers.SurveyService.dao;

import com.tpaWorkers.SurveyService.dao.model.Respondent;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

@Repository("SurveyDAO")
public class SurveyDAO {

    @Autowired
    protected SessionFactory sessionFactory;
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Survey get(long id){
        return getCurrentSession().get(Survey.class,id);
    }
    public long create(Survey survey) {
        getCurrentSession().save(survey);
        return survey.getId();
    }
    public List<Survey> list() {
        return getCurrentSession().createQuery("from Survey").list();
    }
    public boolean remove(Survey survey){
        try {
            getCurrentSession().remove(survey);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    //our funcs
    public boolean setReady(long surveyId){
        //anketi hazır işaretle
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaUpdate<Survey> criteria = builder.createCriteriaUpdate(Survey.class);

        Root<Survey> root = criteria.from(Survey.class);
        criteria.set(root.get("isReady"), true);

        criteria.where(builder.equal(root.get("id"), surveyId));
        int resultCount = getCurrentSession().createQuery(criteria).executeUpdate();
        return (resultCount>0)?true:false;
    }
    public List<Survey> getNotReadySurveys(){
        // ready olmayan anketleri getir
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Survey> criteriaQuery = criteriaBuilder.createQuery(Survey.class);

        Root<Survey> root = criteriaQuery.from(Survey.class);
        Predicate predicateReadyEqualsFalse = criteriaBuilder.equal(root.get("isReady"),false);
        criteriaQuery.select(root).where(predicateReadyEqualsFalse);

        Query<Survey> query = getCurrentSession().createQuery(criteriaQuery);
        List<Survey> readyFalseSurveys = query.getResultList();
        return readyFalseSurveys;
    }
    public List<Survey> getReadySurveys(){
        // ready olan anketleri getir
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Survey> criteriaQuery = criteriaBuilder.createQuery(Survey.class);

        Root<Survey> root = criteriaQuery.from(Survey.class);
        Predicate predicateReadyEqualsTrue = criteriaBuilder.equal(root.get("isReady"),true);
        criteriaQuery.select(root).where(predicateReadyEqualsTrue);

        Query<Survey> query = getCurrentSession().createQuery(criteriaQuery);
        List<Survey> readyTrueSurveys = query.getResultList();
        return readyTrueSurveys;
    }

    public long getRespondentCountForSurvey(long surveyId){
        // ankete katılan kişi sayısı
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<Respondent> root = criteriaQuery.from(Respondent.class);
        Predicate surveyRespondents = criteriaBuilder.equal(root.get("survey"),surveyId);

        criteriaQuery.select(criteriaBuilder.count(root)).where(surveyRespondents);

        Query<Long> query = getCurrentSession().createQuery(criteriaQuery);
        Long surveyRespondentCount = query.getSingleResult();
        return surveyRespondentCount;
    }

    public boolean surveyTitleDuplicateControl(String title){
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Survey> root = criteriaQuery.from(Survey.class);
        Predicate predicateIsDuplicateSurveyTitle = criteriaBuilder.equal(root.get("title"), title);
        criteriaQuery.select(criteriaBuilder.count(root)).where(predicateIsDuplicateSurveyTitle);
        Query<Long> query = getCurrentSession().createQuery(criteriaQuery);
        Long duplicateCount = query.getSingleResult();
        return (duplicateCount >0)?true:false;
    }

}
