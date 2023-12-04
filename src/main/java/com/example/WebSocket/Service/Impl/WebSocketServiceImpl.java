package com.example.WebSocket.Service.Impl;

import com.example.WebSocket.DTO.WebSocketAnnouncementDTO;
import com.example.WebSocket.Service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {


    private SimpMessagingTemplate messagingTemplate;

    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(WebSocketAnnouncementDTO webSocketAnnouncementDTO) {
        messagingTemplate.convertAndSend(
                "/queue/notify",
                webSocketAnnouncementDTO
        );
    }

}
