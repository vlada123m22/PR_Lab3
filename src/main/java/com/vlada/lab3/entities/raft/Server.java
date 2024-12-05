package com.vlada.lab3.entities;

import com.vlada.lab3.repository.ServerLogRepository;
import com.vlada.lab3.services.ServerService;
import lombok.Data;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import javax.swing.text.TabSet;
import java.time.LocalDateTime;
import java.util.*;


@Data
public class Server implements Runnable {
    class NewTermTask extends TimerTask{

        @Override
            public void run() {
            //candidate but only if lasthearbeat is null or timedout
            if (!Server.this.inVotingSession && (Server.this.timeoutDateTime == null || Server.this.timeoutDateTime.isBefore(LocalDateTime.now()))) {
                for (Server server : servers) {
                    server.setInVotingSession(true);
                }

                System.out.println(LocalDateTime.now().toString() + " " + Server.this.serverId + " timeot DateTime: " + timeoutDateTime);
                System.out.println("A new term started. " + Server.this.serverId + " becomes a candidate");
                int upvotes = 0;
                int downvotes = 0;

                //TODO increment by 1 every time a server votes yes
                for (Server server : servers) {
                    if (server.vote(Server.this)) upvotes++;
                    else downvotes++;
                }

                //if the majority voted for this server, make it the leader
                if(upvotes>downvotes){
                    for (Server server : servers) {
                        server.setLeader(Server.this);
                        server.setTimeoutDateTime(LocalDateTime.now().minusNanos(1000000L*server.getTimeoutMilisec()));
                    }
                    ServerService.leader = Server.this.leader;
                    System.out.println(Server.this.serverId + " became the leader");
                }else System.out.println(Server.this.serverId + " did not become the leader");

                for (Server server : servers) {
                    server.setInVotingSession(false);
                }

            }
        }
    }




    class HeartbeatTask extends TimerTask{

        @Override
        public void run() {
            if (Server.this.leader == Server.this){
                Server.this.sendHeartbeat();

                System.out.println("Heartbeat sent from " + Server.this.serverId);
            }
        }
    }





    //private final ServerLogRepository serverLogRepository;
    private ArrayList<Server> servers = ServerService.servers;
    private int term;
    private final String serverId;
    private final int  timeoutMilisec;
    private LocalDateTime timeoutDateTime;
    private Server leader;
    private LocalDateTime lastHeartbeat;

    //during a voting session a node cannot candidate or timeout
    private boolean inVotingSession;


    public Server(String serverId) {
        this.serverId = serverId;
        Random random = new Random();
        //timeout in miliseconds
        this.timeoutMilisec = random.nextInt(1000,2000);
        this.timeoutDateTime = LocalDateTime.now().plusNanos(this.timeoutMilisec* 1000000L);
        this.term = 0;
        this.inVotingSession=false;
    }

    void sendHeartbeat(){
        for(Server server: servers){
            server.setLastHeartbeat(LocalDateTime.now());
            server.setTimeoutDateTime(server.lastHeartbeat.plusNanos(server.timeoutMilisec* 1000000L));

        }
    }


    //TODO
    void log(String log){
        System.out.println("Server id:" + this.getServerId());
        System.out.println("log:" + log);
    }


    // TODO replace term with number of logs
    Boolean vote(Server server){
        this.term++;
        return this.term <= server.term;
    }

    @Override
    public void run() {
            System.out.println( this.getServerId() + " is running");
            Timer timer = new Timer();
            TimerTask selfelection = new NewTermTask();
            TimerTask heartbeat = new HeartbeatTask();


            //check for the heartbeat and become a candidate if the case
            timer.schedule(selfelection, timeoutMilisec, timeoutMilisec);

            //Send Heartbeats
            timer.schedule(heartbeat,500,500);

    }


}

