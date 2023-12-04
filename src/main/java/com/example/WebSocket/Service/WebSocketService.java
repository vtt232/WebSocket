package com.example.WebSocket.Service;

import com.example.WebSocket.DTO.WebSocketAnnouncementDTO;

public interface WebSocketService {
    public void sendMessage(WebSocketAnnouncementDTO webSocketAnnouncementDTO);
}
