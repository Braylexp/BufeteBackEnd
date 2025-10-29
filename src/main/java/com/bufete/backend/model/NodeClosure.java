package com.bufete.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "node_closure", indexes = {
    @Index(name = "idx_closure_ancestor", columnList = "ancestor_id, depth"),
    @Index(name = "idx_closure_descendant", columnList = "descendant_id, depth")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeClosure {
    @EmbeddedId
    private NodeClosureId id;
    
    @Column(name = "depth", nullable = false)
    private Integer depth;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ancestorId")
    @JoinColumn(name = "ancestor_id")
    private Node ancestor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("descendantId")
    @JoinColumn(name = "descendant_id")
    private Node descendant;

    public NodeClosureId getId() {
        return id;
    }

    public void setId(NodeClosureId id) {
        this.id = id;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Node getAncestor() {
        return ancestor;
    }

    public void setAncestor(Node ancestor) {
        this.ancestor = ancestor;
    }

    public Node getDescendant() {
        return descendant;
    }

    public void setDescendant(Node descendant) {
        this.descendant = descendant;
    }

    
}