package nextstep.session.domain;

import nextstep.DateDomain;
import nextstep.payments.domain.Payment;
import nextstep.session.RecruitmentStatus;
import nextstep.session.domain.image.Image;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.List;

public class Session {

    private static final String EXIST_PICK_MESSAGE = "선발된 인원이므로 강의 취소가 불가합니다.";
    private static final String PAID_SUBSCRIBE_MESSAGE = "유료강의는 결제내역이 필수입니다.";
    private static final String FREE_SUBSCRIBE_MESSAGE = "무료강의는 결제내역이 필요없습니다.";
    private static final String SESSION_STATUS_NOT_CLOSED_MESSAGE = "종료된 강의입니다.";
    private static final String RECRUITMENT_STATUS_CLOSED_MESSAGE = "모집이 종료된 강의입니다.";
    private static final String SUBSCRIBE_COUNT_MAX_MESSAGE = "강의가 이미 만석입니다.";

    private Long id;
    private final String title;
    private final List<Image> image;
    private final PaymentType paymentType;
    private SessionStatus sessionStatus;
    private RecruitmentStatus recruitmentStatus;
    private int subscribeMax;
    private int price;
    private Subscribers subscribers = new Subscribers();
    private SessionPicks sessionPicks = new SessionPicks();
    private final DateRange dateRange;
    private final DateDomain dateDomain;

    public Session(String title, List<Image> image, PaymentType paymentType, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.image = image;
        this.paymentType = paymentType;
        this.sessionStatus = SessionStatus.READY;
        this.subscribeMax = subscribeMax;
        this.price = price;
        this.dateRange = new DateRange(startDate, endDate);
        this.dateDomain = new DateDomain();
    }

    private Session(Long id, String title, List<Image> image, PaymentType paymentType, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.paymentType = paymentType;
        this.sessionStatus = SessionStatus.READY;
        this.recruitmentStatus = RecruitmentStatus.NON_RECRUIT;
        this.subscribeMax = subscribeMax;
        this.price = price;
        this.dateRange = new DateRange(startDate, endDate);
        this.dateDomain = new DateDomain();
    }

    private Session(Long id, String title, List<Image> image, PaymentType paymentType, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.paymentType = paymentType;
        this.sessionStatus = SessionStatus.READY;
        this.recruitmentStatus = RecruitmentStatus.NON_RECRUIT;
        this.dateRange = new DateRange(startDate, endDate);
        this.dateDomain = new DateDomain();
    }

    public Session(Long id, String title, String paymentType, String sessionStatus, String recruitmentStatus, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate, List<Image> image, List<SessionPick> sessionPicks, List<Subscriber> subscribers, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.recruitmentStatus = RecruitmentStatus.valueOf(recruitmentStatus);
        this.price = price;
        this.subscribeMax = subscribeMax;
        this.sessionStatus = SessionStatus.valueOf(sessionStatus);
        this.dateRange = new DateRange(startDate, endDate);
        this.image = image;
        this.sessionPicks = new SessionPicks(sessionPicks);
        this.subscribers = new Subscribers(subscribers);
        this.dateDomain = new DateDomain(createdAt, updatedAt);
    }

    public Session(Long id, String title, String paymentType, String sessionStatus, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate, List<Image> image, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.price = price;
        this.subscribeMax = subscribeMax;
        this.sessionStatus = SessionStatus.valueOf(sessionStatus);
        this.image = image;
        this.dateRange = new DateRange(startDate, endDate);
        this.dateDomain = new DateDomain(createdAt, updatedAt);
    }

    public static Session createFree(Long id, String title, List<Image> image, LocalDateTime startDate, LocalDateTime endDate) {
        return new Session(id, title, image, PaymentType.FREE, startDate, endDate);
    }

    public static Session createPaid(Long id, String title, List<Image> image, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate) {
        return new Session(id, title, image, PaymentType.PAID, subscribeMax, price, startDate, endDate);
    }

    public Subscriber subscribe(NsUser user) {
        confirmAllSessionStatus();
        if (paymentType == PaymentType.PAID) {
            throw new IllegalArgumentException(PAID_SUBSCRIBE_MESSAGE);
        }
        return subscribeUser(user);
    }

    public Subscriber subscribe(NsUser user, Payment payment) {
        confirmAllSessionStatus();
        if (paymentType == PaymentType.FREE) {
            throw new IllegalArgumentException(FREE_SUBSCRIBE_MESSAGE);
        }
        payment.checkMatchAmount(this.price);
        confirmSubscribeMax();
        return subscribeUser(user);
    }

    public SessionPick enrollPick(NsUser user) {
        return this.sessionPicks.addUser(this, user);
    }

    public void processSession() {
        changeSessionStatus(SessionStatus.PROCESS);
    }

    public void closedSession() {
        changeSessionStatus(SessionStatus.CLOSED);
    }

    public void recruitSession() {
        this.recruitmentStatus = RecruitmentStatus.RECRUIT;
    }

    public void nonRecruitSession() {
        this.recruitmentStatus = RecruitmentStatus.NON_RECRUIT;
    }

    public Long cancelSubscribe(NsUser user) {
        confirmSessionPick(user);
        return getSubscriberId(user);
    }

    public int getSubscribeCount() {
        return this.subscribers.subscribeUsersSize();
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Image> getImage() {
        return image;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public int getSubscribeMax() {
        return subscribeMax;
    }

    public int getPrice() {
        return price;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public DateDomain getDateDomain() {
        return dateDomain;
    }

    public Subscribers getSubscribers() {
        return subscribers;
    }

    public boolean checkFreePaid() {
        return this.paymentType.equals(PaymentType.FREE);
    }

    public SessionPicks getSessionPicks() {
        return sessionPicks;
    }

    public RecruitmentStatus getRecruitmentStatus() {
        return recruitmentStatus;
    }

    private void changeSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    private Subscriber subscribeUser(NsUser nsUser) {
        return this.subscribers.addUser(this, nsUser);
    }

    private void confirmAllSessionStatus() {
        confirmSessionStatus();
        confirmRecruitmentStatus();
    }

    private void confirmSessionStatus() {
        if (this.sessionStatus == sessionStatus.CLOSED) {
            throw new IllegalArgumentException(SESSION_STATUS_NOT_CLOSED_MESSAGE);
        }
    }

    private void confirmSubscribeMax() {
        if (this.subscribeMax < this.subscribers.subscribeUsersSize() + 1) {
            throw new IllegalArgumentException(SUBSCRIBE_COUNT_MAX_MESSAGE);
        }
    }

    private void confirmRecruitmentStatus() {
        if(this.recruitmentStatus == RecruitmentStatus.NON_RECRUIT) {
            throw new IllegalArgumentException(RECRUITMENT_STATUS_CLOSED_MESSAGE);
        }
    }

    private void confirmSessionPick(NsUser user) {
        if(this.sessionPicks.checkPickUser(user)) {
            throw new IllegalArgumentException(EXIST_PICK_MESSAGE);
        }
    }

    private Long getSubscriberId(NsUser user) {
        return this.subscribers.getSubscribeId(user);
    }

}
