package com.bufete.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class NodeClosureId implements Serializable {
    @Column(name = "ancestor_id")
    private UUID ancestorId;
    
    @Column(name = "descendant_id")
    private UUID descendantId;

    public UUID getAncestorId() {
        return ancestorId;
    }

    public void setAncestorId(UUID ancestorId) {
        this.ancestorId = ancestorId;
    }

    public UUID getDescendantId() {
        return descendantId;
    }

    public void setDescendantId(UUID descendantId) {
        this.descendantId = descendantId;
    }

    
}