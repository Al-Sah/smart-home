package org.smarthome.laststate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.laststate.models.ClientStartMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;

@Service
public class ClientWebSocketHandler extends AbstractWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientWebSocketHandler.class);

    private final DataBaseManager dbManager;


    private final ObjectMapper jsonMapper = new ObjectMapper();
    private WebSocketSession clientSession = null;

    public ClientWebSocketHandler(DataBaseManager dbManager) {
        this.dbManager = dbManager;
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
            sendMessage(new ClientStartMessage(
                    dbManager.getAllDevices(),
                    dbManager.getAllDevicesErrors(),
                    dbManager.getAllDevicesState(),
                    dbManager.getAllHubsState()
            ));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        clientSession = null;
        logger.error("Connection {} closed with status {}", session.getRemoteAddress(), status.getCode());
    }

    public void sendMessage(Object data) throws RuntimeException{

        if(clientSession == null){
            return;
        }

        try {
            clientSession.sendMessage(new TextMessage(jsonMapper.writeValueAsString(data)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to send message. Error: " + e.getMessage());
        }
    }

}
