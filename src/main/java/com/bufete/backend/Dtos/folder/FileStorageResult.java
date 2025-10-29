package com.bufete.backend.Dtos.folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileStorageResult {
    private String storageKey;
    private String bucketName;
    private String checksum;
    private long size;
    public String getStorageKey() {
        return storageKey;
    }
    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }
    public String getBucketName() {
        return bucketName;
    }
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    public String getChecksum() {
        return checksum;
    }
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }

    
}