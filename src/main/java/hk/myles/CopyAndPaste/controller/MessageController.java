package hk.myles.CopyAndPaste.controller;

import hk.myles.CopyAndPaste.model.User;
import hk.myles.CopyAndPaste.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    public void sendMessage(Message message) {
        simpMessagingTemplate.convertAndSendToUser(
                message.getToId(),
                "/queue/message",
                message
        );
    }


}
