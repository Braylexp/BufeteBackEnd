package com.bufete.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bufete.backend.model.FileVersion;

@Repository
public interface FileVersionRepository extends JpaRepository<FileVersion, UUID> {
    
    @Query("SELECT fv FROM FileVersion fv WHERE fv.node.id = :nodeId ORDER BY fv.versionNum DESC")
    List<FileVersion> findByNodeIdOrderByVersionNumDesc(@Param("nodeId") UUID nodeId);
    
    @Query("SELECT fv FROM FileVersion fv WHERE fv.node.id = :nodeId AND fv.isCurrent = true")
    Optional<FileVersion> findCurrentVersionByNodeId(@Param("nodeId") UUID nodeId);
    
    @Query("SELECT MAX(fv.versionNum) FROM FileVersion fv WHERE fv.node.id = :nodeId")
    Integer getLastVersionNumber(@Param("nodeId") UUID nodeId);
}