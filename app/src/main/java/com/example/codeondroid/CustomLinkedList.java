package com.example.codeondroid;

public class CustomLinkedList {
    CustomLinknode head,tail;
    int count,max_count;
    public CustomLinkedList(int max_capacity)
    {
        head=null;
        tail=null;
        count=0;
        max_count=max_capacity;
    }
    public CustomLinkedList()
    {
        head=null;
        tail=null;
        count=0;
        max_count=10;
    }
    public void push(String content,int cursor)
    {
        if(head==null || tail==null)
        {
            CustomLinknode temp = new CustomLinknode(content,cursor,null);
            //head = new CustomLinknode(temp);
            tail = new CustomLinknode(temp);
            head=tail;
            count++;
        }
        else
        {
            CustomLinknode temp = new CustomLinknode(content,cursor,null);
            tail.setNext(content,cursor);
            count++;
            tail = tail.getNext();
        }
        if(count==max_count+ 1)
        {
            head = head.getNext();
            head.prev=null;
            count--;
        }
    }
    public CustomLinknode pop()
    {
        if(head==null||tail==null)
        {
            return null;
        }
        else
        {
            CustomLinknode temp = tail.getPrev();
            CustomLinknode temp1=tail;
            if(temp!=null)
            temp.next=null;
            count--;
            tail=temp;
            if(temp==null)
                head=null;
            return temp1;
        }
    }
    public void clearContents()
    {
        while (count!=0)
        {
            pop();
        }
    }
}
