package com.tpaWorkers.SurveyService.dao;


import com.tpaWorkers.SurveyService.dao.model.Answer;
import com.tpaWorkers.SurveyService.dao.model.RespondentAnswer;
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

@Repository("RespondentAnswerDAO")
public class RespondentAnswerDAO {
    @Autowired
    protected SessionFactory sessionFactory;
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public boolean remove(RespondentAnswer ra){
        try {
            getCurrentSession().remove(ra);
            getCurrentSession().flush(); // RespondenAnswer sil deyince silmiyordu bununla senkronize yaptık veritabanı.
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public long create(RespondentAnswer respondentAnswer) {
        getCurrentSession().save(respondentAnswer);
        return respondentAnswer.getId();
    }

    // To SurveyService
    public long howManyTimeSelectedThisAnswer(long questionId, long answerId){
        // soru ve cevap idlerinin beraber kullanıldığı KAyıt sayısını getir.
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<RespondentAnswer> root = criteriaQuery.from(RespondentAnswer.class);
        Predicate predicateAnswerIdEqueals = criteriaBuilder.equal(root.get("answer"),answerId);
        Predicate predicateQuestionIdEquals= criteriaBuilder.equal(root.get("question"),questionId);

        criteriaQuery.select(criteriaBuilder.count(root)).where(predicateAnswerIdEqueals,predicateQuestionIdEquals);

        Query<Long> query = getCurrentSession().createQuery(criteriaQuery);
        Long answerSelectedCounts = query.getSingleResult();
        return answerSelectedCounts;
    }
    public List<RespondentAnswer> getRespondentAnswersOfRespondent(long respondentId){
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<RespondentAnswer> criteriaQuery = criteriaBuilder.createQuery(RespondentAnswer.class);

        Root<RespondentAnswer> root = criteriaQuery.from(RespondentAnswer.class);
        Predicate predicateRespondentEquals = criteriaBuilder.equal(root.get("respondent"),respondentId);

        criteriaQuery.select(root).where(predicateRespondentEquals);

        Query<RespondentAnswer> query = getCurrentSession().createQuery(criteriaQuery);
        List<RespondentAnswer> respondentAnswers = query.getResultList();

        return respondentAnswers;
    }
    public List<RespondentAnswer> getRespondentAnswersOfQuestion(long questionId){
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<RespondentAnswer> criteriaQuery = criteriaBuilder.createQuery(RespondentAnswer.class);

        Root<RespondentAnswer> root = criteriaQuery.from(RespondentAnswer.class);
        Predicate predicateQuestionEquals = criteriaBuilder.equal(root.get("question"),questionId);

        criteriaQuery.select(root).where(predicateQuestionEquals);

        Query<RespondentAnswer> query = getCurrentSession().createQuery(criteriaQuery);
        List<RespondentAnswer> respondentAnswers = query.getResultList();

        return respondentAnswers;
    }
    public List<RespondentAnswer> getRespondentAnswersOfAnswer(long answerId){
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<RespondentAnswer> criteriaQuery = criteriaBuilder.createQuery(RespondentAnswer.class);

        Root<RespondentAnswer> root = criteriaQuery.from(RespondentAnswer.class);
        Predicate predicateQuestionEquals = criteriaBuilder.equal(root.get("answer"),answerId);

        criteriaQuery.select(root).where(predicateQuestionEquals);

        Query<RespondentAnswer> query = getCurrentSession().createQuery(criteriaQuery);
        List<RespondentAnswer> respondentAnswers = query.getResultList();

        return respondentAnswers;
    }


    // To RespondentService
    public Answer getRespondentAnswerForSurveyQuestion(long respondentId, long questionId){
        // KatılımcıCevap tablo , respondentid ve questionid nin olduğu kayıt satırının answer 'ini dön

        // RespondentAnswer al
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<RespondentAnswer> criteriaQuery = criteriaBuilder.createQuery(RespondentAnswer.class);

        Root<RespondentAnswer> root = criteriaQuery.from(RespondentAnswer.class);
        Predicate perdicateEqualsRespondentId = criteriaBuilder.equal(root.get("respondent"),respondentId);
        Predicate perdicateEqualsQuestionId = criteriaBuilder.equal(root.get("question"),questionId);
        criteriaQuery.select(root).where(perdicateEqualsRespondentId,perdicateEqualsQuestionId); // OR DEĞİL AND YAP

        Query<RespondentAnswer> query = getCurrentSession().createQuery(criteriaQuery);
        RespondentAnswer ra = query.getSingleResult();

        // Answer Al

        CriteriaBuilder criteriaBuilder2 = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Answer> criteriaQuery2 = criteriaBuilder2.createQuery(Answer.class);

        Root<Answer> root2 = criteriaQuery2.from(Answer.class);
        Predicate perdicateAnswerId = criteriaBuilder2.equal(root2.get("id"),ra.getAnswer().getId()); // hata olabilir. RA ' nın answer idsini al buraya ver
        criteriaQuery2.select(root2).where(perdicateAnswerId);

        Query<Answer> query2 = getCurrentSession().createQuery(criteriaQuery2);
        Answer answer = query2.getSingleResult();

        return answer;
    }
}
