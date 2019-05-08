package com.tpaWorkers.TestSurveyService.dao;

import com.tpaWorkers.SurveyService.dao.model.Answer;
import com.tpaWorkers.SurveyService.dao.model.Question;
import com.tpaWorkers.SurveyService.dao.model.Survey;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class TestAnswerDAO extends SuperDaoTest {

    @Test
    public void testGet(){
        Answer answer = createAnswer(createQuestion(createSurvey())); // örnek cevap oluşturuldu
        long id = answer.getId(); // örnek cevap id'si alındı

        Answer checkAnswer = answerDAO.get(id);
        Assert.assertNotNull(checkAnswer); // cevap çekilebildi mi
    }

    @Test
    public void testCreate(){
        Answer testAnswer = new Answer(); //
        testAnswer.setQuestion(createQuestion(createSurvey())); // test edilecek cevap için anket ve sorusu oluşturuldu
        testAnswer.setContent("test");

        Long id = answerDAO.create(testAnswer);
        Assert.assertTrue(id != null && id > 0);
    }

    @Test
    public void testRemove(){
        Answer testAnswer = createAnswer(createQuestion(createSurvey())); // örnek cevap oluşturuldu
        Assert.assertTrue(answerDAO.remove(testAnswer));
    }

    //To Survey Service, QuestionService, RespondentService
    @Test
    public void testGetAnswersByQuestion(){
        Survey survey = createSurvey();
        Question question = createQuestion(survey);
        Answer answer = createAnswer(question);

        long testQuestionId = question.getId();
        List<Answer> testAnswers = answerDAO.getAnswersByQuestion(testQuestionId);
        // hiç cevap gelebiliyor mu kontrol, en az 1 cevap yukarda verdik çünkü.
        Assert.assertTrue(testAnswers != null && testAnswers.size() >= 1);
    }

}
