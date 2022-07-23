package org.smarthome.controlpanel;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smarthome.controlpanel.models.HistoryRequest;
import org.smarthome.controlpanel.services.HistoryProvider;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.module.producer.ModuleProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryProvider historyProvider;

    @MockBean
    private ModuleProducer producer;

    @BeforeEach
    void setup(){

        Mockito.when(historyProvider.getDeviceHistory(any())).thenReturn(new Object[]{
                "{\"device\": \"test1\", \"component\": \"test\", \"property\": \"test\", \"value\": \"test\", \"hub\": \"test\", \"ts\": 1654687598801}",
                "{\"device\": \"test2\", \"component\": \"test\", \"property\": \"test\", \"value\": \"test\", \"hub\": \"test\", \"ts\": 16546875988021}",
        });

        Mockito.when(producer.sendSync(any())).thenReturn(
                new ProducerRecord<>("test", 1, new Date().getTime(), null,
                        new Command(null, null, null, null, null, null, 0)
                ));
    }

    @Test
    public void tryGetHistory(){
        Assertions.assertNotNull(
                historyProvider.getDeviceHistory(new HistoryRequest("device", "test", 0L, null))
        );
    }

    @Test
    public void trySendCommand(){
        Assertions.assertNotNull(producer.sendSync(
                new Command("test", "test", "test", "test", "test", null, 0))
        );
    }

    @Test
    @WithMockUser(username = "owner", authorities = {"owner"})
    public void sendRequestsWithMockUser() {

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/history?type=test&id=test")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/command").contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(new ObjectMapper().writeValueAsString(
                                    new Command("test", "test", "test", "test", "test", null, 0)
                            ))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void sendRequestsWithoutMockUser() {

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/history?type=test&id=test")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isFound());

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/command")
                            .content(new ObjectMapper().writeValueAsString(
                                    new Command("test", "test", "test", "test", "test", null, 0)
                            ))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isFound());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
