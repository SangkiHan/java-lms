package nextstep.session.domain;

import nextstep.session.RecruitmentStatus;

public interface SessionRepository {
    int save(Session session);

    Session findById(Long id);

    int updateSessionStatus(Long sessionId, SessionStatus sessionStatus);

    int updateRecruitmentStatus(Long sessionId, RecruitmentStatus recruitmentStatus);
}
