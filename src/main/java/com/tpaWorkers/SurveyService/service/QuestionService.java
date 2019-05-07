package com.tpaWorkers.SurveyService.service;

import com.tpaWorkers.SurveyService.dao.QuestionDAO;
import com.tpaWorkers.SurveyService.dao.RespondentAnswerDAO;
import com.tpaWorkers.SurveyService.dao.RespondentDAO;
import com.tpaWorkers.SurveyService.dao.SurveyDAO;
import com.tpaWorkers.SurveyService.dao.AnswerDAO;
import com.tpaWorkers.SurveyService.dao.model.Answer;
import com.tpaWorkers.SurveyService.dao.model.Question;
import com.tpaWorkers.SurveyService.dao.model.RespondentAnswer;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import com.tpaWorkers.SurveyService.exception.SurveyException;
import com.tpaWorkers.SurveyService.service.view_model.AnswerSingleVM;
import com.tpaWorkers.SurveyService.service.view_model.QuestionSingleVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class) // Propagation : transaction işlemleri aksamadan devam etsin. // rollbackFor : şuanki transaction işlemin hangi hata alınırsa geri alınacağı. Ne hata olursa geri al uygulama.
public class QuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private SurveyDAO surveyDAO;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private AnswerDAO answerDAO;
    @Autowired
    private RespondentAnswerDAO respondentAnswerDAO;

    public QuestionSingleVM getSingleQuestion(long questionId) throws SurveyException {

        Question question = questionDAO.get(questionId);
        List<Answer> answers = answerDAO.getAnswersByQuestion(questionId);
        if (question == null)
            throw new SurveyException("Question Not Found.");

        QuestionSingleVM qsvm = new QuestionSingleVM();
        qsvm.setId(question.getId());
        qsvm.setTitle(question.getTitle());
        List<AnswerSingleVM> asvms = new ArrayList<>();
        for(Answer answer : answers){
            AnswerSingleVM asvm = new AnswerSingleVM();
            asvm.setId(answer.getId());
            asvm.setContent(answer.getContent());
            asvms.add(asvm);
        }
        qsvm.setAnswers(asvms);

        return qsvm;
    } // Kendisi ve Cevap_lar

    public List<QuestionSingleVM> getAllQuestions() throws SurveyException {

        List<Question> questions = questionDAO.list();
        List<QuestionSingleVM> qsvms = new ArrayList<>();

        for(Question question : questions){
            qsvms.add(getSingleQuestion(question.getId()));
        }
        return qsvms;
    } // tümSoruları getir bir üstteki fonku döndür

    @Transactional()
    public String deleteQuestion(long questionId) throws SurveyException {
        Question question = questionDAO.get(questionId);
        if (question == null)//question var mı
            throw new SurveyException("Question Not Found.");
        boolean haveSurvey = question.getSurvey() != null;//surveyi var mı
        if (haveSurvey) {//varsa question sayısı 1 ve küçükse patla
            Survey survey = surveyDAO.get(question.getSurvey().getId());
            List<Question> questions = questionDAO.getQuestionsBySurvey(survey.getId());
            if (questions.size() <= 1)
                throw new SurveyException("Survey Can Not Have Less Than 1 Question. Please Put Other Questions And After Delete This Question.");
            if (survey.getReady())
                throw new SurveyException("Survey Is Ready, You Can Not Edit. Your Questions Are Not Deleted.");
        }
        List<RespondentAnswer> ras = respondentAnswerDAO.getRespondentAnswersOfQuestion(questionId);//respondentAnswer'lerini çek sil
        for (RespondentAnswer ra : ras) {
            respondentAnswerDAO.remove(ra);
        }
        List<Answer> answers = answerDAO.getAnswersByQuestion(questionId);//cevapları çek sil
        for (Answer a : answers) {
            answerDAO.remove(a);
        }
        questionDAO.remove(question);//soruyu sil

        return "{\"Done\":\"Question Deleted Successfully.\"}";
    }
}
