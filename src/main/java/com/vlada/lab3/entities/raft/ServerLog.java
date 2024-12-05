package com.vlada.lab3.entities;

import jakarta.persistence.*;

@Entity
public class ServerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    private String serverId;
    private String action;
    private String timestamp;


}
