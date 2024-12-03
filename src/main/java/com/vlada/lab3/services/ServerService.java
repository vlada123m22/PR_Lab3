package com.vlada.lab3.services;

import com.vlada.lab3.entities.Server;
import lombok.Data;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Data
@Service
public class ServerService  {
    public static ArrayList<Server> servers;
    public static Server leader;


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
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    //TODO






}
