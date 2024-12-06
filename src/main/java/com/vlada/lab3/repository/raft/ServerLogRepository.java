package com.vlada.lab3.repository.raft;


import com.vlada.lab3.entities.raft.ServerLog;
import org.springframework.data.repository.CrudRepository;

public interface ServerLogRepository extends CrudRepository<ServerLog,Long> {

}
