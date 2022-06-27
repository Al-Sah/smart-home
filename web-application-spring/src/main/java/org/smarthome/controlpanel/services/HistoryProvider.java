package org.smarthome.controlpanel.services;

import org.smarthome.controlpanel.config.HistoryModuleConfiguration;
import org.smarthome.controlpanel.models.HistoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

@Service
public class HistoryProvider {

    private final String template;

    public HistoryProvider(HistoryModuleConfiguration configuration){
        template = configuration.getUri() + "/{object}/{id}";
    }

    private String getQueryParams(HistoryRequest historyRequest){

        if(historyRequest.from() == null && historyRequest.to() == null){
            return "";
        }
        var sb = new StringBuilder("?");
        if(historyRequest.from() != null){
            sb.append("ts_gt=").append(historyRequest.from()).append('&');
        }
        if(historyRequest.to() != null){
            sb.append("ts_lt=").append(historyRequest.to());
        }
        return sb.toString();
    }

    public Object[] getDeviceHistory(HistoryRequest historyRequest){

        //TODO remove
        if(historyRequest.from() == null && historyRequest.to() == null){
            historyRequest = new HistoryRequest(historyRequest.type(), historyRequest.id(), 0L, new Date().getTime());
        } else {
            if(historyRequest.from() == null){
                historyRequest = new HistoryRequest(historyRequest.type(), historyRequest.id(), 0L,historyRequest.to());
            }
            if(historyRequest.to() == null){
                historyRequest = new HistoryRequest(historyRequest.type(), historyRequest.id(), historyRequest.from(), new Date().getTime());
            }
        }

        var uri = UriComponentsBuilder.fromUriString(template)
                .buildAndExpand(historyRequest.type(), historyRequest.id())
                .toUriString() + getQueryParams(historyRequest);

        return new RestTemplate().getForObject(uri, Object[].class); // TODO handle errors
    }
}
