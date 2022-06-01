package org.smarthome.laststate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.laststate.repositories.DevicesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;

@Service
public class ClientWebSocketHandler extends AbstractWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientWebSocketHandler.class);

    private final DevicesRepository devicesRepository;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private WebSocketSession clientSession = null;

    public ClientWebSocketHandler(DevicesRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception){
        logger.error("TransportError: {}, {}", exception.getMessage(), session.getRemoteAddress());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        if(clientSession != null){
            logger.error("Connection exists");
            return;
        }
        clientSession = session;
        logger.info("Created new connection {}", session.getRemoteAddress());

        try {
            sendTextMessage(jsonMapper.writeValueAsString(devicesRepository.findAll()));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        clientSession = null;
        logger.error("Connection {} closed with status {}", session.getRemoteAddress(), status.getCode());
    }

    public void sendTextMessage(String json){
        if(clientSession == null){
            return;
        }
        try {
            clientSession.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            logger.error("Fault send message. Error: {}", e.getMessage());
        }
    }

}
