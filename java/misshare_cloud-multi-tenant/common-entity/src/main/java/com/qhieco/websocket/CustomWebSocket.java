package com.qhieco.websocket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/19 下午3:28
 * <p>
 * 类说明：
 *     处理websocket的session
 */
@Slf4j
@Component
public class CustomWebSocket {

    private static AtomicInteger count = new AtomicInteger();

    public static ConcurrentHashMap<Integer, Set<Session>> sessionMap = new ConcurrentHashMap<>();

    public static void newSession(Integer userId, Session session) {
        Set<Session> sessionSet;
        if ((sessionSet = sessionMap.get(userId)) == null) {
            sessionSet = new HashSet<>();
        }
        sessionSet.add(session);
        log.info("new session, new count={}", count.incrementAndGet());
    }

    public static void closeSession(Integer userId) {
        sessionMap.remove(userId);
        log.info("close session, now count={}", count.decrementAndGet());
    }

    /**
     * 发送消息到客户端
     * @param userId userId
     * @param message message
     */
    @SneakyThrows
    public static void sendMessage(Integer userId, String message) {
        final Set<Session> sessionSet = sessionMap.get(userId);
        for (Session session: sessionSet) {
            session.getBasicRemote().sendText(message);
        }
    }
}
