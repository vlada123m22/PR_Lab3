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
            if (Server.this.timeoutDateTime == null || Server.this.timeoutDateTime.isBefore(LocalDateTime.now())) {

                int upvotes = 0;
                int downvotes = 0;
                for (Server server : servers) {
                    server.setInVotingSession(true);
                }
                //TODO increment by 1 every time a server votes yes
                for (Server server : servers) {
                    if (server.vote(Server.this)) upvotes++;
                    else downvotes++;
                }
                for (Server server : servers) {
                    server.setInVotingSession(false);
                }

                //if the majority voted for this server, make it the leader
                if(upvotes>downvotes){
                    for (Server server : servers) {
                        server.setLeader(Server.this);
                    }
                    ServerService.leader = Server.this.leader;
                }

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
        this.timeoutDateTime = LocalDateTime.now().plusNanos(this.timeoutMilisec* 1000L);
        this.term = 0;
        this.inVotingSession=false;

    }

    void sendHeartbeat(){
        for(Server server: servers){
            server.setLastHeartbeat(LocalDateTime.now());
            server.setTimeoutDateTime(server.lastHeartbeat.plusNanos(server.timeoutMilisec* 1000L));
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

            Timer timer = new Timer();
            TimerTask task = new NewTermTask();

            //check for the heartbeat
            timer.schedule(task, timeoutMilisec, timeoutMilisec);

    }


}

