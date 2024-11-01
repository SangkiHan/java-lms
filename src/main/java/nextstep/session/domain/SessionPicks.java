package nextstep.session.domain;

import nextstep.users.domain.NsUser;

import java.util.ArrayList;
import java.util.List;

public class SessionPicks {

    private static final String NOT_EXIST_PICK_MESSAGE = "해당 인원은 강의승인을 받지 않았습니다.";

    private List<SessionPick> sessionPicks = new ArrayList<>();

    public SessionPicks() {
    }

    public SessionPicks(List<SessionPick> sessionPicks) {
        this.sessionPicks = sessionPicks;
    }

    public SessionPick addUser(Session session, NsUser nsUser) {
        SessionPick sessionPick = new SessionPick(session, nsUser);
        this.sessionPicks.add(sessionPick);
        return sessionPick;
    }

    public void confirmPickUser(NsUser nsUser) {
        boolean hasValidPick = this.sessionPicks.stream()
                .anyMatch(pick -> pick.checkSessionPick(nsUser));

        if (!hasValidPick) {
            throw new IllegalArgumentException(NOT_EXIST_PICK_MESSAGE); // 예외 발생
        }
    }

    public List<SessionPick> getSessionPicks() {
        return sessionPicks;
    }
}
