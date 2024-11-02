package nextstep.session.infrastructure;

import nextstep.session.domain.ApproveStatus;
import nextstep.session.domain.SessionPick;
import nextstep.session.domain.SessionPickRepository;
import nextstep.users.domain.NsUser;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository("sessionPickRepository")
public class JdbcSessionPickRepository implements SessionPickRepository {

    private final JdbcOperations jdbcTemplate;

    public JdbcSessionPickRepository(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(SessionPick sessionPick) {
        String sql = "insert into session_pick (session_id, ns_user_id, approve_status, created_at, updated_at) values(?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                sessionPick.getSessionId(),
                sessionPick.getNsUser().getId(),
                sessionPick.getApproveStatus().name(),
                sessionPick.getDateDomain().getCreatedAt(),
                sessionPick.getDateDomain().getUpdatedAt()
        );
    }

    @Override
    public SessionPick findById(Long id) {
        String sql = "select id, session_id, ns_user_id, approve_status, created_at, updated_at from session_pick where id = ?";
        RowMapper<SessionPick> rowMapper = (rs, rowNum) -> new SessionPick(
                rs.getLong(1),
                rs.getLong(2),
                findUserById(rs.getLong(3)),
                rs.getString(4),
                toLocalDateTime(rs.getTimestamp(5)),
                toLocalDateTime(rs.getTimestamp(6)));

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public int updateApproveStatus(Long sessionPickId, ApproveStatus approveStatus) {
        String sql = "update session_pick set approve_status = ?, updated_at = ? where id = ?";
        return jdbcTemplate.update(sql,
                approveStatus.name(),
                LocalDateTime.now(),
                sessionPickId
        );
    }

    private NsUser findUserById(Long userId) {
        String sql = "select id, user_id, password, name, email, created_at, updated_at from ns_user where id = ?";
        RowMapper<NsUser> rowMapper = (rs, rowNum) -> new NsUser(
                rs.getLong(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                toLocalDateTime(rs.getTimestamp(6)),
                toLocalDateTime(rs.getTimestamp(7)));
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }

}
