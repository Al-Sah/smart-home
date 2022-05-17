package org.smarthome.module;


public class ListenerProvider {

    private String[] topics;
    private String id;
    private String group;


    public ListenerProvider(String[] topics, String id, String group) {
        this.topics = topics;
        this.id = id;
        this.group = group;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


}