package nextstep.session.domain;

import nextstep.session.domain.image.Image;
import nextstep.users.domain.NsUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SessionPickTest {

    public static final NsUser JAVAJIGI = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");

    @DisplayName("강의 승인인원을 생성한다.")
    @Test
    void addUserTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        //when
        SessionPick sessionPick = new SessionPick(session, JAVAJIGI);
        //then
        assertThat(sessionPick)
                .extracting("nsUser.id", "nsUser.userId", "nsUser.password", "nsUser.name", "nsUser.email")
                .contains(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    @DisplayName("강의 승인인원이 맞는지 체크한다.")
    @Test
    void subscribeUsersSizeTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        //when
        SessionPick sessionPick = new SessionPick(session, JAVAJIGI);

        //then
        assertThat(sessionPick.checkSessionPick(JAVAJIGI)).isTrue();
    }
}
