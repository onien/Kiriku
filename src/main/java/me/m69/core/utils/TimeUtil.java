package me.m69.core.utils;

import java.util.HashMap;

/**
 util by: https://pastebin.com/AA2Us01w
 */

public enum TimeUtil {

    SECOND("seconds", "s", 1),
    MINUTE("minutes", "min", 60),
    HOUR("hours", "h", 60 * 60),
    DAY("days", "d", 60 * 60 * 24),
    MONTH("months", "m", 60 * 60 * 24 * 30);

    private String name;
    private String shortcut;
    private long toSecond;

    private static HashMap<String, TimeUtil> ID_SHORTCUT = new HashMap<>();

    private TimeUtil(String name, String shortcut, long toSecond) {
        this.name = name;
        this.shortcut = shortcut;
        this.toSecond = toSecond;
    }

    static {
        for (TimeUtil units : values()) {
            ID_SHORTCUT.put(units.shortcut, units);
        }
    }

    public static TimeUtil getFromShortcut(String shortcut) {
        return ID_SHORTCUT.get(shortcut);
    }

    public String getName() {
        return name;
    }


    public long getToSecond() {
        return toSecond;
    }
}
