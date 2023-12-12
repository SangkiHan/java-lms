package nextstep.courses.domain.session;

import nextstep.courses.CannotEnrollStateException;
import nextstep.courses.ExceedMaxAttendanceCountException;
import nextstep.courses.domain.coverImage.CoverImages;
import nextstep.courses.domain.sessionuser.SessionUser;
import nextstep.courses.domain.sessionuser.SessionUsers;
import nextstep.courses.domain.sessionuser.UserType;

import java.time.LocalDateTime;

public class Session {

    private Long id;

    private Long courseId;

    private CoverImages coverImages = new CoverImages();

    private SessionStatus sessionStatus = SessionStatus.PREPARE;

    private SessionEnrollStatus sessionEnrollStatus = SessionEnrollStatus.NOT_ENROLL;

    private Period period;

    private SessionUsers tutors = new SessionUsers();
    private SessionUsers students = new SessionUsers();

    private SessionType sessionType;

    public Session(SessionEnrollStatus sessionEnrollStatus) {
        this.sessionEnrollStatus = sessionEnrollStatus;
        this.sessionType = SessionType.freeSession();
    }

    public Session(SessionEnrollStatus sessionEnrollStatus, Integer maxAttendance) {
        this.sessionEnrollStatus = sessionEnrollStatus;
        this.sessionType = SessionType.notFreeSession(maxAttendance);
    }

    public Session(CoverImages coverImages, Period period, SessionType sessionType) {
        this.coverImages = coverImages;
        this.period = period;
        this.sessionType = sessionType;
    }

    public Session(Long courseId, SessionStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime,
                   boolean free, Integer maxAttendance) {
        this.courseId = courseId;
        this.sessionStatus = status;
        this.period = new Period(startDateTime, endDateTime);
        this.sessionType = free ? SessionType.freeSession() : SessionType.notFreeSession(maxAttendance);
    }

    public Session(Long id, Long courseId, SessionStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime,
                   boolean free, Integer maxAttendance) {
        this.id = id;
        this.courseId = courseId;
        this.sessionStatus = status;
        this.period = new Period(startDateTime, endDateTime);
        this.sessionType = free ? SessionType.freeSession() : SessionType.notFreeSession(maxAttendance);
    }

    public Session(Long id, Long courseId, SessionStatus status, SessionEnrollStatus sessionEnrollStatus, LocalDateTime startDateTime, LocalDateTime endDateTime,
                   boolean free, Integer maxAttendance) {
        this.id = id;
        this.courseId = courseId;
        this.sessionStatus = status;
        this.sessionEnrollStatus = sessionEnrollStatus;
        this.period = new Period(startDateTime, endDateTime);
        this.sessionType = free ? SessionType.freeSession() : SessionType.notFreeSession(maxAttendance);
    }

    public static Session notFreeSession(CoverImages coverImages, int maxAttendance, Period period) {
        SessionType sessionType = SessionType.notFreeSession(maxAttendance);
        return new Session(coverImages, period, sessionType);
    }

    public static Session freeSession(CoverImages coverImages, Period period) {
        SessionType sessionType = SessionType.freeSession();
        return new Session(coverImages, period, sessionType);
    }

    public void addSessionUser(SessionUser sessionUser) {
        if (sessionUser.userType() == UserType.TUTOR) {
            tutors.add(sessionUser);
            return;
        }

        if (!canRegisterNewUser(students.notCanceledUserSize())) {
            throw new ExceedMaxAttendanceCountException("이미 최대 수강 인원이 다 찼습니다.");
        }
        students.add(sessionUser);
    }

    public long tutorsSize() {
        return tutors.notCanceledUserSize();
    }

    public long studentSize() {
        return students.notCanceledUserSize();
    }


    public boolean isAfterCourseWasCreated(LocalDateTime createdAt) {
        return period.isAfter(createdAt);
    }

    public void bindWithCourse(Long courseId) {
        this.courseId = courseId;
    }

    private boolean canRegisterNewUser(long currentUserSize) {
        if (sessionEnrollStatus.equals(SessionEnrollStatus.NOT_ENROLL)) {
            throw new CannotEnrollStateException("수강 인원 모집중인 강의가 아닙니다.");
        }

        return sessionType.canRegisterNewUser(currentUserSize);
    }

    public LocalDateTime startDate() {
        return period.getStartDateTime();
    }

    public LocalDateTime endDate() {
        return period.getEndDateTime();
    }

    public boolean isFree() {
        return sessionType.isFree();
    }

    public Integer maxAttendance() {
        return sessionType.getMaxAttendance();
    }

    public String sessionStatus() {
        return sessionStatus.name();
    }

    public Long getId() {
        return id;
    }

    public Long courseId() {
        return courseId;
    }

}