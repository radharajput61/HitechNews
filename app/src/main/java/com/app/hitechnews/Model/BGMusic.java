package com.app.hitechnews.Model;

public class BGMusic {
    String name,sdcardpath; int resource;

    public BGMusic(String name, String sdcardpath, int resource) {
        this.name = name;
        this.resource = resource;
        this.sdcardpath = sdcardpath;
    }

    public String getSdcardpath() {
        return sdcardpath;
    }

    public void setSdcardpath(String sdcardpath) {
        this.sdcardpath = sdcardpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
