package com.example.codeondroid;

public class CustomLinknode
{
    String content;
    int cursor;
    CustomLinknode prev;
    CustomLinknode next;
    public CustomLinknode(String cont,int curs, CustomLinknode pre)
    {
        content=cont;
        cursor=curs;
        prev=pre;
        next=null;
    }
    public CustomLinknode(CustomLinknode source)
    {
        content=source.content;
        cursor = source.cursor;
        prev=source.prev;
        next=source.next;
    }
    public void setNext(String cont,int curs)
    {
        CustomLinknode temp = new CustomLinknode(cont,curs,this);
        next = temp;
    }
    public CustomLinknode getNext()
    {
        return next;
    }
    public CustomLinknode getPrev()
    {
        return prev;
    }
    public String getcontent()
    {
        return content;
    }
    public int getcursor()
    {
        return cursor;
    }
}
