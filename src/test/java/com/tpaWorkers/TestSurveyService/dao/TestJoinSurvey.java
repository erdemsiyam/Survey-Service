package com.tpaWorkers.TestSurveyService.dao;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.tpaWorkers.SurveyService.config.AppConfig;
import com.tpaWorkers.SurveyService.config.WebAppInitializer;
import com.tpaWorkers.SurveyService.config.WebConfig;
import com.tpaWorkers.SurveyService.dao.*;
import com.tpaWorkers.SurveyService.dao.model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebAppInitializer.class, AppConfig.class, WebConfig.class})
@Transactional(readOnly = false)
@WebAppConfiguration
@Rollback(value = false) // test verileri KAYDEDİLSİN.
public class TestJoinSurvey { // DAOs TEST
/*
    @Autowired
    private RespondentDAO respondentDAO;
    @Autowired
    private SurveyDAO surveyDAO;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private AnswerDAO answerDAO;
    @Autowired
    private RespondentAnswerDAO respondentAnswerDAO;

    // test için aşağıdakiler oluşturulur, ilk fonk'ta değerleri alınır, ikinci fonk'ta anket'e katılımcı için cevap eklenir.
    private Respondent testRespondent; //
    private Survey testSurvey;

    @Before // ilk bu fonksiyonu test et
    @Test(timeout = 300000L)
    public void testAddRespondent(){ // test için anket seç ve bir katılımcı oluştur.
        List<Survey> surveys = surveyDAO.getReadySurveys();
        Survey survey = surveys.get(0);
        testSurvey = survey; // test için kullanılacak anket alındı
        Person person = Fairy.create().person(); // FAKE DATA ile sahte katılımcı oluşturuluyor
        Respondent respondent = new Respondent();
        respondent.setSurvey(survey);
        respondent.setNameAndSurname(person.getFullName());
        respondent.setPhone(person.getTelephoneNumber());
        respondent.seteMail(person.getEmail());
        respondent.setRegisterDate(new Date());

        respondentDAO.create(respondent);
        Assert.assertTrue(respondent.getId() != null && respondent.getId() > 0);

        testRespondent = respondent;
    }
    @After // sonra bu fonksiyonu
    @Test(timeout = 300000L)
    public void testAddRespondentAnswer(){  // test için anket seçilip, katılımcı oluşturulduysa, katılımcı için ankete cevap ekleme testi yap.
        if(testSurvey == null || testRespondent == null) //  test edilecek anket yoksa veya katılımcı yoksa es geç
            return;
        List<Question> questions = questionDAO.getQuestionsBySurvey(testSurvey.getId()); // anket sorularını çek
        for(Question q : questions){
            List<Answer> asnwers = answerDAO.getAnswersByQuestion(q.getId()); // sıradaki sorunun cevaplarını çek.
            for(Answer a : asnwers){
                RespondentAnswer ra = new RespondentAnswer();
                ra.setRespondent(testRespondent);
                ra.setQuestion(q);
                ra.setAnswer(a);
                long idd = respondentAnswerDAO.create(ra);
                Assert.assertTrue(ra.getId() != null && ra.getId() > 0);
                break; // sıradaki soru için ilk cevap, katılımcı için ankete eklendi. diğer soruya geç.
            }
        }
    }
    */
}
