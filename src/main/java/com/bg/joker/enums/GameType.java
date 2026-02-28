package com.bg.joker.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

// 加上 JsonFormat，讓 API 回傳時變成一個包含代碼與名稱的 JSON 物件，而不是單純的字串
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GameType {
    BOARD_GAME("BOARD_GAME", "桌遊"),
    MTG("MTG", "魔法風雲會"),
    POKEMON("POKEMON", "寶可夢 PTCG"),
    YUGIOH("YUGIOH", "遊戲王"),
    FAB("FAB", "血肉之戰 (Flesh and Blood)"),
    WS("WS", "Weiß Schwarz (黑白雙翼)");

    private final String code;
    private final String displayName;

    GameType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}
