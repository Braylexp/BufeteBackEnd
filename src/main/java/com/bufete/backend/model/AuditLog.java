package com.bufete.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "audit_log", indexes = {
    @Index(name = "idx_audit_table_record", columnList = "table_name, record_id"),
    @Index(name = "idx_audit_changed_by", columnList = "changed_by"),
    @Index(name = "idx_audit_changed_at", columnList = "changed_at"),
    @Index(name = "idx_audit_modulo", columnList = "modulo")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;
    
    @Column(name = "record_id", nullable = false, length = 100)
    private String recordId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private AuditAction action;
    
    @Type(JsonType.class)
    @Column(name = "old_values", columnDefinition = "jsonb")
    private Map<String, Object> oldValues;
    
    @Type(JsonType.class)
    @Column(name = "new_values", columnDefinition = "jsonb")
    private Map<String, Object> newValues;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", nullable = false)
    private Usuario changedBy;
    
    @Column(name = "changed_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant changedAt;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "modulo", length = 20)
    private String modulo;
    
    public enum AuditAction {
        INSERT, UPDATE, DELETE
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public Map<String, Object> getOldValues() {
        return oldValues;
    }

    public void setOldValues(Map<String, Object> oldValues) {
        this.oldValues = oldValues;
    }

    public Map<String, Object> getNewValues() {
        return newValues;
    }

    public void setNewValues(Map<String, Object> newValues) {
        this.newValues = newValues;
    }

    public Usuario getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Usuario changedBy) {
        this.changedBy = changedBy;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    
}