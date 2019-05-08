package com.tpaWorkers.TestSurveyService.dao;

import com.tpaWorkers.SurveyService.dao.model.*;
import com.tpaWorkers.TestSurveyService.SuperTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TestRespondentDAO  extends SuperTest {

    @Test
    public void testCreate(){
        Survey survey = createSurvey();
        Question question = createQuestion(survey);

        Respondent testRespondent = new Respondent();
        testRespondent.setNameAndSurname("Test Respondent");
        testRespondent.seteMail("Test Mail");
        testRespondent.setPhone("000");
        testRespondent.setRegisterDate(new Date());
        testRespondent.setSurvey(survey);
        respondentDAO.create(testRespondent);

        Assert.assertTrue( testRespondent.getId() != null && testRespondent.getId() > 0);
    }

    @Test
    public void testGet(){
        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Respondent testRespondent = createRespondent(survey);

        Long testRespondentId = testRespondent.getId();

        Respondent resultRespondent = respondentDAO.get(testRespondentId);

        Assert.assertNotNull(resultRespondent);
    }

    @Test
    public void testList(){
        Survey survey = createSurvey();
        Respondent respondent = createRespondent(survey);

        List<Respondent> testRespondentList = respondentDAO.list();
        //yukarda bir ankete bir kişi ekledik. anket katılımcı sayımız en az 1, ve en az 1 olmalı şartta
        Assert.assertTrue(testRespondentList != null && testRespondentList.size() >= 1);
    }

    @Test
    public void testRemove(){
        Survey survey = createSurvey();
        Respondent respondent = createRespondent(survey);

        Assert.assertTrue(respondentDAO.remove(respondent));
    }

    @Test
    public void testHasThisPersonAnsweredSurveyBefore(){
        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);
        Respondent respondent = createRespondent(survey);
        RespondentAnswer respondentAnswer = createRespondentAnswer(answer,respondent);
        //bir kişi için bir ankete bir cevap verdik.

        // bu kişinin ankete önceden cevap verdiğini söylemeli true dönmeli.
        Assert.assertTrue(respondentDAO.hasThisPersonAnsweredSurveyBefore(respondent.geteMail(),survey.getId()));
    }

    // To SurveyService

    @Test
    public void testGetRespondentsBySurvey(){
        // bir anket için iki katılımcı ekleyelim
        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);

        // iki tane aynı kişi aynı cevabı verdirdik.
        Respondent r1 = createRespondent(survey);
        Respondent r2 = createRespondent(survey);
        RespondentAnswer ra1 = createRespondentAnswer(answer,r1);
        RespondentAnswer ra2 = createRespondentAnswer(answer,r2);

        //test'te geri gelen katılımcı sayısı 2 olmalı !
        List<Respondent> testRespondents = respondentDAO.getRespondentsBySurvey(survey.getId());
        Assert.assertTrue(testRespondents != null && testRespondents.size() == 2);
    }


}
