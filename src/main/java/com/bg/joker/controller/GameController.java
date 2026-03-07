package com.bg.joker.controller;

import com.bg.joker.enums.GameType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @GetMapping
    public ResponseEntity<List<GameType>> getAllGameTypes() {
        return ResponseEntity.ok(Arrays.asList(GameType.values()));
    }
}
