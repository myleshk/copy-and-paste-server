package hk.myles.CopyAndPaste.controller;

import hk.myles.CopyAndPaste.model.Message;
import hk.myles.CopyAndPaste.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @MessageMapping("/message")
    public void sendMessage(Message message) {
        userService.updateLastSeenById(message.getFromId());

        simpMessagingTemplate.convertAndSendToUser(
                message.getToId(),
                "/queue/message",
                message
        );
    }


}
