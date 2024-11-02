package nextstep.session.domain;

import nextstep.payments.domain.Payment;
import nextstep.session.domain.image.Image;
import nextstep.users.domain.NsUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SessionTest {

    @DisplayName("무료 강의를 생성한다.")
    @Test
    void createFreeSessionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");
        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);

        //when
        Session session = Session.createFree(1L, "테스트강의", List.of(image), startDate, endDate);

        //then
        assertThat(session)
                .extracting("title", "paymentType")
                .contains("테스트강의", PaymentType.FREE);
    }

    @DisplayName("유료 강의를 생성한다.")
    @Test
    void createPaidSessionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");
        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);

        //when
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 100, 800000, startDate, endDate);

        //then
        assertThat(session)
                .extracting("title", "paymentType", "subscribeMax", "price")
                .contains("테스트강의", PaymentType.PAID, 100, 800000);
    }

    @DisplayName("강의를 모집중으로 변경한다.")
    @Test
    void SessionWaitTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createFree(1L, "테스트강의", List.of(image), startDate, endDate);

        //when
        session.processSession();

        //then
        assertThat(session.getSessionStatus()).isEqualTo(SessionStatus.PROCESS);
    }

    @DisplayName("강의를 종료한다.")
    @Test
    void SessionClosedTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);

        Session session = Session.createFree(1L, "테스트강의", List.of(image), startDate, endDate);

        //when
        session.closedSession();

        //then
        assertThat(session.getSessionStatus()).isEqualTo(SessionStatus.CLOSED);
    }

    @DisplayName("무료 강의를 신청할 시 수강인원이 1 증가한다.")
    @Test
    void subscribeSessionAddSubscribeCountTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);

        Session session = Session.createFree(1L, "테스트강의", List.of(image), startDate, endDate);
        session.processSession();
        session.recruitSession();

        NsUser user = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");

        //when
        session.subscribe(user);

        //then
        assertThat(session.getSubscribeCount()).isEqualTo(1);
    }

    @DisplayName("강의를 신청할 시 종료된 강의이면 예외가 발생한다")
    @Test
    void subscribeSessionClosedThrowExceptionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 100, 800000, startDate, endDate);
        session.closedSession();
        Payment payment = new Payment(1L, 1L, 1L, 800000);
        NsUser user = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");

        //when, then
        assertThatThrownBy(() -> session.subscribe(user, payment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료된 강의입니다.");
    }

    @DisplayName("유료 강의를 신청할 시 수강생이 결제한 금액과 수강료가 일치하지 않으면 예외가 발생한다.")
    @Test
    void subscribeSessionNotCorrectPaymentThrowExceptionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 100, 800000, startDate, endDate);
        NsUser user = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        Payment payment = new Payment(1L, 1L, 1L, 700000);
        session.processSession();
        session.recruitSession();

        //when, then
        assertThatThrownBy(() -> session.subscribe(user, payment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 금액이 일치하지 않습니다.");
    }

    @DisplayName("유료 강의를 신청할 시 결제내역이 없으면 예외가 발생한다.")
    @Test
    void subscribeSessionWithoutPaymentThrowExceptionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 100, 800000, startDate, endDate);
        session.processSession();
        session.recruitSession();
        NsUser user = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");

        //when, then
        assertThatThrownBy(() -> session.subscribe(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유료강의는 결제내역이 필수입니다.");
    }

    @DisplayName("강의가 이미 만석이면 예외를 발생한다.")
    @Test
    void subscribeSessionAlreadyMaxThrowExceptionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        NsUser user1 = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        NsUser user2 = new NsUser(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        Payment payment = new Payment(1L, 1L, 1L, 800000);

        session.processSession();
        session.recruitSession();

        session.subscribe(user1, payment);

        //when, then
        assertThatThrownBy(() -> session.subscribe(user2, payment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의가 이미 만석입니다.");
    }

    @DisplayName("강의 승인자에 포함되어있지 않으면 예외가 발생한다.")
    @Test
    void subscribeSessionNotPickThrowExceptionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createFree(1L, "테스트강의", List.of(image), startDate, endDate);

        NsUser user1 = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        NsUser user2 = new NsUser(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

        session.processSession();
        session.enrollPick(user1);

        //when, then
        assertThatThrownBy(() -> session.subscribe(user2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 인원은 강의승인을 받지 않았습니다.");
    }

    @DisplayName("강의를 신청할 시 비모집중인 강의이면 예외가 발생한다")
    @Test
    void subscribeSessionNotRecruitThrowExceptionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 100, 800000, startDate, endDate);
        session.nonRecruitSession();
        Payment payment = new Payment(1L, 1L, 1L, 800000);
        NsUser user = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");

        //when, then
        assertThatThrownBy(() -> session.subscribe(user, payment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("모집이 종료된 강의입니다.");
    }

    @DisplayName("강의를 취소할 시 선발된 인원이면 예외가 발생한다.")
    @Test
    void cancelSessionPickUserThrowExceptionTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createFree(1L, "테스트강의", List.of(image), startDate, endDate);

        NsUser user1 = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");

        session.processSession();
        session.enrollPick(user1);

        //when, then
        assertThatThrownBy(() -> session.cancelSubscribe(user1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선발된 인원이므로 강의 취소가 불가합니다.");
    }

}
