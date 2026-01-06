package com.cryptic.cache.graphics.widget.impl.teleport;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @Author https://github.com/ophionb
 * @author Ophion
 */
public class Teleport {

    private String name;

    private Optional<Integer> npcId;

    private ZonedDateTime lastUsed;

    private Optional<String> taskDescription;

    public Teleport(String name)  {
        this.setName(name);
    }

    public Teleport(String name, int npcId)  {
        this.setName(name);
        this.setNpcId(Optional.of(npcId));
    }

    public Teleport(String name, String taskDescription)  {
        this.setName(name);
        this.setTaskDescription(Optional.of(taskDescription));
    }

    public Teleport(String name, int npcId, String taskDescription)  {
        this.setName(name);
        this.setTaskDescription(Optional.of(taskDescription));
        this.setNpcId(Optional.of(npcId));
    }

    /**
     * Represents the visible name of the teleport.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<Integer> getNpcId() {
        return npcId;
    }

    public void setNpcId(Optional<Integer> npcId) {
        this.npcId = npcId;
    }

    /**
     * Represents the teleport's last used time-stamp
     */
    public ZonedDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(ZonedDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }

    /**
     * Represents the teleport's taskDescription
     */
    public Optional<String> getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(Optional<String> taskDescription) {
        this.taskDescription = taskDescription;
    }
}
