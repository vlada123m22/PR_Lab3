package com.vlada.lab3.controllers;

import com.vlada.lab3.services.ServerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/raft")
public class RaftElectionController {

    private final ServerService serverService;

    public RaftElectionController(ServerService serverService) {
        this.serverService = serverService;
    }

    @Async
    @PostMapping ("/")
    public CompletableFuture<String> electLeader(){
        serverService.runServers();
        return CompletableFuture.completedFuture("Leader elected") ;
    }


    @Async
    @GetMapping("/leader")
    public CompletableFuture<String> getLeader(){
        return CompletableFuture.completedFuture("The leader is:" + ServerService.leader.getServerId());
    }
}
