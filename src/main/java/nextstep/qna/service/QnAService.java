package nextstep.qna.service;

import nextstep.qna.CannotDeleteException;
import nextstep.qna.NotFoundException;
import nextstep.qna.domain.*;
import nextstep.users.domain.NsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("qnaService")
public class QnAService {
    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "deleteHistoryService")
    private DeleteHistoryService deleteHistoryService;

    @Transactional
    public void deleteQuestion(NsUser loginUser, long questionId) throws CannotDeleteException {
        Question question = questionRepository.findById(questionId).orElseThrow(NotFoundException::new);

        Map<Question, List<Answer>> deleted = question.delete(loginUser);

        deleteHistoryService.saveAll(toDeleteHistory(deleted));
    }

    private static List<DeleteHistory> toDeleteHistory(Map<Question, List<Answer>> deleted) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleted.forEach((key, value) -> {
            deleteHistories.add(DeleteHistory.fromQuestion(key.getId(), key.getWriter()));

            deleteHistories.addAll(value.stream().map(it -> DeleteHistory.fromAnswer(it.getId(), it.getWriter())).collect(Collectors.toList()));
        });
        return deleteHistories;
    }
}
