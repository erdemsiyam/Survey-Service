package com.tpaWorkers.SurveyService.service;

import com.tpaWorkers.SurveyService.dao.*;
import com.tpaWorkers.SurveyService.dao.model.*;
import com.tpaWorkers.SurveyService.service.post_model.AddQuestionModel;
import com.tpaWorkers.SurveyService.service.post_model.CreateSurveyModel;
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

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class) // Propagation : transaction işlemleri aksamadan devam etsin. // rollbackFor : şuanki transaction işlemin hangi hata alınırsa geri alınacağı. Ne hata olursa geri al uygulama.
public class SurveyService {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyService.class);

    @Autowired
    private SurveyDAO surveyDAO;
    @Autowired
    private RespondentDAO respondentDAO;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private AnswerDAO answerDAO;
    @Autowired
    private RespondentAnswerDAO respondentAnswerDAO;

    public SurveySingleVM getSingleSurvey(long surveyId) throws SurveyException { //Kendisi : diğer alanlar hariç

        Survey survey = surveyDAO.get(surveyId);
        if (survey == null)
            throw new SurveyException("Survey Not Found.");

        SurveySingleVM svm = new SurveySingleVM();
        svm.setId(survey.getId());
        svm.setTitle(survey.getTitle());
        svm.setExplanation(survey.getExplanation());
        svm.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(survey.getStartDate()));
        svm.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(survey.getEndDate()));
        svm.setReady(survey.getReady());
       return svm ;
    } //Kendisi : diğer alanlar hariç
    public List<RespondentSingleVM> getRespondentsBySurvey(long surveyId) throws SurveyException {

        Survey survey = surveyDAO.get(surveyId);
        if (survey == null)
            throw new SurveyException("Survey Not Found.");
        List<Respondent> respondents = respondentDAO.getRespondentsBySurvey(surveyId);

        List<RespondentSingleVM> rsvms = new ArrayList<>();
        for(Respondent r : respondents){
            RespondentSingleVM rsvm = new RespondentSingleVM();
            rsvm.setId(r.getId());
            rsvm.setNameAndSurname(r.getNameAndSurname());
            rsvm.setEMail(r.geteMail());
            rsvm.setPhone(r.getPhone());
            rsvm.setRegisterDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(r.getRegisterDate()));
            rsvms.add(rsvm);
        }
        return rsvms;
    } // Katılımcı_lar : Katılımcı(Kendisi)

    public SurveyResultVM getSurveyResults(long surveyId) throws SurveyException {
        SurveyResultVM sr = new SurveyResultVM();
        sr.setSurvey(getSingleSurvey(surveyId));
        sr.setRespondentCount(surveyDAO.getRespondentCountForSurvey(surveyId));
        List<QuestionSingleVM> qs = new ArrayList<>();
        List<Question> questions = questionDAO.getQuestionsBySurvey(surveyId);
        for(Question question : questions){
            QuestionSingleVM q = new QuestionSingleVM();
            q.setId(question.getId());
            q.setTitle(question.getTitle());
            List<Answer> answers = answerDAO.getAnswersByQuestion(question.getId());
            List<AnswerSingleVM> asvms = new ArrayList<>();
            for(Answer answer : answers){
                AnswerSingleVM asvm = new AnswerSingleVM();
                asvm.setId(answer.getId());
                asvm.setContent(answer.getContent());
                asvm.setSelectedCount(respondentAnswerDAO.howManyTimeSelectedThisAnswer(question.getId(), answer.getId()));
                asvms.add(asvm);
            }
            q.setAnswers(asvms);
            qs.add(q);
        }
        sr.setQuestions(qs);
        return sr;
    }

    @Transactional() // default, readOnly = false;
    public String createSurveyFromAbstractModel(CreateSurveyModel model) throws SurveyException {
        Survey survey;
        String returnMessage = "";
            // Survey İçerik Kontrolü
            survey = model.Survey.getRealSurveyObject();
            if (survey.getTitle().equalsIgnoreCase(""))
                throw new SurveyException("Title Can Not Empty");
            if (survey.getExplanation().equalsIgnoreCase(""))
                throw new SurveyException("Explanation Can Not Empty");
            if (surveyDAO.surveyTitleDuplicateControl(survey.getTitle()))
                throw new SurveyException("This Title Using");
            if (survey.getEndDate().getTime() <= new Date().getTime() || survey.getEndDate().getTime() <= survey.getStartDate().getTime())
                throw new SurveyException("End Date Can Not Before Now Or Start Date");
            //Question ve Answer içerikleri kontrolü.
            if (model.Questions.size() <= 0)
                throw new SurveyException("Questions Count Must Greater Than 0");
            for (CreateSurveyModel.QuestionModel qm1 : model.Questions) {
                int qCount = 0; // aynı başlıklı soru varsa patlat, onun için sayaç açtık
                for(CreateSurveyModel.QuestionModel qm2 : model.Questions){
                    if(qm1.getTitle().trim().equalsIgnoreCase(qm2.getTitle().trim())){
                        qCount++;
                    }
                }
                if(qCount > 1){// aynı başlıklı soru varsa patlat
                    throw new SurveyException("You Can Not Ask Same Question \""+qm1.getTitle().trim()+"\"");
                }
                Question question = qm1.getRealQuestionObject();
                if (question.getTitle().equalsIgnoreCase(""))
                    throw new SurveyException("Question Title Can Not Empty");
                if(qm1.getAnswers().size() <= 1)
                    throw new SurveyException("Answers Count Must Greater Than 1");
                for (CreateSurveyModel.AnswerModel am1 : qm1.getAnswers()) {
                    int aCount = 0; // aynı içerikli cevap varsa patlat, onun için sayaç açtık
                    for(CreateSurveyModel.AnswerModel am2 : qm1.getAnswers()){
                        if(am1.getContent().trim().equalsIgnoreCase(am2.getContent().trim())){
                            aCount++;
                        }
                    }
                    if(aCount > 1){
                        throw new SurveyException("You Can Not Give Duplicate Answer \""+am1.getContent().trim()+"\"");
                    }
                    Answer answer = am1.getRealAnswerObject();
                    if (answer.getContent().equalsIgnoreCase(""))
                        throw new SurveyException("Answer Title Can Not Empty");
                }
            }
            //Sırasıyla Survey Question ve Answer Kaydedilir.
            surveyDAO.create(survey);
            for (CreateSurveyModel.QuestionModel qm : model.Questions) {
                Question question = qm.getRealQuestionObject();
                question.setSurvey(survey);
                questionDAO.create(question);
                for (CreateSurveyModel.AnswerModel am : qm.getAnswers()) {
                    Answer answer = am.getRealAnswerObject();
                    answer.setQuestion(question);
                    answerDAO.create(answer);
                }
            }

        returnMessage ="{\"Done\":\"";
        returnMessage += "Survey Created With "+survey.getId()+" Id.";
        if(survey.getReady()){
            returnMessage += " You Have Set Ready 'True', You CAN NOT Add New Questions/Answers To Survey. But You Can Delete.";
        }else{
            returnMessage += " You Have Set Ready 'False', Until Set 'True' You Can Edit Survey.";
        }
        returnMessage +="\"}";
        return returnMessage ;
    } // olusturModel : kendi + soru Id'leri

    public SurveyLookVM requestSurveyParticipation(long surveyId) throws SurveyException { // kullanıcı anket katılım isteği
            SurveyLookVM sl = new SurveyLookVM();
            sl.setSurvey(getSingleSurvey(surveyId));
            List<QuestionSingleVM> qss = new ArrayList<>();
            List<Question> questions = questionDAO.getQuestionsBySurvey(surveyId);
            for(Question question : questions){
                QuestionSingleVM qs = new QuestionSingleVM();
                qs.setId(question.getId());
                qs.setTitle(question.getTitle());
                List<AnswerSingleVM> ass = new ArrayList<>();
                List<Answer> answers = answerDAO.getAnswersByQuestion(question.getId());
                for(Answer answer : answers){
                    AnswerSingleVM as = new AnswerSingleVM();
                    as.setId(answer.getId());
                    as.setContent(answer.getContent());
                    ass.add(as);
                }
                qs.setAnswers(ass);
                qss.add(qs);
            }
            sl.setQuestions(qss);
            return sl;
    }//KATILIMCI GETİ :anket katılma isteği : Survey +  Soru_lar içinde cevaplar (anket katılımı sayfası)

    public List<SurveySingleVM> getAllSurvey() throws SurveyException {
        List<SurveySingleVM> sss = new ArrayList<>();
        List<Survey> surveys = surveyDAO.list();
        for(Survey survey : surveys){
            SurveySingleVM ss = new SurveySingleVM();
            ss.setId(survey.getId());
            ss.setTitle(survey.getTitle());
            ss.setExplanation(survey.getExplanation());
            ss.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(survey.getStartDate()));
            ss.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(survey.getEndDate()));
            ss.setReady(survey.getReady());
            sss.add(ss);
        }
        return sss;
    } //tümAnketleriGetir bir üstteki fonk her anket için

    @Transactional()
    public String setSurveyReady(long surveyId) throws SurveyException {
        boolean status = surveyDAO.setReady(surveyId);
        if (status)
            return "{\"Done\":\"Survey Ready.\"}";
        else
            throw new SurveyException("Survey Not Found.");
    } // kullanimaBaslat

    public List<SurveySingleVM> getNotReadySurveys() throws SurveyException {
        List<SurveySingleVM> sss = new ArrayList<>();
        List<Survey> surveys = surveyDAO.getNotReadySurveys();
        for(Survey survey : surveys){
            SurveySingleVM ss = new SurveySingleVM();
            ss.setId(survey.getId());
            ss.setTitle(survey.getTitle());
            ss.setExplanation(survey.getExplanation());
            ss.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(survey.getStartDate()));
            ss.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(survey.getEndDate()));
            ss.setReady(survey.getReady());
            sss.add(ss);
        }
        return sss;
    } // kullanimaBaslatilmayanAnketleri Getir

    public List<SurveySingleVM> getReadySurveys(){ // hazır anketlerin listesini çek
        List<SurveySingleVM> sss = new ArrayList<>(); // geri gönderilecek Anket View Model listesi
        List<Survey> surveys = surveyDAO.getReadySurveys(); // Anketleri çek
        for(Survey survey : surveys){ // Anket modeli Anket View Modele çevirilip, geri gönderilecek model listesine eklenir.
            SurveySingleVM ss = new SurveySingleVM();
            ss.setId(survey.getId());
            ss.setTitle(survey.getTitle());
            ss.setExplanation(survey.getExplanation());
            ss.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(survey.getStartDate()));
            ss.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(survey.getEndDate()));
            ss.setReady(survey.getReady());
            sss.add(ss);
        }
        return sss;
    }

    @Transactional()
    public String addQuestionsAndAnswersToSurvey(long surveyId,AddQuestionModel model) throws SurveyException {

        Survey survey = surveyDAO.get(surveyId);
        if (survey == null)
            throw new SurveyException("Survey Not Found");

        if (survey.getReady())
            throw new SurveyException("Survey Is Ready, You Can Not Edit. Your Questions Are Not Added.");
        // kendi içinde aynı soru var mı
        for (AddQuestionModel.QuestionModel qm1 : model.getQuestions()) {
            int count = 0;
            for (AddQuestionModel.QuestionModel qm2 : model.getQuestions()) {
                if (qm1.getTitle().trim().equalsIgnoreCase(qm2.getTitle().trim())) {
                    count++;
                }
            }
            if (count > 1) {
                throw new SurveyException("The Same Questions Can Not Be Questioned.");
            }
        }
        // kendi içinde aynı cevap var mı
        for (AddQuestionModel.QuestionModel qm : model.getQuestions()) {
            for (AddQuestionModel.AnswerModel am1 : qm.getAnswers()) {
                int count = 0;
                for (AddQuestionModel.AnswerModel am2 : qm.getAnswers()) {
                    if (am1.getContent().trim().equalsIgnoreCase(am2.getContent().trim())) {
                        count++;
                    }
                }
                if (count > 1) {
                    throw new SurveyException("The Same Answers Can Not Be Questioned.");
                }
            }
        }
        List<Question> questions = questionDAO.getQuestionsBySurvey(surveyId);//db'de aynı soru var mı
        for (Question question : questions) {
            for (AddQuestionModel.QuestionModel qm : model.getQuestions()) {
                if (question.getTitle().equalsIgnoreCase(qm.getTitle())) {
                    throw new SurveyException("This Question \"" + question.getTitle() + "\" Have Already.");
                }
            }
        }
        // sırayla ekle
        for (AddQuestionModel.QuestionModel qm : model.getQuestions()) {
            Question question = qm.getRealQuestionObject();
            question.setSurvey(survey);
            questionDAO.create(question);
            for (AddQuestionModel.AnswerModel am : qm.getAnswers()) {
                Answer answer = am.getRealAnswerObject();
                answer.setQuestion(question);
                answerDAO.create(answer);
            }
        }

        return "{\"Done\":\"Questions And Answers Successfully Added To Survey.\"}";
    }

    @Transactional()
    public String deleteSurvey(long surveyId) throws SurveyException {

        Survey survey = surveyDAO.get(surveyId);
        //varmı
        if (survey == null) {
            throw new SurveyException("Survey Not Found.");
        }
        //respondentleri al//respondenAnswerleri sil//respondenti sil
        List<Respondent> respondents = respondentDAO.getRespondentsBySurvey(surveyId);
        for (Respondent r : respondents) {
            for (RespondentAnswer ra : respondentAnswerDAO.getRespondentAnswersOfRespondent(r.getId())) {
                respondentAnswerDAO.remove(ra);
            }
            respondentDAO.remove(r);
        }
        List<Question> questions = questionDAO.getQuestionsBySurvey(surveyId);//soru al///cevap al//cevap sil//soru sil
        for (Question q : questions) {
            for (Answer a : answerDAO.getAnswersByQuestion(q.getId())) {
                answerDAO.remove(a);
            }
            questionDAO.remove(q);
        }
        surveyDAO.remove(survey);//survey sil


        return "{\"Done\":\"Survey Deleted Successfuly.\"}";
    }
}
