package nextstep.session.service;

import nextstep.session.domain.ApproveStatus;
import nextstep.session.domain.PickSession;
import nextstep.session.domain.Session;
import nextstep.session.domain.SessionStatus;
import nextstep.session.domain.image.Image;
import nextstep.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class SessionPickServiceTest extends TestSupport {

    @Autowired
    private SessionPickService sessionPickService;
    @Autowired
    private SessionService sessionService;

    @DisplayName("강의에 선발된 인원을 저장한다.")
    @Test
    void subscribeTest() {
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        sessionService.save(session);

        sessionService.changeSubscribeStatus(1L, SessionStatus.PROCESS);
        sessionPickService.enrollPickUser(1L, 1L);

        Session findSession = sessionService.findById(1L);
        assertThat(findSession.getSessionPicks().getSessionPicks())
                .extracting("nsUser.id", "nsUser.userId", "nsUser.password", "nsUser.name", "nsUser.email")
                .contains(tuple(1L, "javajigi", "test", "자바지기", "javajigi@slipp.net"));
    }

    @DisplayName("선발된 인원의 강의를 승인한다.")
    @Test
    void approveSessionPickTest() {
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        sessionService.save(session);

        sessionService.changeSubscribeStatus(1L, SessionStatus.PROCESS);
        sessionPickService.enrollPickUser(1L, 1L);

        sessionPickService.approvePickUser(1L, ApproveStatus.APPROVED);

        assertThat(sessionPickService.findById(1L))
                .extracting("approveStatus")
                .isEqualTo(ApproveStatus.APPROVED);
    }
}
