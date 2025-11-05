package com.example.upstdc.model;

import jakarta.persistence.*;

@Entity
@Table(name="documents")
public class Document extends BaseEntity {
    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(nullable = false)
    private String storagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Project project;

    public String getFilename(){ return filename; }
    public void setFilename(String filename){ this.filename = filename; }
    public String getContentType(){ return contentType; }
    public void setContentType(String contentType){ this.contentType = contentType; }
    public long getSizeBytes(){ return sizeBytes; }
    public void setSizeBytes(long sizeBytes){ this.sizeBytes = sizeBytes; }
    public String getStoragePath(){ return storagePath; }
    public void setStoragePath(String storagePath){ this.storagePath = storagePath; }
    public Project getProject(){ return project; }
    public void setProject(Project project){ this.project = project; }
}
