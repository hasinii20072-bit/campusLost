package com.example.campusLost.entity;

import jakarta.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reporterStudentId;
    private String itemName;
    private String category;
    private String location;
    private String description;
    private String itemType;
    private String contactInfo;
    private String status = "OPEN";
    private boolean giftOffered;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReporterStudentId() { return reporterStudentId; }
    public void setReporterStudentId(String reporterStudentId) { this.reporterStudentId = reporterStudentId; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isGiftOffered() { return giftOffered; }
    public void setGiftOffered(boolean giftOffered) { this.giftOffered = giftOffered; }
}