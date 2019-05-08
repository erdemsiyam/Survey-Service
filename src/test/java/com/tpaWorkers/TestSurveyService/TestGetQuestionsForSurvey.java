package com.tpaWorkers.TestSurveyService;

import com.tpaWorkers.SurveyService.config.AppConfig;
import com.tpaWorkers.SurveyService.config.WebAppInitializer;
import com.tpaWorkers.SurveyService.config.WebConfig;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import com.tpaWorkers.SurveyService.service.SurveyService;
import com.tpaWorkers.SurveyService.service.view_model.SurveyLookVM;
import com.tpaWorkers.SurveyService.service.view_model.SurveySingleVM;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebAppInitializer.class, AppConfig.class, WebConfig.class})
@Transactional(readOnly = true)
@WebAppConfiguration
@Rollback(value = false) // test verileri KAYDEDİLSİN.
public class TestGetQuestionsForSurvey { // SERVICE TEST
/*
    @Autowired
    private SurveyService surveyService;
//test düzelt
    @Test
    public void getQuestionsForSurvey(){

        try {
            List<SurveySingleVM> surveys = surveyService.getAllSurvey(); // servisten bütün anketler ViewModel oalrak çekilir
            SurveySingleVM ssvm = surveys.get(0); // anket varsa ilkini al test için
            SurveyLookVM slvm = surveyService.requestSurveyParticipation(ssvm.getId()); // ilkinin geniş halini al, sorulu ve cevaplı.
            Assert.assertFalse(slvm.getQuestions().size() < 1); // her anketin en az bir sorusu olacağından. 1 den az sorusu varsa hata döndür
        }
        catch (Exception e){
            Assert.assertTrue(false); // hata varsa direkt test başarısız yap.
        }
    }
*/
}
