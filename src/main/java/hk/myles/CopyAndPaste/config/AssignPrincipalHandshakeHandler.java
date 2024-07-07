package hk.myles.CopyAndPaste.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class AssignPrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        request.getHeaders();
        if (request instanceof ServletServerHttpRequest) {
            String sessionId = ((ServletServerHttpRequest) request).getServletRequest().getSession().getId();
            return () -> sessionId;
        }

        return () -> "";
    }
}
