package nextstep.session.domain;

import nextstep.users.domain.NsUser;

import java.util.ArrayList;
import java.util.List;

public class SessionPicks {

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

    public boolean checkPickUser(NsUser nsUser) {
        return this.sessionPicks.stream()
                .anyMatch(pick -> pick.checkSessionPick(nsUser));
    }

    public List<SessionPick> getSessionPicks() {
        return sessionPicks;
    }
}
