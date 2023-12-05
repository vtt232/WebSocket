package com.example.WebSocket.Kafka;

import com.example.WebSocket.DTO.WebSocketAnnouncementDTO;
import com.example.WebSocket.Service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;
@Component
@Slf4j
public class KafkaListeners {

    private final WebSocketService webSocketService;

    public KafkaListeners(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }


    @KafkaListener(topics = "admin", groupId = "groupId")
    void listener(WebSocketAnnouncementDTO webSocketAnnouncementDTO){
        log.info("Send message to " + webSocketAnnouncementDTO.getReceiver());
        log.info("The event type is " + webSocketAnnouncementDTO.getServerEvent());
        webSocketService.sendMessage(webSocketAnnouncementDTO);
    }
}
