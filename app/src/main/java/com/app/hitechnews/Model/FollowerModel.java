package com.app.hitechnews.Model;

public class FollowerModel {
    private String About,FollowerId,Name,ProfilePic;

    public FollowerModel(String about, String followerId, String name, String profilePic) {
        About = about;
        FollowerId = followerId;
        Name = name;
        ProfilePic = profilePic;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getFollowerId() {
        return FollowerId;
    }

    public void setFollowerId(String followerId) {
        FollowerId = followerId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }
}
