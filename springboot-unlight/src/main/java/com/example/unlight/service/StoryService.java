package com.example.unlight.service;

import com.example.unlight.dao.StoryDAO;
import com.example.unlight.exception.StoryNotFoundException;
import com.example.unlight.model.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoryService {

    @Autowired
    private StoryDAO storyDAO;

    private static final String IMAGE_UPLOAD_DIR = "uploads/images/";

    // 查詢所有故事
    public List<Story> getAllStories() {
        List<Story> stories = storyDAO.findAll();
        // 查詢每個故事的圖片
        for (Story story : stories) {
            List<String> images = storyDAO.findImagesByStoryId(story.getId());
            story.setImages(images);
        }
        return stories;
    }

    // 根據角色查故事
    public List<Story> getStoriesByCharacterId(Long characterId) {
        return storyDAO.findByCharacterId(characterId);
    }

    
    // 根據 ID 查詢故事
    public Story getStoryById(Long id) {
        Story story = storyDAO.findById(id)
                .orElseThrow(() -> new StoryNotFoundException("故事不存在，ID: " + id));
        // 查詢並設置圖片列表
        List<String> images = storyDAO.findImagesByStoryId(id);
        story.setImages(images);
        return story;
    }

    // 新增故事（含圖片）
    public void addStoryWithImages(Story story, List<MultipartFile> images) {
        List<String> imagePaths = saveImages(images); // 保存圖片並獲取路徑列表
        storyDAO.save(story, imagePaths); // 保存故事及圖片路徑
    }

    // 更新故事（含圖片）
    public void updateStoryWithImages(Long id, Story story, List<MultipartFile> images) {
        ensureStoryExists(id);
        story.setId(id);

        List<String> imagePaths = null;
        if (images != null && !images.isEmpty()) {
            imagePaths = saveImages(images); // 保存新圖片並獲取路徑列表
        }

        storyDAO.update(story, imagePaths); // 更新故事及圖片路徑
    }

    // 刪除故事
    public void deleteStory(Long id) {
        ensureStoryExists(id);

        // 刪除故事的圖片記錄
        List<String> images = storyDAO.findImagesByStoryId(id);
        deleteImageFiles(images);

        // 刪除數據庫中故事相關記錄
        storyDAO.deleteStoryImages(id);
        storyDAO.delete(id);
    }

    // 保存圖片並返回路徑列表
    private List<String> saveImages(List<MultipartFile> images) {
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile image : images) {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            File file = new File(IMAGE_UPLOAD_DIR + fileName);
            try {
                // 確保目錄存在
                file.getParentFile().mkdirs();
                image.transferTo(file);
                imagePaths.add("/" + IMAGE_UPLOAD_DIR + fileName); // 儲存相對路徑
            } catch (IOException e) {
                throw new RuntimeException("圖片保存失敗：" + fileName, e);
            }
        }
        return imagePaths;
    }

    // 刪除本地圖片文件
    private void deleteImageFiles(List<String> imagePaths) {
        for (String path : imagePaths) {
            File file = new File(path.replaceFirst("/", "")); // 去掉相對路徑的開頭 "/"
            if (file.exists()) {
                file.delete();
            }
        }
    }

    // 確保故事存在
    private void ensureStoryExists(Long id) {
        if (!storyDAO.findById(id).isPresent()) {
            throw new StoryNotFoundException("故事不存在，ID: " + id);
        }
    }
}
