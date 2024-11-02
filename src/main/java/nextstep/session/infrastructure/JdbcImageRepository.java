package nextstep.session.infrastructure;

import nextstep.session.domain.ImageRepository;
import nextstep.session.domain.image.Image;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository("imageRepository")
public class JdbcImageRepository implements ImageRepository {

    private final JdbcOperations jdbcTemplate;

    public JdbcImageRepository(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Image image) {
        String sql = "insert into session_image (session_id, name, width, height, capacity, created_at, updated_at) values(?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                image.getSessionId(),
                image.getName(),
                image.getSize().getWidth().getWidth(),
                image.getSize().getHeight().getHeight(),
                image.getCapacity().getCapacity(),
                image.getDateDomain().getCreatedAt(),
                image.getDateDomain().getUpdatedAt()
        );
    }
}
