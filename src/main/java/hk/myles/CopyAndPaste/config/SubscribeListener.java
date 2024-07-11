package hk.myles.CopyAndPaste.config;

import hk.myles.CopyAndPaste.model.User;
import hk.myles.CopyAndPaste.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class SubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    private static final Logger log = LoggerFactory.getLogger(SubscribeListener.class);
    public static final String USER_TOPIC = "/topic/user";
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    public SubscribeListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    private void createOrUpdateUser(String userId) {
        Optional<User> optionalUser = userService.fetchUserById(userId);
        if (optionalUser.isPresent()) {
            // existing user
            userService.updateLastSeenById(userId);
        } else {
            // create user
            userService.createUserById(userId);
            log.info("User with ID {} created", userId);
        }
    }

    private void broadcastLatestUsers() {
        List<User> latestUsers = userService.fetchUsers();
        messagingTemplate.convertAndSend(
                USER_TOPIC,
                latestUsers
        );
        log.info("Latest users broadcast");
    }

    private void cleanupInactiveUsers() {
        // 12 hours ago
        Date inactiveThreshold = new Date(System.currentTimeMillis() - 3600 * 1000 * 12);

        userService.fetchUsers().forEach(user -> {
            if (user.getLastSeen().before(inactiveThreshold)) {
                userService.removeUserById(user.getId());
            }
        });
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        String destination = Objects.requireNonNull(event.getMessage().getHeaders().get("simpDestination")).toString();
        String userId = Objects.requireNonNull(event.getUser()).getName();
        if (Objects.equals(destination, USER_TOPIC) && !userId.isEmpty()) {
            log.info("User with session ID {} connected", userId);
            cleanupInactiveUsers();
            createOrUpdateUser(userId);
            broadcastLatestUsers();

        }
    }
}