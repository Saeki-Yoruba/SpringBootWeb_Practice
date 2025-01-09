package com.example.unlight.model;

import java.util.List;

public class Character {
    private Long id;                   // 角色ID
    private String name;               // 角色名稱
    private String description;        // 角色描述
    private String mainImage;          // 主圖片
    private List<String> images;       // 角色圖片URL列表（可選）

    // 無參構造函數
    public Character() {
    }

    // 全參構造函數
    public Character(Long id, String name, String description, String mainImage, List<String> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mainImage = mainImage;
        this.images = images;
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }
    
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    // toString 方法
    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mainImage=" + mainImage +
                ", images=" + images +
                '}';
    }
}
