package com.kafka.kafka;

import Evalutor.RunCP;
import Evalutor.Submission;
import Singleton.ThreadPoolManager;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;


public class KafkaConsumerListener {
    @KafkaListener(topics = "test")
    public void listen04(ConsumerRecord<String,byte[]> consumerRecord){
        System.out.println("开始消费test的消息");
        System.out.println(consumerRecord.value());
        byte[] data = consumerRecord.value();
                // 字节数组转string数据
        System.out.println(new String(data));
//         反序列化成submission
        Submission submission= JSONObject.parseObject(data,Submission.class);
        if(submission!=null) {
            //此处有并发问题
            ThreadPoolManager.getFixedThreadPool().execute(new RunCP(submission));
            System.out.println("接收消息submission,核对下是否和发送过来的一样： " + submission);
        }
    }
}