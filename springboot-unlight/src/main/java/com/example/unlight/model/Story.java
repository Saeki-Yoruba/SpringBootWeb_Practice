package com.example.unlight.model;

import java.util.List;

public class Story {
    private Long id;           // 故事ID
    private String title;      // 故事標題
    private String content;    // 故事內容
    private int chapter;       // 章節
    private Long characterId;  // 關聯角色ID
    private List<String> images; // 圖片URL列表

    // 無參構造函數
    public Story() {
    }

    // 全參構造函數（不包含 images）
    public Story(Long id, String title, String content, int chapter, Long characterId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.chapter = chapter;
        this.characterId = characterId;
    }

    // 全參構造函數（包含 images）
    public Story(Long id, String title, String content, int chapter, Long characterId, List<String> images) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.chapter = chapter;
        this.characterId = characterId;
        this.images = images;
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
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
        return "Story{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", chapter=" + chapter +
                ", characterId=" + characterId +
                ", images=" + images +
                '}';
    }
}
