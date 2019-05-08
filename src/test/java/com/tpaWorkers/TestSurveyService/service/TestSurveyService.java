package com.tpaWorkers.TestSurveyService.service;

import com.tpaWorkers.SurveyService.dao.model.Answer;
import com.tpaWorkers.SurveyService.dao.model.Question;
import com.tpaWorkers.SurveyService.dao.model.Respondent;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import com.tpaWorkers.SurveyService.exception.SurveyException;
import com.tpaWorkers.TestSurveyService.SuperTest;
import com.tpaWorkers.SurveyService.service.view_model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestSurveyService extends SuperTest {

    @Test
    public void testGetSingleSurvey(){
        try {
            Survey survey = createSurvey();
            SurveySingleVM ssvm = surveyService.getSingleSurvey(survey.getId());

            Assert.assertNotNull(ssvm);
        }
        catch (SurveyException e){
            Assert.fail();
        }
    }

    @Test
    public void testGetRespondentsBySurvey(){
        Survey survey = createSurvey();

        //iki katılımcı eklenir.
        Respondent respondent1 = createRespondent(survey);
        Respondent respondent2 = createRespondent(survey);

        //ve bu katılımcılar survey ile çekilip sayısı 2 mi kontrol edilir.
        try {
            List<RespondentSingleVM> respondents = surveyService.getRespondentsBySurvey(survey.getId());
            Assert.assertTrue(respondents != null && respondents.size() == 2);
        }
        catch (SurveyException e){
            Assert.fail();
        }
    }

}
