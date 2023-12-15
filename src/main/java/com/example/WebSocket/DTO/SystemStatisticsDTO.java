package com.example.WebSocket.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatisticsDTO {
    private long userCount = 0;
    private long repoCount = 0;
    private long noteCount = 0;
    private LocalDate lastUpdate =  LocalDate.now();
}
