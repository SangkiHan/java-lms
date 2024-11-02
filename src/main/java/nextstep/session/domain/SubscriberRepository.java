package nextstep.session.domain;

public interface SubscriberRepository {

    int save(Subscriber subscriber);

    Subscriber findById(Long id);

    void deleteById(Long id);

}
