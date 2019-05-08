package com.tpaWorkers.TestSurveyService.dao;

import com.tpaWorkers.SurveyService.dao.model.Respondent;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TestSurveyDAO extends SuperDaoTest {

    @Test
    public void testGet(){
        Survey testSurvey = createSurvey();

        long testSurveyId = testSurvey.getId();

        Survey resultSurvey = surveyDAO.get(testSurveyId);
        Assert.assertTrue( resultSurvey.getId() != null && resultSurvey.getId() > 0);
    }

    @Test
    public void testCreate(){
        Survey survey = new Survey();
        survey.setTitle("Test Survey");
        survey.setExplanation("Test");
        survey.setEndDate(new Date(new Date().getTime() + 600)); // 10dk sonra
        survey.setStartDate(new Date());

        long resultId = surveyDAO.create(survey);
        Assert.assertTrue(resultId > 0);
    }

    @Test
    public void testList(){
        //test için bir anket oluşturulur
        Survey survey1 = createSurvey();

        List<Survey> testSurveys = surveyDAO.list();
        // en az bir anket olmalı
        Assert.assertTrue(testSurveys != null && testSurveys.size() >= 1);
    }

    @Test
    public void testRemove(){
        Survey testSurvey = createSurvey();

        Assert.assertTrue(surveyDAO.remove(testSurvey));
    }

    @Test
    public void testSetReady(){
        Survey testSurvey = createSurvey();

        // anketin Hazır Alanını TRUE yaptık
        // artık anketin Hazır olmuş olması TRUE gelmesi gerekir.
        Assert.assertTrue(surveyDAO.setReady(testSurvey.getId()));
    }

    @Test
    public void testGetNotReadySurveys(){
        //hazır olmayan bir anket oluşturulur
        Survey survey = createSurvey();

        List<Survey> surveys = surveyDAO.getNotReadySurveys();
        //en az bir hazır olmayan anket olmalı, yukarda oluşturduk ya hani.
        Assert.assertTrue(survey != null && surveys.size() >= 1);
    }

    @Test
    public void testGetReadySurveys(){
        // HAZIR bir anket oluşturulur, Hazır alanı TRUE işaretlenmiş anket
        Survey survey = createSurvey();
        surveyDAO.setReady(survey.getId());

        List<Survey> surveys = surveyDAO.getReadySurveys();
        // artık en az bir tane hazır anket var. aşağıdaki şart sağlanmalı
        Assert.assertTrue(surveys != null && surveys.size() >= 1);
    }

    @Test
    public void testGetRespondentCountForSurvey(){
        // bir anket oluşturup ona 2 tane katılımcı ekleriz.
        Survey survey = createSurvey();
        Respondent respondent1 = createRespondent(survey);
        Respondent respondent2 = createRespondent(survey);

        long respondentCount = surveyDAO.getRespondentCountForSurvey(survey.getId());
        // iki tane katılımcı olduğu şartını sağlamalı
        Assert.assertTrue(respondentCount == 2);
    }

    @Test
    public void testSurveyTitleDupliceControl(){
        Survey survey = createSurvey();

        // oluşturulan anket'in aynı başlığı fonksiyona gönderilir
        // haliyle aynı başlık kullanıldığından duplice olacağından True dönmeli
        boolean isDuplicate = surveyDAO.surveyTitleDuplicateControl(survey.getTitle());
        Assert.assertTrue(isDuplicate);
    }


}
