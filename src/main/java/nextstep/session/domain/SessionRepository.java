package nextstep.session.domain;

public interface SessionRepository {
    int save(Session session);

    Session findById(Long id);

    int updateSessionStatus(Long sessionId, SessionStatus sessionStatus);
}
