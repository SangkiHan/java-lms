package nextstep.session.domain;

import nextstep.DateDomain;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class SessionPick {
    private Long id;
    private Long sessionId;
    private NsUser nsUser;
    private DateDomain dateDomain;

    public SessionPick(Session session, NsUser nsUser) {
        this.sessionId = session.getId();
        this.nsUser = nsUser;
        this.dateDomain = new DateDomain();
    }

    public SessionPick(Long id, Long sessionId, NsUser nsUser, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.nsUser = nsUser;
        this.dateDomain = new DateDomain(createAt, updateAt);
    }

    public Long getId() {
        return id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public NsUser getNsUser() {
        return nsUser;
    }

    public DateDomain getDateDomain() {
        return dateDomain;
    }

    public boolean checkSessionPick(NsUser nsUser) {
        return nsUser.equals(this.nsUser);
    }
}