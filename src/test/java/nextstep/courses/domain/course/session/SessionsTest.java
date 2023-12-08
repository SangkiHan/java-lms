package nextstep.courses.domain.course.session;

import nextstep.courses.domain.course.image.Image;
import nextstep.payments.domain.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SessionsTest {
    private Sessions sessions;
    private Image image;
    private Payment payment;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private Duration duration;
    private Session session;

    @BeforeEach
    void setUp() {
        sessions = new Sessions();
        image = new Image(1000, "jpeg", Image.WIDTH_MIN, Image.HEIGHT_MIN);
        payment = new Payment("1", 1L, 3L, 1000L);
        localDate = LocalDate.of(2023, 12, 5);
        localDateTime = LocalDateTime.of(2023, 12, 5, 12, 0);
        duration = new Duration(localDate, localDate);
        session = new Session(1L, image, duration, Session.Type.FREE, 1000L,
                10, new Applicants(), Session.Status.RECRUIT, localDateTime, localDateTime);
        sessions.add(session);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Sessions 은 빈 값이 주어지면 예외를 던진다.")
    void newObject_null_throwsException(List<Session> sessions) {
        assertThatThrownBy(
                () -> new Sessions(sessions)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("add 는 이미 강의가 추가되었으면 예외를 던진다.")
    void add_alreadyExistedSession_throwsException() {
        assertThatThrownBy(
                () -> sessions.add(session)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
