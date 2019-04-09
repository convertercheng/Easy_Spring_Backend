package com.qhieco.apiwrite.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qhieco.websocket.CustomWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/19 下午3:21
 * <p>
 * 类说明：
 *     发布服务
 */
@Slf4j
@Component
@ServerEndpoint(value = "/order")
public class OrderStateWebSocket {

    public OrderStateWebSocket() {
        log.info("\n---- 构造器：OrderStateWebSocket建立 ----\n");
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("webSocket open");
    }

    @OnClose
    public void onClose(Session session) {
        ConcurrentHashMap<Integer, Set<Session>> sessionMap = CustomWebSocket.sessionMap;
        final Set<Map.Entry<Integer, Set<Session>>> entries = sessionMap.entrySet();
        Set<Integer> userIds = new HashSet<>();
        for (Map.Entry<Integer, Set<Session>> entry: entries) {
            if (entry.getValue().contains(session)) {
                final Integer userId = entry.getKey();
                userIds.add(userId);
            }
        }
        userIds.forEach(CustomWebSocket::closeSession);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject receivedMsg = JSON.parseObject(message);
        final int userId = receivedMsg.getInteger("userId");
        CustomWebSocket.newSession(userId, session);
        CustomWebSocket.sendMessage(userId, "success");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info(session.getId() + " > OnError: " + throwable.getMessage());
        throwable.printStackTrace();
        onClose(session);
    }


}
