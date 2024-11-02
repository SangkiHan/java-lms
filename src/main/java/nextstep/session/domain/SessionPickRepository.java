package nextstep.session.domain;

public interface SessionPickRepository {

    int save(SessionPick selectPick);

    SessionPick findById(Long id);

    int updateApproveStatus(Long sessionPickId, ApproveStatus approveStatus);

}
