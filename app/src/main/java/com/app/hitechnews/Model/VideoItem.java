package com.app.hitechnews.Model;

public class VideoItem {
    private String MyLike,postId,title,description,location,postDate,postRelTo,totalComment,totalLike,totalView,videoLink,thumbnail,followerId,postedByName,postedByImage,isFollowed;

    public String getMyLike() {
        return MyLike;
    }

    public void setMyLike(String myLike) {
        MyLike = myLike;
    }

    public VideoItem(String postId, String title, String description, String location, String postDate, String postRelTo, String totalComment, String totalLike, String totalView, String videoLink, String thumbnail, String followerId, String postedByName, String postedByImage, String isFollowed, String MyLike) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.postDate = postDate;
        this.postRelTo = postRelTo;
        this.totalComment = totalComment;
        this.totalLike = totalLike;
        this.totalView = totalView;
        this.videoLink = videoLink;
        this.thumbnail = thumbnail;
        this.followerId = followerId;
        this.postedByName = postedByName;
        this.postedByImage = postedByImage;
        this.isFollowed = isFollowed;
        this.MyLike = MyLike;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostRelTo() {
        return postRelTo;
    }

    public void setPostRelTo(String postRelTo) {
        this.postRelTo = postRelTo;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getTotalView() {
        return totalView;
    }

    public void setTotalView(String totalView) {
        this.totalView = totalView;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public String getPostedByImage() {
        return postedByImage;
    }

    public void setPostedByImage(String postedByImage) {
        this.postedByImage = postedByImage;
    }

    public String getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(String isFollowed) {
        this.isFollowed = isFollowed;
    }
}
