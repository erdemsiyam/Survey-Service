package com.tpaWorkers.SurveyService.service;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.tpaWorkers.SurveyService.dao.*;
import com.tpaWorkers.SurveyService.dao.model.*;
import com.tpaWorkers.SurveyService.service.post_model.RegisterSurveyModel;
import com.tpaWorkers.SurveyService.exception.SurveyException;
import com.tpaWorkers.SurveyService.service.view_model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class) // Propagation : transaction işlemleri aksamadan devam etsin. // rollbackFor : şuanki transaction işlemin hangi hata alınırsa geri alınacağı. Ne hata olursa geri al uygulama.
public class RespondentService {

    private static final Logger LOG = LoggerFactory.getLogger(RespondentService.class);

    @Autowired
    private SurveyDAO surveyDAO;
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private RespondentDAO respondentDAO;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private AnswerDAO answerDAO;
    @Autowired
    private RespondentAnswerDAO respondentAnswerDAO;


    public RespondentSingleVM.WithSurveyId getSingleRespondent(long respondentId) throws SurveyException {

        Respondent respondent = respondentDAO.get(respondentId);
        if (respondent == null)
            throw new SurveyException("Respondent Not Found.");

        RespondentSingleVM.WithSurveyId rs = new RespondentSingleVM.WithSurveyId();
        rs.setId(respondent.getId());
        rs.setNameAndSurname(respondent.getNameAndSurname());
        rs.setEMail(respondent.geteMail());
        rs.setPhone(respondent.getPhone());
        rs.setRegisterDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(respondent.getRegisterDate()));
        rs.setJoinedSurveyId(respondent.getSurvey().getId());
        return rs;

    } // Kendisi : diğer alanlar hariç
    public RespondentAnswersVM getResponderAnswersForASurvey(long respondentId,long surveyId) throws SurveyException {
        RespondentAnswersVM ravm = new RespondentAnswersVM();
        SurveySingleVM ssvm = surveyService.getSingleSurvey(surveyId);
        RespondentSingleVM rsvm = getSingleRespondent(respondentId);
        ravm.setSurvey(ssvm);
        ravm.setRespondent(rsvm);
        List<QuestionSingleVM> qsvms =new ArrayList<>();
        List<Question> questions = questionDAO.getQuestionsBySurvey(surveyId);
        for(Question question : questions){
            QuestionSingleVM qsvm = new QuestionSingleVM();
            qsvm.setId(question.getId());
            qsvm.setTitle(question.getTitle());
            Answer respondentAnswer = respondentAnswerDAO.getRespondentAnswerForSurveyQuestion(respondentId, question.getId());
            List<AnswerSingleVM> asvms = new ArrayList<>();
            AnswerSingleVM asvm = new AnswerSingleVM();
            asvm.setId(respondentAnswer.getId());
            asvm.setContent(respondentAnswer.getContent());
            asvms.add(asvm);
            qsvm.setAnswers(asvms);
            qsvms.add(qsvm);
        }
        ravm.setQuestions(qsvms);
        return ravm;
    } // katılım_bilgisi : istenen Survey'inin , Soru ve KendiCevapları + Survey bilgisi

    public List<RespondentSingleVM> getAllRespondent(){
        List<Respondent> respondents = respondentDAO.list();
        List<RespondentSingleVM> rsvms = new ArrayList<>();
        for(Respondent respondent : respondents){
            RespondentSingleVM rsvm = new RespondentSingleVM();
            rsvm.setId(respondent.getId());
            rsvm.setNameAndSurname(respondent.getNameAndSurname());
            rsvm.setEMail(respondent.geteMail());
            rsvm.setPhone(respondent.getPhone());
            rsvm.setRegisterDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(respondent.getRegisterDate()));
            rsvms.add(rsvm);
        }
        return rsvms;
    }

    @Transactional() // readOnly  = false.
    public String registerSurveyFromPostModel(long surveyId, RegisterSurveyModel model) throws SurveyException {

        Survey survey = surveyDAO.get(surveyId);
        if (survey == null)
            throw new SurveyException("Survey Not Found");
        if (!survey.getReady())
            throw new SurveyException("Survey Not Ready");
        if (survey.getStartDate().getTime() > new Date().getTime())
            throw new SurveyException("Error : Survey Not Started");
        if (survey.getEndDate().getTime() < new Date().getTime())
            throw new SurveyException("Error : Survey Has Been Finished at " + new SimpleDateFormat("yyyy/MM/dd HH:mm:SS").format(survey.getEndDate()));
        Respondent respondent = model.getRespondent().getRealRespondentObject();
        if (respondentDAO.hasThisPersonAnsweredSurveyBefore(respondent.geteMail(), surveyId))
            throw new SurveyException("You Already Answered This Survey");
        if (respondent.getNameAndSurname().equalsIgnoreCase("") ||
                respondent.geteMail().equalsIgnoreCase("") ||
                respondent.getPhone().equalsIgnoreCase(""))
            throw new SurveyException("Respondent Fields Must Be Not Empty");
        //questions bendeki ve ondakilerin sayısı eşit olmalı!
        List<Question> questions = questionDAO.getQuestionsBySurvey(surveyId);
        if (model.RespondentAnswers.size() != questions.size())
            throw new SurveyException("The Number Of Answers Does Not Coincide. Please Checkout");
        // Question : ondakiler için benimdekiler dön. eğer birisi benimkilerden denk bulamazsa patla
        for (Question q : questions) {
            boolean haveQuestion = false;
            for (RegisterSurveyModel.RespondentAnswerModel ram : model.getRespondentAnswers()) {
                if (q.getId() == ram.QuestionId) {
                    haveQuestion = true; // db deki soru kullanıcının gönderdiği soruyla eşleşti, bu döngü bitebilir
                    boolean haveAnswer = false;
                    List<Answer> answers = answerDAO.getAnswersByQuestion(q.getId());
                    for (Answer a : answers) {
                        if (a.getId() == ram.AnswerId) {
                            haveAnswer = true; // evet bu cevap bu sorunundur dedik.
                        }
                    }
                    if (!haveAnswer) {//bu answer bu sorunun mu, değilse patla
                        throw new SurveyException("Answer Not Specified Well. Please Checkout");
                    }
                    continue;
                }
            }
            if (!haveQuestion) { // anketin sorusu bizim gönderdiğimiz soru ve cevaplarında yoksa hata ver
                throw new SurveyException("Questions Entries Not Specified Well. Please Checkout");
            }
        }
        //en son hepsini sırasıyla kaydet
        respondent.setRegisterDate(new Date());
        respondent.setSurvey(survey);
        respondentDAO.create(respondent);
        for(RegisterSurveyModel.RespondentAnswerModel ram : model.getRespondentAnswers()){
            RespondentAnswer ra = new RespondentAnswer();
            Question question = questionDAO.get(ram.QuestionId);
            Answer answer = answerDAO.get(ram.AnswerId);
            ra.setRespondent(respondent);
            ra.setQuestion(question);
            ra.setAnswer(answer);
            respondentAnswerDAO.create(ra);
        }

        return "{\"Done\":\"Your Survey Participation Completed Successfully !\"}";
    }

    @Transactional()
    public String deleteRespondent(long respondentId) throws SurveyException {

        Respondent respondent = respondentDAO.get(respondentId);//respondent çek
        if (respondent == null)
            throw new SurveyException("Respondent Not Found.");
        List<RespondentAnswer> ras = respondentAnswerDAO.getRespondentAnswersOfRespondent(respondentId);//respondent answerlaı çek sil
        for(RespondentAnswer ra : ras){
            respondentAnswerDAO.remove(ra);
        }
        respondentDAO.remove(respondent);//respondent sil


        return "{\"Done\":\"Respondent Deleted Successfully.\"}";
    }

    // BOT
    @Transactional()
    public String addBotRespondentsToSurvey(long surveyId,long respondentCount) throws SurveyException {
        Survey survey = surveyDAO.get(surveyId);
        if(survey == null)
            throw new SurveyException("Survey Not Found.");
        for(int i=0; i<respondentCount; i++){
            Person person = Fairy.create().person(); // FAKE DATA ile sahte katılımcı oluşturuluyor
            Respondent respondent = new Respondent();
            respondent.setNameAndSurname(person.getFullName());
            respondent.seteMail(person.getEmail());
            respondent.setPhone(person.getTelephoneNumber());
            respondent.setRegisterDate(new Date());
            respondent.setSurvey(survey);
            respondentDAO.create(respondent);
            List<Question> questions = questionDAO.getQuestionsBySurvey(surveyId); // sorular
            for(Question question : questions){
                List<Answer> answers = answerDAO.getAnswersByQuestion(question.getId()); // cevaplar
                RespondentAnswer respondentAnswer = new RespondentAnswer(); // KatılımcıCevabı oluşturma
                respondentAnswer.setQuestion(question);
                respondentAnswer.setRespondent(respondent);
                Answer randomAnswer = answers.get(new Random().nextInt(answers.size())); // rastgele cevap alma
                respondentAnswer.setAnswer(randomAnswer);
                respondentAnswerDAO.create(respondentAnswer); // katılımcıCevabı db'ye yükleme
            }
        }
        return "{\"Done\":\""+respondentCount +" Respondent Added."+"\"}";
    }
}
