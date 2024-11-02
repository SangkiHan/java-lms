package nextstep.session.service;

import nextstep.session.RecruitmentStatus;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class SubscriberServiceTest extends TestSupport {

    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionPickService sessionPickService;

    @DisplayName("강의 신청자를 저장한다.")
    @Test
    void subscribeTest() {
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        sessionService.save(session);

        sessionService.changeSubscribeStatus(1L, SessionStatus.PROCESS);

        subscriberService.subscribe(1L, 1L);

        Session findSession = sessionService.findById(1L);
        assertThat(findSession.getSubscribers().getSubscribeUsers())
                .extracting("nsUser.id", "nsUser.userId", "nsUser.password", "nsUser.name", "nsUser.email")
                .contains(tuple(1L, "javajigi", "test", "자바지기", "javajigi@slipp.net"));
    }

    @DisplayName("강의 신청자를 저장할시 승인된 인원이 아니라면 예외가 발생한다..")
    @Test
    void subscribeNotPickThrowExceptionTest() {
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        sessionService.save(session);

        sessionService.changeSubscribeStatus(1L, SessionStatus.PROCESS);

        assertThatThrownBy(() -> subscriberService.subscribe(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 인원은 강의승인을 받지 않았습니다.");
    }

    @DisplayName("강의를 취소할 시 선발된 인원이라면 예외가 발생한다.")
    @Test
    void cancelSubscribePickUserThrowExceptionTest() {
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        sessionService.save(session);
        sessionService.changeRecruitmentStatus(1L, RecruitmentStatus.RECRUIT);
        subscriberService.subscribe(1L, 1L);
        sessionPickService.enrollPickUser(1L, 1L);

        assertThatThrownBy(() -> subscriberService.cancelSubscribe(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선발된 인원이므로 강의 취소가 불가합니다.");
    }
}
