package com.tpaWorkers.SurveyService.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tpaWorkers.SurveyService.service.post_model.RegisterSurveyModel;
import com.tpaWorkers.SurveyService.exception.SurveyException;
import com.tpaWorkers.SurveyService.service.RespondentService;
import com.tpaWorkers.SurveyService.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController // @Controller + @ResponseBody demektir.
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/api/user/*",consumes = {MediaType.ALL_VALUE},produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}) // her şey kabul, sadece json UTF-8 üretir.
public class UserController {

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private RespondentService respondentService;


    // SURVEY SERVICE

    @GetMapping(value = "/surveys")
    public String getReadySurveys()  {
        String response = new Gson().toJson(surveyService.getReadySurveys());
        return response;
    }

    @GetMapping(value = "/survey/{id}/join")
    public String requestSurveyParticipation(@PathVariable("id")long surveyId)  {
        String response;
        try {
            response = new Gson().toJson(surveyService.requestSurveyParticipation(surveyId));
        } catch (SurveyException e) {
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        return response;
    }

    // RESPONDENT SERVICE

    @PostMapping(value = "/survey/{id}/join", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String registerSurvey(@PathVariable("id")long surveyId,@RequestBody String body){
        RegisterSurveyModel rsm;
        String response;
        try {
            rsm = new Gson().fromJson(body, RegisterSurveyModel.class);
            response = respondentService.registerSurveyFromPostModel(surveyId,rsm);
        }
        catch (SurveyException e){
            response = "{\"Error\":\""+e.getMessage()+"\"}";
        }
        catch (NullPointerException e){
            response = "{\"Error\":\""+"Make Sure Survey Response Model Designed Correctly."+"\"}";
        }
        catch (JsonSyntaxException e){
            response = "{\"Error\":\""+"Make Sure Survey Response Model Designed Correctly."+"\"}";
        }
        catch (Exception e){
            e.printStackTrace();
            response = "{\"Error\":\"Server Error.\"}";
        }
        return response;
    }

}