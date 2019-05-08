package com.tpaWorkers.SurveyService.dao;


import com.tpaWorkers.SurveyService.dao.model.Question;
import com.tpaWorkers.SurveyService.dao.model.Survey;
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

@Repository("QuestionDAO")
public class QuestionDAO {
    @Autowired
    protected SessionFactory sessionFactory;
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public Question get(long id){
        return getCurrentSession().get(Question.class,id);
    }
    public long create(Question question) {
        getCurrentSession().save(question);
        return question.getId();
    }
    public List<Question> list() {
        return getCurrentSession().createQuery("from Question").list();
    }
    public boolean remove(Question question){
        try {
            getCurrentSession().remove(question);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    // our funcs

    // To SurveyService , RespontentService
    public List<Question> getQuestionsBySurvey(long surveyId){
        // ankete göre soruları çek
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Question> criteriaQuery = criteriaBuilder.createQuery(Question.class);

        Root<Question> root = criteriaQuery.from(Question.class);
        Predicate predicateSurveyQuestions = criteriaBuilder.equal(root.get("survey"),surveyId);
        criteriaQuery.select(root).where(predicateSurveyQuestions);

        Query<Question> query = getCurrentSession().createQuery(criteriaQuery);
        List<Question> surveyQuestions = query.getResultList();
        return surveyQuestions;
    }
}
