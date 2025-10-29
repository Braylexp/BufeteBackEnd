package com.bufete.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "node_permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "node_id", "usuario_id" })
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false, length = 20)
    private PermissionType permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "granted_by", nullable = false)
    private Usuario grantedBy;

    @Column(name = "granted_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant grantedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    public enum PermissionType {
        READ, write, admin
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }

    public Usuario getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(Usuario grantedBy) {
        this.grantedBy = grantedBy;
    }

    public Instant getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(Instant grantedAt) {
        this.grantedAt = grantedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    
}