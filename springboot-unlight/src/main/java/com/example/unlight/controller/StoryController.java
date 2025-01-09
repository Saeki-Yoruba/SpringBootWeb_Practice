package com.example.unlight.controller;

import com.example.unlight.model.Story;
import com.example.unlight.response.ApiResponse;
import com.example.unlight.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    // 查詢所有故事 (對所有用戶開放)
    @GetMapping
    public ApiResponse<List<Story>> getAllStories() {
        List<Story> stories = storyService.getAllStories();
        return ApiResponse.success("查詢成功", stories);
    }

    // 根據角色查故事
    @GetMapping("/character/{characterId}")
    public ApiResponse<List<Story>> getStoriesByCharacterId(@PathVariable Long characterId) {
        List<Story> stories = storyService.getStoriesByCharacterId(characterId);
        return ApiResponse.success("查詢成功", stories);
    }

    // 根據 ID 查詢故事
    @GetMapping("/{id}")
    public ApiResponse<Story> getStoryById(@PathVariable Long id) {
        Story story = storyService.getStoryById(id);
        return ApiResponse.success("查詢成功", story);
    }

    // 新增故事 (支持多張圖片，僅限管理員)
    @PostMapping(consumes = {"multipart/form-data"})
    public ApiResponse<Void> addStory(
    		@RequestPart("story") Story story,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader(value = "Role", defaultValue = "USER") String role) {
        validateAdminRole(role);
        storyService.addStoryWithImages(story, images);
        return ApiResponse.success("新增成功", null);
    }

    // 更新故事 (支持多張圖片，僅限管理員)
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ApiResponse<Void> updateStory(
            @PathVariable Long id,
            @RequestPart("story") Story story,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader(value = "Role", defaultValue = "USER") String role) {
        validateAdminRole(role);
        storyService.updateStoryWithImages(id, story, images);
        return ApiResponse.success("更新成功", null);
    }

    // 刪除故事 (僅限管理員)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStory(
            @PathVariable Long id,
            @RequestHeader(value = "Role", defaultValue = "USER") String role) {
        validateAdminRole(role);
        storyService.deleteStory(id);
        return ApiResponse.success("刪除成功", null);
    }

    // 檢查角色是否為管理員
    private void validateAdminRole(String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new SecurityException("無權限進行此操作");
        }
    }
}
