package com.example.sportPlanner.controllers;

import com.example.sportPlanner.dtos.UserDto;
import com.example.sportPlanner.entities.User;
import com.example.sportPlanner.facade.UserFacade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "user/")
public class UserController {
    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping(value="/addUser/{username}")
    public ResponseEntity<?> addUser(@PathVariable final String username){
        User user = userFacade.addUser(username);
        return ResponseEntity.ok("User added successfully");
    }
    @PostMapping(value="/setProfile")
    public ResponseEntity<?> setProfile(@RequestBody final UserDto userDto){
        return ResponseEntity.ok(userFacade.setProfile(userDto));
    }
    @PostMapping("/uploadPhoto")
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "File is empty"));
        }

        // Check the MIME type
        String mimeType = file.getContentType();
        List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp");
        if (!allowedMimeTypes.contains(mimeType)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(Map.of("error", "Only image files are allowed"));
        }

        try {

            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get("uploads/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String photoUrl = filePath.toAbsolutePath().toString();
            return ResponseEntity.ok(Map.of("photo", photoUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to upload file"));
        }
    }
    @GetMapping(value="/getProfile/{username}")
    public ResponseEntity<?> getProfile(@PathVariable final String username){
        return ResponseEntity.ok(userFacade.getProfile(username));
    }

}
