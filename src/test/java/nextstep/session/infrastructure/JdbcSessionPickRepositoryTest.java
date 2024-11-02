package nextstep.session.infrastructure;

import nextstep.session.domain.SessionPick;
import nextstep.session.domain.SessionPickRepository;
import nextstep.session.domain.*;
import nextstep.session.domain.image.Image;
import nextstep.support.TestSupport;
import nextstep.users.domain.NsUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcSessionPickRepositoryTest extends TestSupport {

    public static final NsUser JAVAJIGI = new NsUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");

    @Autowired
    private SessionPickRepository sessionPickRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @DisplayName("강의 승인자를 저장한 후 조회한다.")
    @Test
    void saveTest() {
        //given
        LocalDateTime startDate = LocalDateTime.parse("2023-04-05T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-05-05T00:00:00");

        Image image = new Image(1L, "테스트이미지.jpg", 300, 200, 1);
        Session session = Session.createPaid(1L, "테스트강의", List.of(image), 1, 800000, startDate, endDate);

        sessionRepository.save(session);

        Session findSession = sessionRepository.findById(1L);

        SessionPick sessionPick = new SessionPick(findSession, JAVAJIGI);

        //when, then
        sessionPickRepository.save(sessionPick);

        assertThat(sessionPickRepository.findById(1L))
                .extracting("sessionId", "nsUser.id")
                .containsExactly(findSession.getId(), JAVAJIGI.getId());
    }
}
