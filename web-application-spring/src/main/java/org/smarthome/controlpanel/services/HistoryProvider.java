package org.smarthome.controlpanel.services;

import org.smarthome.controlpanel.config.HistoryModuleConfiguration;
import org.smarthome.controlpanel.models.HistoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public ResponseEntity<Object[]> getDeviceHistory(HistoryRequest historyRequest){

        var uri = UriComponentsBuilder.fromUriString(template)
                .buildAndExpand(historyRequest.type(), historyRequest.id())
                .toUriString() + getQueryParams(historyRequest);

        return new RestTemplate().getForEntity(uri, Object[].class); // TODO handle errors
    }
}
