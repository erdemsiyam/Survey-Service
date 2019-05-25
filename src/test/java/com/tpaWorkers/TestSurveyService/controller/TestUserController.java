package com.tpaWorkers.TestSurveyService.controller;

import com.google.gson.Gson;
import com.tpaWorkers.SurveyService.dao.model.Answer;
import com.tpaWorkers.SurveyService.dao.model.Question;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import com.tpaWorkers.SurveyService.service.view_model.SurveyLookVM;
import com.tpaWorkers.SurveyService.service.view_model.SurveySingleVM;
import com.tpaWorkers.TestSurveyService.SuperTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestUserController extends SuperTest {

    @Test
    public void testGetReadySurveys(){
        try{
            // geriye List<SurveySingleVM> ' in JSON String hali döner yada hata nesnesi.
            // bu cevap string json'dan List<SurveySingleVM> ' e dönebilirse başarılı
            // cevap eğer List<SurveySingleVM> ' e dönemez (hata nesnesinin json'u) ise hata fırlatır catch'te de test fail yapılır.

            String response = userController.getReadySurveys();
            List<SurveySingleVM> ssvm = new Gson().fromJson(response, List.class);
            Assert.assertTrue( ssvm.size() > 0);
        }
        catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testRequestSurveyParticipation(){
        try{
            //anket oluşturulur
            Survey survey = createSurvey();
            Question question = createQuestion(survey);
            Answer answer = createAnswer(question);

            // anket katılım sayfası istenir.
            String response =userController.requestSurveyParticipation(survey.getId());
            // cevap olarak bize json nesne döner:
            // json nesne 2 ihtimallidir
            // ya SurveyLookVM yada hata mesajı nesnesi

            // eğer bu dönüşüm başarısız olursa, gelen hata mesajı nesnesi demektir, cathc'e gider test başarısız olur
            SurveyLookVM slvm = new Gson().fromJson(response,SurveyLookVM.class);
            Assert.assertNotNull(slvm);
        }
        catch (Exception e){
            Assert.fail();
        }
    }


}
