package com.example.cookbook.util;

public class Comment {
    private Integer userId;

    private Integer cookbookId;

    private String commentInf;

    public String getCommentInf() {
        return commentInf;
    }

    public void setCommentInf(String commentInf) {
        this.commentInf = commentInf;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCookbookId() {
        return cookbookId;
    }

    public void setCookbookId(Integer cookbookId) {
        this.cookbookId = cookbookId;
    }
}
