package com.vlada.lab3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RaftNodeService {
    @Value("${raft.node.id}")
    private int nodeId;

    @Value("${raft.total.nodes}")
    private int totalNodes;

    @Value("${raft.base.port}")
    private int basePort;

    private int currentTerm = 0;
    private volatile int votedFor = -1;
    private volatile boolean isLeader = false;
    private volatile long lastHeartbeatTime = System.currentTimeMillis();

    private final Random random = new Random();
    private static final int HEARTBEAT_INTERVAL = 2000; // ms
    private static final int ELECTION_TIMEOUT = 3000;

    private final AtomicBoolean electionInProgress = new AtomicBoolean(false);

    @Scheduled(fixedRate = HEARTBEAT_INTERVAL)
    public void sendHeartbeats() {
        if (isLeader) {
            for (int i = 0; i < totalNodes; i++) {
                if (i != nodeId) {
                    sendMessage(i, "HEARTBEAT:" + nodeId);
                }
            }
        }
    }

    @Scheduled(fixedRate = 1000)
    public void checkForElectionTimeout() {
        if (System.currentTimeMillis() - lastHeartbeatTime > ELECTION_TIMEOUT && !electionInProgress.get()) {
            startElection();
        }
    }

    private void startElection() {
        electionInProgress.set(true);
        currentTerm++;
        votedFor = nodeId;
        System.out.printf("Node %d started election for Term %d%n", nodeId, currentTerm);

        int votes = 1; // Vote for itself
        for (int i = 0; i < totalNodes; i++) {
            if (i != nodeId) {
                sendMessage(i, "REQUEST_VOTE:" + nodeId + ":" + currentTerm);
            }
        }

        // Simulate election delay
        try {
            Thread.sleep(random.nextInt(500) + 500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (votes > totalNodes / 2) {
            isLeader = true;
            System.out.printf("Node %d became the LEADER for Term %d%n", nodeId, currentTerm);
        }
        electionInProgress.set(false);
    }

    public void handleMessage(String message, int senderId) {
        String[] parts = message.split(":");
        String type = parts[0];

        switch (type) {
            case "HEARTBEAT":
                System.out.printf("Node %d received HEARTBEAT from Node %d%n", nodeId, senderId);
                lastHeartbeatTime = System.currentTimeMillis();
                isLeader = false; // Revert to follower
                break;
            case "REQUEST_VOTE":
                int term = Integer.parseInt(parts[2]);
                if (term > currentTerm) {
                    currentTerm = term;
                    votedFor = senderId;
                    System.out.printf("Node %d voted for Node %d in Term %d%n", nodeId, senderId, term);
                    sendMessage(senderId, "VOTE:" + nodeId);
                }
                break;
            case "VOTE":
                System.out.printf("Node %d received VOTE from Node %d%n", nodeId, senderId);
                break;
        }
    }

    private void sendMessage(int targetNodeId, String message) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName("localhost");
            int targetPort = basePort + targetNodeId;
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, targetPort);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

