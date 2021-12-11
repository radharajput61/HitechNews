package com.app.hitechnews.Model;

public class CatModel {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String id="",name="";

    public CatModel(String id, String Name) {
        id = id;
        Name = Name;

    }


}
