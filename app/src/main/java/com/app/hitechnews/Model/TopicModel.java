package com.app.hitechnews.Model;

public class TopicModel {
    private String desc,topicId,topicName,topicType,userId;

    public TopicModel(String desc, String topicId, String topicName, String topicType, String userId) {
        this.desc = desc;
        this.topicId = topicId;
        this.topicName = topicName;
        this.topicType = topicType;
        this.userId = userId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
