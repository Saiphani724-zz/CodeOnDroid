package com.example.codeondroid;

public class Snipetcontaier {
    private String title;
    private String content;
    public Snipetcontaier(String snipname,String code)
    {
        title=snipname;
        content=code;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
}
