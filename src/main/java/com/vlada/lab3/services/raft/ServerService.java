package com.vlada.lab3.services.raft;

import com.vlada.lab3.entities.raft.Server;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Data
@Service
public class ServerService  {
    public static ArrayList<Server> servers;
    public static Server leader;

    private ArrayList<Thread> serverThreads;


    public ServerService() {
        Random random = new Random();

        servers = new ArrayList<>();
        servers.add(new Server("Server1"));
        servers.add(new Server("Server2"));
        servers.add(new Server("Server3"));
        servers.add(new Server("Server4"));
        servers.add(new Server("Server5"));
    }


    public void runServers(){
        for (Server server : servers) {
            Thread thread = new Thread(server, server.getServerId());
            thread.start();
        }
    }

    public void killServer(String serverId){

    }

    //TODO






}
