package nextstep.session.service;

import nextstep.session.domain.ImageRepository;
import nextstep.session.domain.Session;
import nextstep.session.domain.SessionRepository;
import nextstep.session.domain.SubscribeStatus;
import nextstep.session.domain.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, ImageRepository imageRepository) {
        this.sessionRepository = sessionRepository;
        this.imageRepository = imageRepository;
    }

    public void save(Session session) {
        sessionRepository.save(session);
        session.getImage().forEach(imageRepository::save);
    }

    public Session findById(Long sessionId) {
        return sessionRepository.findById(sessionId);
    }

    public void changeSubscribeStatus(Long sessionId, SubscribeStatus subscribeStatus) {
        sessionRepository.updateSubscribeStatus(sessionId, subscribeStatus);
    }

}
