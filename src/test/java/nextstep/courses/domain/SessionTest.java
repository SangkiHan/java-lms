package nextstep.courses.domain;

import nextstep.courses.domain.code.SessionStatus;
import nextstep.courses.domain.strategy.PaidEnrollmentStrategy;
import nextstep.courses.exception.CanNotApplySessionStatusException;
import nextstep.courses.exception.IncorrectAmountException;
import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionTest {

    @Test
    @DisplayName("결제 금액과 강의 금액이 다르면 예외 처리 한다")
    void apply() {
        Period period = new Period(LocalDate.of(2023, 11, 24),
                LocalDate.of(2023, 11, 24));
        Thumbnail thumbnail = new Thumbnail("테스트",
                "/home/test.png",
                new FileSize(1024L),
                new ImageSize(300L,
                        200L));
        PaidEnrollmentStrategy paidEnrollmentStrategy = new PaidEnrollmentStrategy(1);
        Amount amount = new Amount(20000L);
        Session session = new Session(
                period,
                thumbnail,
                paidEnrollmentStrategy,
                amount,
                SessionStatus.RECRUITING);

        assertThrows(IncorrectAmountException.class, () -> session.enrol(new Payment("테스트", 0L, 0L, 15000L), new NsUser()),
                "결제 금액과 강의 금액이 다릅니다.");
    }

    @Test
    @DisplayName("모집중이 아니면 예외 처리 된다.")
    void apply2() {
        Period period = new Period(LocalDate.of(2023, 11, 24),
                LocalDate.of(2023, 11, 24));
        Thumbnail thumbnail = new Thumbnail("테스트",
                "/home/test.png",
                new FileSize(1024L),
                new ImageSize(300L,
                        200L));
        PaidEnrollmentStrategy paidEnrollmentStrategy = new PaidEnrollmentStrategy(1);
        Amount amount = new Amount(20000L);


        Assertions.assertAll(
                () -> {
                    Session session = new Session(
                            period,
                            thumbnail,
                            paidEnrollmentStrategy,
                            amount,
                            SessionStatus.PREPARING);

                    assertThrows(CanNotApplySessionStatusException.class,
                            () -> session.enrol(new Payment("테스트", 0L, 0L, 20000L), new NsUser()),
                            "수강 신청이 가능한 상태가 아닙니다.");
                },
                () -> {
                    Session session = new Session(
                            period,
                            thumbnail,
                            paidEnrollmentStrategy,
                            amount,
                            SessionStatus.END);

                    assertThrows(CanNotApplySessionStatusException.class,
                            () -> session.enrol(new Payment("테스트", 0L, 0L, 20000L), new NsUser()),
                            "수강 신청이 가능한 상태가 아닙니다.");
                }
        );


    }

    @Test
    @DisplayName("유료 강의 신청을 한다")
    void apply3() {
        Period period = new Period(LocalDate.of(2023, 11, 24),
                LocalDate.of(2023, 11, 24));
        Thumbnail thumbnail = new Thumbnail("테스트",
                "/home/test.png",
                new FileSize(1024L),
                new ImageSize(300L,
                        200L));
        PaidEnrollmentStrategy paidEnrollmentStrategy = new PaidEnrollmentStrategy(1);
        Amount amount = new Amount(20000L);
        Session session = new Session(
                period,
                thumbnail,
                paidEnrollmentStrategy,
                amount,
                SessionStatus.RECRUITING);

        assertDoesNotThrow(() -> session.enrol(new Payment("테스트", 0L, 0L, 20000L), new NsUser()));
    }
}
