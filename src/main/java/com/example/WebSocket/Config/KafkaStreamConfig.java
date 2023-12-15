package com.example.WebSocket.Config;

import com.example.WebSocket.DTO.SystemStatisticsDTO;
import com.example.WebSocket.Deserializer.SystemStatisticsDeserializer;
import com.example.WebSocket.Service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;


import java.util.Objects;
import java.util.Properties;

@Configuration
@Slf4j
public class KafkaStreamConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    private WebSocketService webSocketService;

    public static final String SYSTEM_STATISTICS = "system-statistics";

    public KafkaStreamConfig(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }


    @Bean
    public Properties kafkaStreamsProps() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "websocket-backend");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return properties;
    }


    @Bean
    public Topology buildTopology() {

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        SystemStatisticsDeserializer systemStatisticsDeserializer = new SystemStatisticsDeserializer();



        KStream<String, Object> systemStatisticsStream = streamsBuilder.stream(SYSTEM_STATISTICS, Consumed.with(Serdes.String(), Serdes.String())).mapValues((key, value) -> {
            log.info("GET SYSTEM INFOR FROM KAFKA");
            // Create an ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();

            // Deserialize the string to SystemStatisticsDTO
            try {
                SystemStatisticsDTO systemStatisticsDTO = objectMapper.readValue(value, SystemStatisticsDTO.class);
                webSocketService.sendSystemStatistics(systemStatisticsDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return value;
        });

        return streamsBuilder.build();

    }

    @Bean
    public KafkaStreams kafkaStreams(@Qualifier("kafkaStreamsProps") Properties properties) {
        Topology topology = buildTopology();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);

        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

        return kafkaStreams;

    }

}
