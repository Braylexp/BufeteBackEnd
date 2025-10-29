package com.bufete.backend.Dtos.folder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.bufete.backend.model.Node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeDTO {
    private UUID id;
    private Long expedienteId;
    private String expedienteNombre;
    private Long contableId;
    private UUID parentId;
    private String parentName;
    private Node.NodeType type;
    private String name;
    private String description;
    private Node.Modulo modulo;
    private String createdByNombre;
    private Instant createdAt;
    private Instant updatedAt;
    private Long sizeBytes;
    private Integer itemCount;
    private Instant lastAccessed;
    
    // Para archivos
    private UUID currentVersionId;
    private String mimeType;
    private Integer versionNum;
    private String originalName;
    
    // Para carpetas
    private List<NodeDTO> children;
    private String path;
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public Long getExpedienteId() {
        return expedienteId;
    }
    public void setExpedienteId(Long expedienteId) {
        this.expedienteId = expedienteId;
    }
    public String getExpedienteNombre() {
        return expedienteNombre;
    }
    public void setExpedienteNombre(String expedienteNombre) {
        this.expedienteNombre = expedienteNombre;
    }
    public Long getContableId() {
        return contableId;
    }
    public void setContableId(Long contableId) {
        this.contableId = contableId;
    }
    public UUID getParentId() {
        return parentId;
    }
    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public Node.NodeType getType() {
        return type;
    }
    public void setType(Node.NodeType type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Node.Modulo getModulo() {
        return modulo;
    }
    public void setModulo(Node.Modulo modulo) {
        this.modulo = modulo;
    }
    public String getCreatedByNombre() {
        return createdByNombre;
    }
    public void setCreatedByNombre(String createdByNombre) {
        this.createdByNombre = createdByNombre;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Long getSizeBytes() {
        return sizeBytes;
    }
    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }
    public Integer getItemCount() {
        return itemCount;
    }
    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }
    public Instant getLastAccessed() {
        return lastAccessed;
    }
    public void setLastAccessed(Instant lastAccessed) {
        this.lastAccessed = lastAccessed;
    }
    public UUID getCurrentVersionId() {
        return currentVersionId;
    }
    public void setCurrentVersionId(UUID currentVersionId) {
        this.currentVersionId = currentVersionId;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public Integer getVersionNum() {
        return versionNum;
    }
    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }
    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
    public List<NodeDTO> getChildren() {
        return children;
    }
    public void setChildren(List<NodeDTO> children) {
        this.children = children;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    
}
