package nextstep.session.domain;

import nextstep.users.domain.NsUser;

import java.util.ArrayList;
import java.util.List;

public class Subscribers {

    private static final String NOT_EXIST_SUBSCRIBER_MESSAGE = "해당 인원은 강의를 신청하지 않았습니다.";

    private List<Subscriber> subscribeUsers = new ArrayList<>();

    public Subscribers() {
    }

    public Subscribers(List<Subscriber> subscribeUsers) {
        this.subscribeUsers = subscribeUsers;
    }

    public int subscribeUsersSize() {
        return this.subscribeUsers.size();
    }

    public Subscriber addUser(Session session, NsUser nsUser) {
        Subscriber subscriber = new Subscriber(session, nsUser);
        this.subscribeUsers.add(subscriber);
        return subscriber;
    }

    public List<Subscriber> getSubscribeUsers() {
        return subscribeUsers;
    }

    public Long getSubscribeId(NsUser user) {
        return this.subscribeUsers.stream()
                .filter(subscriber -> subscriber.checkNsUser(user))
                .findFirst()
                .map(Subscriber::getId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_SUBSCRIBER_MESSAGE));
    }
}
