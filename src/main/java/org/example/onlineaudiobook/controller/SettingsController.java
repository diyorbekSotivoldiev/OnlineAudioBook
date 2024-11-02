package org.example.onlineaudiobook.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.requestDto.EditUserDTO;
import org.example.onlineaudiobook.service.SettingService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api1/edit")
public class SettingsController {
    private final SettingService settingService;

    @PutMapping("{id}")
    public HttpEntity<?> editUser(@PathVariable @NotNull Long id, @RequestBody @NotNull EditUserDTO editUserDto) {
        return ResponseEntity.ok(settingService.editUser(id, editUserDto));
    }
}
