package nextstep.qna.service;

import nextstep.qna.CannotDeleteException;
import nextstep.qna.NotFoundException;
import nextstep.qna.domain.AnswerRepository;
import nextstep.qna.domain.DeleteHistory;
import nextstep.qna.domain.Question;
import nextstep.qna.domain.QuestionRepository;
import nextstep.users.domain.NsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service("qnaService")
public class QnAService {
    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "deleteHistoryService")
    private DeleteHistoryService deleteHistoryService;

    @Transactional
    public void deleteQuestion(NsUser loginUser, long questionId, LocalDateTime date) {
        Question question = getQuestion(questionId);
        question = question.delete(loginUser);
        questionRepository.update(questionId, question);

        List<DeleteHistory> deleteHistories = question.toDeleteHistories(loginUser, date);
        deleteHistoryService.saveAll(deleteHistories);
    }

    private Question getQuestion(long questionId) throws CannotDeleteException {
        return questionRepository.findById(questionId).orElseThrow(NotFoundException::new);
    }
}
