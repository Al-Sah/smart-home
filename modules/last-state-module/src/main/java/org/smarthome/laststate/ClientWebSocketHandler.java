package org.smarthome.laststate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.laststate.models.*;
import org.smarthome.sdk.models.DeviceMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            sendMessage(
                    ModuleMessageAction.START,
                    new StartMessage(
                            generateDevicesDescription(),
                            dbManager.getAllHubsState().stream().map(HubStateDTO::new).collect(Collectors.toList())
                    )
            );
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    private List<StartMessage.FullDeviceDescription> generateDevicesDescription(){
        var devices = dbManager.getAllDevices();
        var errors = dbManager.getAllDevicesErrors();
        var devicesState = dbManager.getAllDevicesState().stream().map(DeviceStateDTO::new).collect(Collectors.toList());

        var res = new ArrayList<StartMessage.FullDeviceDescription>();

        for (DeviceMetadata device : devices) {
            res.add(new StartMessage.FullDeviceDescription(
                    device,
                    errors.stream()
                            .filter((message -> Objects.equals(message.getDevice(), device.getId())))
                            .findFirst().orElse(null),
                    devicesState.stream()
                            .filter((state -> Objects.equals(state.getId(), device.getId())))
                            .findFirst().orElse(null)
            ));
        }
        return res;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        clientSession = null;
        logger.error("Connection {} closed with status {}", session.getRemoteAddress(), status.getCode());
    }

    public void sendMessage(ModuleMessageAction action, Object data) throws RuntimeException{

        if(clientSession == null){
            return;
        }

        try {
            clientSession.sendMessage(new TextMessage(jsonMapper.writeValueAsString(new ModuleMessage<>(action,data))));
        } catch (IOException e) {
            throw new RuntimeException("Failed to send message. Error: " + e.getMessage());
        }
    }

}
