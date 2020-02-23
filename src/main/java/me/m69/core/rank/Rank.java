package me.m69.core.rank;

import java.util.List;

public class Rank {

    private String name;
    private String prefix;
    private String suffix;
    private String maincolor;
    private int priority;
    private boolean defaultRank;
    private List<String> perms;

    public Rank(String name, String prefix, String suffix, String maincolor, int priority, boolean defaultRank, List<String> perms) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.maincolor = maincolor;
        this.priority = priority;
        this.defaultRank = defaultRank;
        this.perms = perms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getMaincolor() {
        return maincolor;
    }

    public void setMaincolor(String maincolor) {
        this.maincolor = maincolor;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isDefaultRank() {
        return defaultRank;
    }

    public void setDefaultRank(boolean defaultRank) {
        this.defaultRank = defaultRank;
    }

    public List<String> getPerms() {
        return perms;
    }

    public void setPerms(List<String> perms) {
        this.perms = perms;
    }

    /*
    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getMaincolor() {
        return maincolor;
    }

    public List<String> getPerms() {
        return perms;
    }

    public boolean isDefaultRank() {
        return defaultRank;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setMaincolor(String maincolor) {
        this.maincolor = maincolor;
    }

    public void setDefaultRank(boolean defaultRank) {
        this.defaultRank = defaultRank;
    }

    public void setPerms(List<String> perms) {
        this.perms = perms;
    }

     */
}
