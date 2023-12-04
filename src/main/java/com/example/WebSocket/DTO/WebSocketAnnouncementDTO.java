package com.example.WebSocket.DTO;

import com.example.WebSocket.Entity.ServerEvent;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WebSocketAnnouncementDTO {
    ServerEvent serverEvent;
    String receiver;
}
