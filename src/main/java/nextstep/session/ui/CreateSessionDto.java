package nextstep.session.ui;

import nextstep.session.domain.FreeSession;
import nextstep.session.domain.ImageType;
import nextstep.session.domain.PaidSession;
import nextstep.session.domain.Session;
import nextstep.session.domain.SessionImage;
import nextstep.session.domain.SessionType;
import nextstep.users.domain.NsUser;

import java.time.LocalDate;

public class CreateSessionDto {
    private Long courseId;
    private int generation;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageURL;
    private int imageSize;
    private ImageType imageType;
    private int width;
    private int height;
    private SessionType sessionType;
    private int limitNumberOfStudents;
    private Long price;

    public Long getCourseId() {
        return courseId;
    }

    public Session toSession(NsUser nsUser) {
        if (sessionType.equals(SessionType.FREE)) {
            return FreeSession.create(generation, nsUser.getId(), startDate, endDate, this.toSessionImage());
        }
        return PaidSession.create(generation, nsUser.getId(), startDate, endDate, this.toSessionImage(), limitNumberOfStudents, price);
    }

    private SessionImage toSessionImage() {
        return SessionImage.of(imageURL, imageSize, width, height);
    }
}
