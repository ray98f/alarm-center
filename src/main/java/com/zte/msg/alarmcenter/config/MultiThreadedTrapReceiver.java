package com.zte.msg.alarmcenter.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zte.msg.alarmcenter.dto.KafkaResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpResParsingDTO;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Data
@Component
public class MultiThreadedTrapReceiver implements CommandResponder {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka-monitor.port}")
    private String port;

    @Value("${kafka-monitor.topic}")
    private String topic;

    private MultiThreadedMessageDispatcher dispatcher;
    private Snmp snmp = null;
    private Address listenAddress;
    private ThreadPool threadPool;

    public MultiThreadedTrapReceiver() {
    }

    private void init() throws IOException {
        threadPool = ThreadPool.create("Trap", 2);
        dispatcher = new MultiThreadedMessageDispatcher(threadPool,
                new MessageDispatcherImpl());
        listenAddress = GenericAddress.parse(port); // 本地IP与监听端口
        TransportMapping transport;
        // 对TCP与UDP协议进行处理
        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping(
                    (UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping(
                    (TcpAddress) listenAddress);
        }
        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(
                MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();
    }

    @PostConstruct
    public void run() {
        try {
            init();
            snmp.addCommandResponder(this);
            System.out.println("开始监听Trap信息!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 当监听到消息时，会自动调用该方法
     */
    public void processPdu(CommandResponderEvent respEvent) {
        // 解析Response
        if (respEvent != null && respEvent.getPDU() != null) {
            Vector<VariableBinding> recVBs = respEvent.getPDU().getVariableBindings();
            List<SnmpResParsingDTO> list = new ArrayList<>();
            for (int i = 0; i < recVBs.size(); i++) {
                SnmpResParsingDTO snmpResParsingDTO = new SnmpResParsingDTO();
                VariableBinding recVB = recVBs.elementAt(i);
                // 处理数据
                snmpResParsingDTO.setOid(recVB.getOid().toString());
                snmpResParsingDTO.setVariable(recVB.getVariable().toString());
                list.add(snmpResParsingDTO);
            }
            String message = JSON.toJSONString(list);
            kafkaTemplate.send(topic, message).addCallback(success -> {
                assert success != null;
                System.out.println("发送消息成功:" +
                        "topic:" + success.getRecordMetadata().topic() + "," +
                        "分区:" + success.getRecordMetadata().partition() + "," +
                        "偏移量:" + success.getRecordMetadata().offset() + "," +
                        "数据:" + message);
            }, failure -> {
                System.out.println("发送消息失败:" + failure.getMessage());
            });
        }
    }

    @KafkaListener(topics = {"${kafka-monitor.topic}"}, containerFactory = "batchFactory", errorHandler = "consumerAwareErrorHandler")
    public void receive(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        System.out.println(">>>批量消费一次，数量 = " + records.size());
        for (ConsumerRecord<?, ?> record : records) {
            List<KafkaResDTO> kafkaResList = JSON.parseObject(record.value().toString(), new TypeReference<List<KafkaResDTO>>() {
            }.getType());
            // todo 处理数据
            for (KafkaResDTO kafkaResDTO : kafkaResList) {
                System.out.println("oid = " + kafkaResDTO.getOid());
                System.out.println("variable = " + kafkaResDTO.getVariable());
            }
        }
        // 告知kafka消费成功
        ack.acknowledge();
    }

    /**
     * 过滤器
     * 原先拉去数据是单个json 现在会是多个 list 设置factory.setConcurrency(1)为一跳，
     * 但返回数据还是list。这里的 1条是你提交1次的数据。你一次提交了20条也是一个json里的所有
     * 算做一条数据。
     */
    @Bean
    public KafkaListenerContainerFactory<?> batchFactory(ConsumerFactory<? super Integer, ? super String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // 消费几条数据
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(1500);
        //设置为批量消费，每个批次数量在Kafka配置参数中设置
        factory.setBatchListener(true);
        //设置手动提交ackMode
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤策略
        factory.setRecordFilterStrategy(consumerRecord -> {
            return consumerRecord.value() == null && !"".equals(consumerRecord.value());
            //返回true消息则被过滤
        });
        return factory;
    }


    /**
     * 异常处理器
     *
     * @return
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
        return (message, exception, consumer) -> {
            System.out.println("消费异常：" + message.getPayload());
            // todo 异常处理
            return null;
        };
    }

}
