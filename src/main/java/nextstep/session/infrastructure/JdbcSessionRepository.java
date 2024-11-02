package nextstep.session.infrastructure;

import nextstep.session.RecruitmentStatus;
import nextstep.session.domain.*;
import nextstep.session.domain.image.Image;
import nextstep.users.domain.NsUser;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository("sessionRepository")
public class JdbcSessionRepository implements SessionRepository {
    private final JdbcOperations jdbcTemplate;

    public JdbcSessionRepository(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public int save(Session session) {
        String sql = "insert into session (title, payment_type, session_status, recruitment_status, subscribe_max, price, start_date, end_date, created_at, updated_at) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                session.getTitle(),
                session.getPaymentType().name(),
                session.getSessionStatus().name(),
                session.getRecruitmentStatus().name(),
                session.getSubscribeMax(),
                session.getPrice(),
                session.getDateRange().getStartDate(),
                session.getDateRange().getEndDate(),
                session.getDateDomain().getCreatedAt(),
                session.getDateDomain().getUpdatedAt()
        );
    }

    @Override
    public Session findById(Long id) {
        String sql = "select id, title, payment_type, session_status, recruitment_status, subscribe_max, price, start_date, end_date, created_at, updated_at from session where id = ?";
        RowMapper<Session> rowMapper = (rs, rowNum) -> new Session(
                rs.getLong(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getInt(6),
                rs.getInt(7),
                toLocalDateTime(rs.getTimestamp(8)),
                toLocalDateTime(rs.getTimestamp(9)),
                findImageBySessionId(rs.getLong(1)),
                findSessionPickBySessionId(rs.getLong(1)),
                findSubscriberBySessionId(rs.getLong(1)),
                toLocalDateTime(rs.getTimestamp(10)),
                toLocalDateTime(rs.getTimestamp(11)));

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public int updateSessionStatus(Long sessionId, SessionStatus sessionStatus) {
        String sql = "update session set session_status = ?, updated_at = ? where id = ?";
        return jdbcTemplate.update(sql,
                sessionStatus.name(),
                LocalDateTime.now(),
                sessionId
        );
    }

    @Override
    public int updateRecruitmentStatus(Long sessionId, RecruitmentStatus recruitmentStatus) {
        String sql = "update session set recruitment_status = ?, updated_at = ? where id = ?";
        return jdbcTemplate.update(sql,
                recruitmentStatus.name(),
                LocalDateTime.now(),
                sessionId
        );
    }

    private List<Image> findImageBySessionId(Long sessionId) {
        String sql = "select id, session_id, name, width, height, capacity, created_at, updated_at from session_image where session_id = ?";
        RowMapper<Image> rowMapper = (rs, rowNum) -> new Image(
                rs.getLong(1),
                rs.getLong(2),
                rs.getString(3),
                rs.getInt(4),
                rs.getInt(5),
                rs.getInt(6),
                toLocalDateTime(rs.getTimestamp(7)),
                toLocalDateTime(rs.getTimestamp(8)));

        return jdbcTemplate.query(sql, rowMapper, sessionId);
    }

    private List<Subscriber> findSubscriberBySessionId(Long sessionId) {
        String sql = "select id, session_id, ns_user_id, created_at, updated_at from subscriber where session_id = ?";
        RowMapper<Subscriber> rowMapper = (rs, rowNum) -> new Subscriber(
                rs.getLong(1),
                findUserById(rs.getLong(3)),
                toLocalDateTime(rs.getTimestamp(4)),
                toLocalDateTime(rs.getTimestamp(5)));

        return jdbcTemplate.query(sql, rowMapper, sessionId);
    }

    private List<SessionPick> findSessionPickBySessionId(Long sessionId) {
        String sql = "select id, session_id, ns_user_id, approve_status, created_at, updated_at from session_pick where session_id = ?";
        RowMapper<SessionPick> rowMapper = (rs, rowNum) -> new SessionPick(
                rs.getLong(1),
                rs.getLong(2),
                findUserById(rs.getLong(3)),
                rs.getString(4),
                toLocalDateTime(rs.getTimestamp(5)),
                toLocalDateTime(rs.getTimestamp(6)));

        return jdbcTemplate.query(sql, rowMapper, sessionId);
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
