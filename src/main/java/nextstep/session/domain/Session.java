package nextstep.session.domain;

import nextstep.DateDomain;
import nextstep.payments.domain.Payment;
import nextstep.session.domain.image.Image;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.List;

public class Session {

    private static final String PAID_SUBSCRIBE_MESSAGE = "유료강의는 결제내역이 필수입니다.";
    private static final String FREE_SUBSCRIBE_MESSAGE = "무료강의는 결제내역이 필요없습니다.";
    private static final String SUBSCRIBE_STATUS_NOT_WAIT_MESSAGE = "현재 강의가 모집중이 아닙니다.";
    private static final String SUBSCRIBE_COUNT_MAX_MESSAGE = "강의가 이미 만석입니다.";

    private Long id;
    private final String title;
    private final List<Image> image;
    private final PaymentType paymentType;
    private SubscribeStatus subscribeStatus;
    private int subscribeMax;
    private int price;
    private PickSession pickSession;
    private Subscribers subscribers = new Subscribers();
    private SessionPicks sessionPicks = new SessionPicks();
    private final DateRange dateRange;
    private final DateDomain dateDomain;

    public Session(String title, List<Image> image, PaymentType paymentType, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.image = image;
        this.paymentType = paymentType;
        this.subscribeStatus = SubscribeStatus.READY;
        this.subscribeMax = subscribeMax;
        this.price = price;
        this.dateRange = new DateRange(startDate, endDate);
        this.dateDomain = new DateDomain();
    }

    private Session(Long id, String title, List<Image> image, PickSession pickSession, PaymentType paymentType, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.pickSession = pickSession;
        this.paymentType = paymentType;
        this.subscribeStatus = SubscribeStatus.READY;
        this.subscribeMax = subscribeMax;
        this.price = price;
        this.dateRange = new DateRange(startDate, endDate);
        this.dateDomain = new DateDomain();
    }

    private Session(Long id, String title, List<Image> image, PickSession pickSession, PaymentType paymentType, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.pickSession = pickSession;
        this.paymentType = paymentType;
        this.subscribeStatus = SubscribeStatus.READY;
        this.dateRange = new DateRange(startDate, endDate);
        this.dateDomain = new DateDomain();
    }

    public Session(Long id, String title, String paymentType, String pickSession, String subscribeStatus, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate, List<Image> image, List<SessionPick> sessionPicks, List<Subscriber> subscribers, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.pickSession = PickSession.valueOf(pickSession);
        this.price = price;
        this.subscribeMax = subscribeMax;
        this.subscribeStatus = SubscribeStatus.valueOf(subscribeStatus);
        this.dateRange = new DateRange(startDate, endDate);
        this.image = image;
        this.sessionPicks = new SessionPicks(sessionPicks);
        this.subscribers = new Subscribers(subscribers);
        this.dateDomain = new DateDomain(createdAt, updatedAt);
    }

    public Session(Long id, String title, String paymentType, String subscribeStatus, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate, List<Image> image, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.price = price;
        this.subscribeMax = subscribeMax;
        this.subscribeStatus = SubscribeStatus.valueOf(subscribeStatus);
        this.image = image;
        this.dateRange = new DateRange(startDate, endDate);
        this.dateDomain = new DateDomain(createdAt, updatedAt);
    }

    public static Session createFree(Long id, String title, List<Image> image, PickSession pickSession, LocalDateTime startDate, LocalDateTime endDate) {
        return new Session(id, title, image, pickSession, PaymentType.FREE, startDate, endDate);
    }

    public static Session createPaid(Long id, String title, List<Image> image, PickSession pickSession, int subscribeMax, int price, LocalDateTime startDate, LocalDateTime endDate) {
        return new Session(id, title, image, pickSession, PaymentType.PAID, subscribeMax, price, startDate, endDate);
    }

    public Subscriber subscribe(NsUser user) {
        confirmSessionPick(user);
        confirmSubscribeStatus();
        if (paymentType == PaymentType.PAID) {
            throw new IllegalArgumentException(PAID_SUBSCRIBE_MESSAGE);
        }
        return subscribeUser(user);
    }

    public Subscriber subscribe(NsUser user, Payment payment) {
        confirmSessionPick(user);
        confirmSubscribeStatus();
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

    public void waitSession() {
        changeSubscribeStatus(SubscribeStatus.WAIT);
    }

    public void closedSession() {
        changeSubscribeStatus(SubscribeStatus.CLOSED);
    }

    public int getSubscribeCount() {
        return this.subscribers.subscribeUsersSize();
    }

    public SubscribeStatus getSubscribeStatus() {
        return subscribeStatus;
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

    public PickSession getPickSession() {
        return pickSession;
    }

    private void changeSubscribeStatus(SubscribeStatus subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }

    private Subscriber subscribeUser(NsUser nsUser) {
        return this.subscribers.addUser(this, nsUser);
    }


    private void confirmSubscribeStatus() {
        if (this.subscribeStatus != SubscribeStatus.WAIT) {
            throw new IllegalArgumentException(SUBSCRIBE_STATUS_NOT_WAIT_MESSAGE);
        }
    }

    private void confirmSubscribeMax() {
        if (this.subscribeMax < this.subscribers.subscribeUsersSize() + 1) {
            throw new IllegalArgumentException(SUBSCRIBE_COUNT_MAX_MESSAGE);
        }
    }

    private void confirmSessionPick(NsUser nsUser) {
        if (this.pickSession.checkPickSession()) {
            this.sessionPicks.confirmPickUser(nsUser);
        }
    }
}
