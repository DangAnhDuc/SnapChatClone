package com.example.admin.snapchatclone.RecyclerViewStory;

public class StoryObject {
    private String email;
    private String uid;
    private String chatorstory;

    public StoryObject(String email, String uid,String chatorStory){
        this.email=email;
        this.uid=uid;
        this.chatorstory=chatorStory;
    }

    public String getChatorstory() {
        return chatorstory;
    }

    public void setChatorstory(String chatorstory) {
        this.chatorstory = chatorstory;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object obj){
        boolean same = false;
        if(obj != null && obj instanceof StoryObject){
            same = this.uid == ((StoryObject) obj ).uid;
        }
        return same;
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + (this.uid == null ? 0 : this.uid.hashCode());
        return result;
    }
}
