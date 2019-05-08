package com.tpaWorkers.SurveyService.dao;


import com.tpaWorkers.SurveyService.dao.model.Answer;
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

@Repository("AnswerDAO")
public class AnswerDAO {
    @Autowired
    protected SessionFactory sessionFactory;
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public Answer get(long id){
        return getCurrentSession().get(Answer.class,id);
    }
    public long create(Answer answer) {
        getCurrentSession().save(answer);
        return answer.getId();
    }
    public boolean remove(Answer answer){
        try {
            getCurrentSession().remove(answer);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    //our funcs
    // To SurveyService, QuestionService , RespondentService
    public List<Answer> getAnswersByQuestion(long questionId){
        // soruya göre cevapları çek
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Answer> criteriaQuery = criteriaBuilder.createQuery(Answer.class);

        Root<Answer> root = criteriaQuery.from(Answer.class);
        Predicate predicateQuestionAnswers = criteriaBuilder.equal(root.get("question"),questionId);
        criteriaQuery.select(root).where(predicateQuestionAnswers);

        Query<Answer> query = getCurrentSession().createQuery(criteriaQuery);
        List<Answer> questionAnswers = query.getResultList();
        return questionAnswers;
    }

}
