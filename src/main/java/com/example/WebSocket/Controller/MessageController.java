package com.example.WebSocket.Controller;


import com.example.WebSocket.DTO.WebSocketAnnouncementDTO;
import com.example.WebSocket.Service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/mess/")
@Slf4j
public class MessageController {

    private final WebSocketService webSocketService;

    public MessageController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @PostMapping(path="/announce", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> announceNewAdmin(@RequestBody WebSocketAnnouncementDTO webSocketAnnouncementDTO) {
        System.out.println(webSocketAnnouncementDTO.getReceiver());
        System.out.println(webSocketAnnouncementDTO.getServerEvent());
        webSocketService.sendMessage(webSocketAnnouncementDTO);
        return ResponseEntity.ok(true);
    }
}
