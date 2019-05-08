package com.tpaWorkers.TestSurveyService.dao;


import com.tpaWorkers.SurveyService.config.AppConfig;
import com.tpaWorkers.SurveyService.config.WebAppInitializer;
import com.tpaWorkers.SurveyService.config.WebConfig;
import com.tpaWorkers.SurveyService.dao.model.*;
import com.tpaWorkers.TestSurveyService.SuperTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebAppInitializer.class, AppConfig.class, WebConfig.class})
@Transactional(readOnly = false)
@WebAppConfiguration
@Rollback(value = true)
public class TestRespondentAnswerDAO extends SuperTest {

    @Test
    public void testRemove(){
        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);
        Respondent respondent = createRespondent(survey);

        RespondentAnswer testRespondentAnswer = createRespondentAnswer(answer,respondent);

        Assert.assertTrue(respondentAnswerDAO.remove(testRespondentAnswer));
    }

    @Test
    public void testCreate(){
        Survey survey = createSurvey(); // ilişkilendirilecek örnek modelleri oluştur
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);
        Respondent respondent = createRespondent(survey);

        RespondentAnswer testRespondentAnswer = new RespondentAnswer(); // test edilecek Katılımcı Cevabı oluştur
        testRespondentAnswer.setRespondent(respondent);
        testRespondentAnswer.setQuestion(question);
        testRespondentAnswer.setAnswer(answer);
        respondentAnswerDAO.create(testRespondentAnswer); // ekle

        // ekleme sonucu başarılı mı kontrol et.
        Assert.assertTrue(testRespondentAnswer.getId() != null && testRespondentAnswer.getId() > 0);

    }

    // To SurveyService

    @Test
    public void testHowManyTimeSelectedThisAnswer(){
        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);

        // iki tane aynı kişi aynı cevabı verdirdik.
        Respondent r1 = createRespondent(survey);
        Respondent r2 = createRespondent(survey);
        RespondentAnswer ra1 = createRespondentAnswer(answer,r1);
        RespondentAnswer ra2 = createRespondentAnswer(answer,r2);


        Long times = respondentAnswerDAO.howManyTimeSelectedThisAnswer(question.getId(),answer.getId());

        // 2 kez o cevabın seçilmiş olması gerekiyor.
        Assert.assertEquals(times.intValue(), 2);

        //ARDINDAN KAYDEDİLEN VERİLERİ SİL
        surveyDAO.remove(survey);
    }
    @Test
    public void testGetRespondentAnswersOfRespondent(){
        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);

        Respondent respondent = createRespondent(survey);
        // oluşturulan kişiyle bir soruya bir cevap verildi.
        createRespondentAnswer(answer,respondent);

        // kişi bir cevap verdiği için 1 e eşit olmalı
        List<RespondentAnswer> ras = respondentAnswerDAO.getRespondentAnswersOfRespondent(respondent.getId());
        Assert.assertTrue( ras != null && ras.size() == 1);
    }
    @Test
    public void testGetRespondentAnswersOfQuestion(){

        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);

        Respondent respondent = createRespondent(survey);
        // oluşturulan kişiyle bir soruya bir cevap verildi.
        createRespondentAnswer(answer,respondent);

        // 1 cevap verdiğimiz için 1 cevap olması gerekmekte.
        List<RespondentAnswer> ras = respondentAnswerDAO.getRespondentAnswersOfQuestion(question.getId());
        Assert.assertTrue(ras != null && ras.size() == 1);
    }

    // To Respondent Service

    @Test
    public void testGetRespondentAnswerForSurveyQuestion(){ // kişinin o soruya verdiği yanıt.
        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);
        Respondent respondent = createRespondent(survey);
        RespondentAnswer respondentAnswer = createRespondentAnswer(answer,respondent);

        long respondentId = respondent.getId();
        long questionId = question.getId();

        Answer testAnswer = respondentAnswerDAO.getRespondentAnswerForSurveyQuestion(respondentId,questionId);

        //kişi için verdiğimiz cevap ile bu kişi hangi cevabı seçti değerleri(cevaplar) aynı olmalı
        Assert.assertEquals(answer.getId(),testAnswer.getId());
    }

}
