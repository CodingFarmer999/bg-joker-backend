package com.bg.joker.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

// 加上 JsonFormat，讓 API 回傳時變成一個包含代碼與名稱的 JSON 物件，而不是單純的字串
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GameType {
    BOARD_GAME("BOARD_GAME", "桌遊", "#3b82f6"), // Blue
    MTG("MTG", "魔法風雲會", "#ef4444"), // Red
    POKEMON("POKEMON", "寶可夢 PTCG", "#10b981"), // Emerald
    YUGIOH("YUGIOH", "遊戲王", "#f59e0b"), // Amber
    FAB("FAB", "血肉之戰 (Flesh and Blood)", "#8b5cf6"), // Violet
    WS("WS", "Weiß Schwarz (黑白雙翼)", "#ec4899"); // Pink

    private final String code;
    private final String displayName;
    private final String color;

    GameType(String code, String displayName, String color) {
        this.code = code;
        this.displayName = displayName;
        this.color = color;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }
}
