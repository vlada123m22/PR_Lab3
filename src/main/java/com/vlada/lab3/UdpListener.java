package com.vlada.lab3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Component
public class UdpListener {

    @Value("${server.port}")
    private int port;

    private final RaftNodeService nodeService;

    public UdpListener(RaftNodeService nodeService) {
        this.nodeService = nodeService;
    }

    @EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
    public void startListener() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(port)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    int senderId = packet.getPort() - 8080; // Calculate sender node ID
                    nodeService.handleMessage(message, senderId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
