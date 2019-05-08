package com.tpaWorkers.TestSurveyService.dao;

import com.tpaWorkers.SurveyService.config.AppConfig;
import com.tpaWorkers.SurveyService.config.WebAppInitializer;
import com.tpaWorkers.SurveyService.config.WebConfig;
import com.tpaWorkers.SurveyService.dao.QuestionDAO;
import com.tpaWorkers.SurveyService.dao.SurveyDAO;
import com.tpaWorkers.SurveyService.dao.model.Question;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class TestQuestionDAO extends SuperDaoTest{

    @Test
    public void testGet(){
        Question testQuestion = createQuestion(createSurvey());
        long testQuestionId = testQuestion.getId(); // test edilcek SORU oluşturuldu

        Question question = questionDAO.get(testQuestionId); // SORU yu çek
        Assert.assertNotNull(question); // SORU çekme işlemi başarılı mı kontrol
    }

    @Test
    public void testCreate(){
        //oluşturulacak soru için mecbur bir anket gerekir onu oluştur
        Survey survey = createSurvey();

        Question testQuestion = new Question(); // test edilecek soru oluşturulur
        testQuestion.setTitle("Test Question");
        testQuestion.setSurvey(survey); // anketi verilir.

        questionDAO.create(testQuestion); // soruyu ekleme denenir.
        //soru eklendi mi et.
        Assert.assertTrue(testQuestion.getId() != null && testQuestion.getId() > 0);
    }

    @Test
    public void testList(){

        // bir soru oluşturduk, db'de ekli en az 1 soru olduğunu biliyoruz.
        createQuestion(createSurvey());

        List<Question> list = questionDAO.list();
        Assert.assertTrue(list.size() >= 1); // en az 1 soru olmalı.
    }

    @Test
    public void testRemove(){
        // soru ve ona bağlı mecburi anket oluştu.
        Question testQuestion = createQuestion(createSurvey());

        Assert.assertTrue(questionDAO.remove(testQuestion)); // silme testi.
    }

    // To SurveyService, RespondentService
    @Test
    public void testGetQuestionsBySurvey(){
        Survey survey = createSurvey();
        createQuestion(survey); // bir anket ve ona bir soru oluşturduk.

        long testSurveyId = survey.getId(); // soruları çekilecek anket id'si alındı.

        List<Question> testQuestions = questionDAO.getQuestionsBySurvey(testSurveyId); // anketin sorularını çek
        Assert.assertTrue(testQuestions != null && testQuestions.size() >= 1);// en az 1 soru olmalı, yukarda 1tane soru verdik ona
    }
}
