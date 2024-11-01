package nextstep.session.service;

import nextstep.session.domain.PaymentType;
import nextstep.session.domain.PickSession;
import nextstep.session.domain.Session;
import nextstep.session.domain.SubscribeStatus;
import nextstep.session.domain.image.Image;
import nextstep.support.TestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class SessionServiceTest extends TestSupport {

    @Autowired
    private SessionService sessionService;

    private final LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
    private final LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

    @BeforeEach
    void setUp() {
        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);

        Session session = Session.createPaid(1L, "테스트강의", List.of(image), PickSession.NON_PICK, 1, 800000, startDate, endDate);

        sessionService.save(session);
    }

    @DisplayName("강의를 저장한다 후 조회한다.")
    @Test
    void saveTest() {
        Session session = sessionService.findById(1L);
        assertThat(session)
                .extracting("id", "title", "paymentType", "subscribeStatus", "subscribeMax", "price", "dateRange.startDate", "dateRange.endDate")
                .contains(1L, "테스트강의", PaymentType.PAID, SubscribeStatus.READY, 1, 800000, startDate, endDate);

        assertThat(session.getImage())
                .extracting("sessionId", "name", "size.width.width", "size.height.height", "capacity.capacity")
                .containsExactly(tuple(1L, "테스트이미지.jpg", 300, 200, 1));
    }

    @DisplayName("강의의 상태를 변경한다.")
    @Test
    void changeSubscribeStatusTest() {
        sessionService.changeSubscribeStatus(1L, SubscribeStatus.WAIT);

        Session session = sessionService.findById(1L);

        assertThat(session)
                .extracting("id", "title", "paymentType", "subscribeStatus", "subscribeMax", "price", "dateRange.startDate", "dateRange.endDate")
                .contains(1L, "테스트강의", PaymentType.PAID, SubscribeStatus.WAIT, 1, 800000, startDate, endDate);

        assertThat(session.getImage())
                .extracting("sessionId", "name", "size.width.width", "size.height.height", "capacity.capacity")
                .containsExactly(tuple(1L, "테스트이미지.jpg", 300, 200, 1));
    }

}
