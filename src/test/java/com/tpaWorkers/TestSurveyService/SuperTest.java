package com.tpaWorkers.TestSurveyService;

import com.tpaWorkers.SurveyService.config.AppConfig;
import com.tpaWorkers.SurveyService.config.WebAppInitializer;
import com.tpaWorkers.SurveyService.config.WebConfig;
import com.tpaWorkers.SurveyService.controller.UserController;
import com.tpaWorkers.SurveyService.dao.*;
import com.tpaWorkers.SurveyService.dao.model.*;
import com.tpaWorkers.SurveyService.service.SurveyService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebAppInitializer.class, AppConfig.class, WebConfig.class})
@Transactional() //readOnly = false
@WebAppConfiguration
@Rollback() // value = true
//@Ignore //test yapmaz
public class SuperTest {

    @Autowired
    protected RespondentAnswerDAO respondentAnswerDAO;

    @Autowired
    protected RespondentDAO respondentDAO;

    @Autowired
    protected QuestionDAO questionDAO;

    @Autowired
    protected AnswerDAO answerDAO;

    @Autowired
    protected SurveyDAO surveyDAO;

    @Autowired
    protected SurveyService surveyService;

    @Autowired
    protected UserController userController;

    // Örnek Dao - Model Oluşturma.
    protected Survey createSurvey(){
        Survey survey = new Survey();
        survey.setTitle("Test Survey");
        survey.setExplanation("Test");
        survey.setEndDate(new Date(new Date().getTime() + 600)); // 10dk sonra
        survey.setStartDate(new Date());
        surveyDAO.create(survey);
        return survey;
    }
    protected Question createQuestion(Survey survey){
        Question question = new Question();
        question.setTitle("Test Question");
        question.setSurvey(survey);
        questionDAO.create(question);
        return question;
    }
    protected Answer createAnswer(Question question){
        Answer answer = new Answer();
        answer.setContent("Test Asnwer");
        answer.setQuestion(question);
        answerDAO.create(answer);
        return answer;
    }
    protected Respondent createRespondent(Survey survey){
        Respondent respondent = new Respondent();
        respondent.setNameAndSurname("Test Respondent");
        respondent.seteMail("Test Mail");
        respondent.setPhone("000");
        respondent.setRegisterDate(new Date());
        respondent.setSurvey(survey);
        respondentDAO.create(respondent);
        return respondent;
    }
    protected RespondentAnswer createRespondentAnswer(Answer answer, Respondent respondent){
        RespondentAnswer respondentAnswer = new RespondentAnswer();
        respondentAnswer.setRespondent(respondent);
        respondentAnswer.setQuestion(answer.getQuestion());
        respondentAnswer.setAnswer(answer);
        respondentAnswerDAO.create(respondentAnswer);
        return respondentAnswer;
    }
}
