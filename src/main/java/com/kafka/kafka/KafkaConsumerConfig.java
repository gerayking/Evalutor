package com.kafka.kafka;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;


@Configurable
@Component
@EnableKafka
public class KafkaConsumerConfig {


    public KafkaConsumerConfig() {
        System.out.println("kafka消费者配置加载...");
    }

    public Map<String, Object> consumerProperties() {
        Map<String, Object> props = new HashMap<String, Object>();

        //Kafka服务地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //消费组
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"consumer-group-id");
        //设置
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        //设置间隔时间
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        //Key反序列化类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,  "org.apache.kafka.common.serialization.StringDeserializer");
        //Value反序列化
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,   "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        //从头开始消费
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }

    public ConsumerFactory<String, byte[]> consumerFactory() {
        return new DefaultKafkaConsumerFactory<String, byte[]>(consumerProperties());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, byte[]>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public KafkaConsumerListener kafkaConsumerListener() {
        return new KafkaConsumerListener();
    }
}