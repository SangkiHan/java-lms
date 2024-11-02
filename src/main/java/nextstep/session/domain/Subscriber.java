package nextstep.session.domain;

import nextstep.DateDomain;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class Subscriber {

    private Long id;
    private Long sessionId;
    private NsUser nsUser;
    private DateDomain dateDomain;

    public Subscriber(Session session, NsUser nsUser) {
        this.sessionId = session.getId();
        this.nsUser = nsUser;
        this.dateDomain = new DateDomain();
    }

    public Subscriber(Long id, Long sessionId, NsUser nsUser, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.nsUser = nsUser;
        this.dateDomain = new DateDomain(createdAt, updatedAt);
    }

    public Subscriber(Long id, NsUser nsUser, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nsUser = nsUser;
        this.dateDomain = new DateDomain(createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public NsUser getNsUser() {
        return nsUser;
    }

    public DateDomain getDateDomain() {
        return dateDomain;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public boolean checkNsUser(NsUser nsUser) {
        return this.nsUser.equals(nsUser);
    }
}
