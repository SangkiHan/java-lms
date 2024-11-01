package nextstep.session.service;

import nextstep.payments.domain.Payment;
import nextstep.session.domain.*;
import nextstep.users.domain.NsUser;
import nextstep.users.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SessionPickService {

    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final SessionPickRepository sessionPickRepository;

    @Autowired
    public SessionPickService(SessionService sessionService, UserRepository userRepository, SessionPickRepository sessionPickRepository) {
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.sessionPickRepository = sessionPickRepository;
    }

    public void enrollPickUser(Long sessionId, Long userId) {
        Session session = sessionService.findById(sessionId);
        NsUser nsUser = userRepository.findById(userId);

        SessionPick sessionPick = session.enrollPick(nsUser);
        sessionPickRepository.save(sessionPick);
    }
}
