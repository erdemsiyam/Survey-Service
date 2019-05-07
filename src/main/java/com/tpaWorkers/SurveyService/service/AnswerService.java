package com.tpaWorkers.SurveyService.service;

import com.tpaWorkers.SurveyService.dao.*;
import com.tpaWorkers.SurveyService.dao.model.Answer;
import com.tpaWorkers.SurveyService.dao.model.Question;
import com.tpaWorkers.SurveyService.dao.model.RespondentAnswer;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import com.tpaWorkers.SurveyService.service.post_model.AddAnswerModel;
import com.tpaWorkers.SurveyService.exception.SurveyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class) // Propagation : transaction işlemleri aksamadan devam etsin. // rollbackFor : şuanki transaction işlemin hangi hata alınırsa geri alınacağı. Ne hata olursa geri al uygulama.
public class AnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(AnswerService.class);

    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private AnswerDAO answerDAO;
    @Autowired
    private RespondentAnswerDAO respondentAnswerDAO;


    @Transactional()
    public String deleteAnswer(long answerId) throws SurveyException {

        Answer answer = answerDAO.get(answerId);
        if(answer == null)
            throw new SurveyException("Answer Not Found.");
        boolean haveQuestion = answer.getQuestion() != null;//sorusu var mı//varsa sorunun cevaplarının sayısı 2 ve küçükse patla
        if(haveQuestion){
            List<Answer> as = answerDAO.getAnswersByQuestion(answer.getQuestion().getId());
            if(as.size() <= 2)
                throw new SurveyException("Question Can Not Have Less Than Or Equals 2 Answer. Please Put Other Answers And After Delete This Answer.");
            if(answer.getQuestion().getSurvey().getReady()){
                throw new SurveyException("Survey Is Ready, You Can Not Edit. Your Answers Are Not Deleted.");
            }
        }
        List<RespondentAnswer> ras = respondentAnswerDAO.getRespondentAnswersOfAnswer(answerId);//responseAnswer varsa sil
        for(RespondentAnswer ra : ras){
            respondentAnswerDAO.remove(ra);
        }
        answerDAO.remove(answer);//cevabı sil
        return "{\"Done\":\"Answer Deleted Successfully.\"}";
    }

    @Transactional()
    public String addAnswersToQuestion(long questionId, AddAnswerModel model) throws SurveyException {

        Question question = questionDAO.get(questionId);
        if (question == null) // soru yoksa patla
            throw new SurveyException("Question Not Found.");
        Survey survey = question.getSurvey();
        if (survey.getReady())
            throw new SurveyException("Survey Is Ready, You Can Not Edit. Your Answers Are Not Added.");
        for (String s1 : model.Answers) {//gönderilen cevaplar içinde aynı cevap varsa patla.
            int sameCount = 0;
            for (String s2 : model.Answers) {
                if (s1.trim().equalsIgnoreCase(s2.trim()))
                    sameCount++;
            }
            if (sameCount > 1)
                throw new SurveyException("You Can Not Put Duplicate Answer \"" + s1.trim() + "\"");
        }
        List<Answer> oldAnswers = answerDAO.getAnswersByQuestion(questionId); // aynı cevap zaten o soruda varsa ekleme
        for (Answer a : oldAnswers) {
            for (String s : model.Answers) {
                if (a.getContent().equalsIgnoreCase(s.trim()))
                    throw new SurveyException("This Answer \"" + s.trim() + "\" Is Already In Use ");
            }
        }
        //ekle
        for (String s : model.Answers) {
            Answer answer = new Answer();
            answer.setContent(s.trim());
            answer.setQuestion(question);
            answerDAO.create(answer);
        }
        return "{\"Done\":\"Answers Added Successfully.\"}";
    }

}

