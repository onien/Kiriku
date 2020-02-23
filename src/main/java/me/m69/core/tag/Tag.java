package me.m69.core.tag;

public class Tag {

    private String name;
    private String display;

    public Tag(String name, String display) {
        this.name = name;
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
