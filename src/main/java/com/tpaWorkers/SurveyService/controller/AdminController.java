package com.tpaWorkers.SurveyService.controller;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tpaWorkers.SurveyService.service.post_model.AddAnswerModel;
import com.tpaWorkers.SurveyService.service.post_model.AddQuestionModel;
import com.tpaWorkers.SurveyService.service.post_model.CreateSurveyModel;
import com.tpaWorkers.SurveyService.exception.SurveyException;
import com.tpaWorkers.SurveyService.service.AnswerService;
import com.tpaWorkers.SurveyService.service.QuestionService;
import com.tpaWorkers.SurveyService.service.RespondentService;
import com.tpaWorkers.SurveyService.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/api/admin/*",consumes = {MediaType.ALL_VALUE},produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class AdminController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private RespondentService respondentService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;

    @GetMapping(value = "/surveys")
    public String getAllSurveys()  {
        String response;
        try {
            response = new Gson().toJson(surveyService.getAllSurvey());
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @GetMapping(value = "/survey/{id}")
    public String getSingleSurvey(@PathVariable("id")long surveyId, HttpServletResponse r)  {
        String response;
        try {
            response = new Gson().toJson(surveyService.requestSurveyParticipation(surveyId));
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @GetMapping(value = "/survey/{id}/respondents")
    public String getRespondentsBySurvey(@PathVariable("id")long surveyId)  {
        String response;
        try {
            response = new Gson().toJson(surveyService.getRespondentsBySurvey(surveyId));
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @GetMapping(value = "/survey/{id}/results")
    public String getSurveyResults(@PathVariable("id")long surveyId)  {
        String response;
        try {
            response = new Gson().toJson(surveyService.getSurveyResults(surveyId));
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @GetMapping(value = "/surveys/notready")
    public String getNotReadySurveys()  {
        String response;
        try {
            response = new Gson().toJson(surveyService.getNotReadySurveys());
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @PutMapping(value = "/survey/{id}/start")
    public String setSurveyReady(@PathVariable("id")long surveyId)  {
        String response;
        try {
            response = surveyService.setSurveyReady(surveyId);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (Exception e){
            e.printStackTrace();
            response = "{\"Error\":\"Server Error.\"}";
        }
        printLog(response);
        return response;
    }

    @PostMapping(value = "/survey/create")
    public String createSurvey(@RequestBody String body){
        CreateSurveyModel csm;
        String response ;
        try {
            csm = new Gson().fromJson(body, CreateSurveyModel.class);
            response = surveyService.createSurveyFromAbstractModel(csm);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (JsonSyntaxException e){
            response = "{\"Error\":\"Make Sure Survey Create Model Designed Correctly.\"}";
        }
        catch (NullPointerException e){
            response = "{\"Error\":\"Make Sure Survey Create Model Designed Correctly.\"}";
        }
        printLog(response);
        return response;
    }

    @PostMapping(value = "/survey/{id}/addquestions")
    public String addQuestionsToSurvey(@PathVariable("id")long surveyId,@RequestBody String body){
        String response;
        AddQuestionModel aqm ;
        try {
            aqm = new Gson().fromJson(body,AddQuestionModel.class);
            response = surveyService.addQuestionsAndAnswersToSurvey(surveyId,aqm);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (NullPointerException e){
            response = "{\"Error\":\""+"Make Sure Add Questions Model Designed Correctly."+"\"}";
        }
        catch (JsonSyntaxException e){
            response = "{\"Error\":\"Make Sure Add Questions Model Designed Correctly.\"}";
        }
        catch (Exception e){
            e.printStackTrace();
            response = "{\"Error\":\"Server Error.\"}";
        }
        printLog(response);
        return response;
    }

    @DeleteMapping(value = "/survey/{id}/delete")
    public String deleteSurvey (@PathVariable("id")long surveyId){
        String response;
        try{
            response = surveyService.deleteSurvey(surveyId);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (Exception e){
            e.printStackTrace();;
            response = "{\"Error\":\"Server Error.\"}";
        }
        printLog(response);
        return response;
    }


    // RESPONDENT SERVICE

    @GetMapping(value = "/survey/{id1}/respondent/{id2}")
    public String getResponderAnswersForASurvey(@PathVariable("id1")long surveyId,@PathVariable("id2")long respondentId)  {
        String response;
        try {
            response = new Gson().toJson(respondentService.getResponderAnswersForASurvey(respondentId,surveyId));
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @GetMapping(value = "/respondents")
    public String getAllRespondent()  {
        String response = new Gson().toJson(respondentService.getAllRespondent());
        printLog(response);
        return response;
    }

    @GetMapping(value = "/respondent/{id}")
    public String getSingleRespondent(@PathVariable("id")long respondentId)  {
        String response;
        try {
            response = new Gson().toJson(respondentService.getSingleRespondent(respondentId));
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @DeleteMapping(value = "/respondent/{id}/delete")
    public String deleteRespondent (@PathVariable("id")long respondentId){
        String response;
        try{
            response = respondentService.deleteRespondent(respondentId);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (Exception e){
            e.printStackTrace();
            response = "{\"Error\":\"Server Error.\"}";
        }
        printLog(response);
        return response;
    }


    // QUESTION SERVICE

    @GetMapping(value = "/questions")
    public String getAllQuestions()  {
        String response;
        try {
            response = new Gson().toJson(questionService.getAllQuestions());
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @GetMapping(value = "/question/{id}")
    public String getSingleQuestion(@PathVariable("id")long questionId)  {
        String response;
        try {
            response = new Gson().toJson(questionService.getSingleQuestion(questionId));
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        printLog(response);
        return response;
    }

    @DeleteMapping(value = "/question/{id}/delete")
    public String deleteQuestion (@PathVariable("id")long questionId){
        String response ;
        try{
            response = questionService.deleteQuestion(questionId);
        }catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (Exception e){
            e.printStackTrace();
            response = "{\"Error\":\"Server Error.\"}";
        }
        printLog(response);
        return response;
    }


    // ANSWER SERVICE

    @PostMapping(value = "/question/{id}/addanswers")
    public String addAnswersToQuestion(@PathVariable("id")long questionId,@RequestBody String body){
        AddAnswerModel aam ;
        String response;
        try {
            aam = new Gson().fromJson(body,AddAnswerModel.class);
            response = answerService.addAnswersToQuestion(questionId,aam);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (NullPointerException e){
            response = "{\"Error\":\""+"Make Sure Add Answer Model Designed Correctly."+"\"}";
        }
        catch (JsonSyntaxException e){
            response = "{\"Error\":\"Make Sure Add Answer Model Designed Correctly.\"}";
        }
        catch (Exception e){
            e.printStackTrace();
            response = "{\"Error\":\"Server Error.\"}";
        }
        printLog(response);
        return response;
    }

    @DeleteMapping(value = "/answer/{id}/delete")
    public String deleteAnswer (@PathVariable("id")long answerId){
        String response;
        try{
            response = answerService.deleteAnswer(answerId);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (Exception e){
            e.printStackTrace();
            response = "{\"Error\":\"Server Error.\"}";
        }
        printLog(response);
        return response;
    }

    // BOT
    @GetMapping(value="/survey/{id1}/addbotrespondents/{id2}")
    public String addBotRespondentsToSurvey(@PathVariable("id1") long surveyId,@PathVariable("id2") long respondentCount){
        String response;
        try{
            response = respondentService.addBotRespondentsToSurvey(surveyId,respondentCount);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (Exception e){
            response = "{\"Error\":\"Server Error.\"}";
        }
        printLog(response);
        return response;
    }

    private void printLog(String content){
        LOG.info("Response '"+content+"'");
    }
}
