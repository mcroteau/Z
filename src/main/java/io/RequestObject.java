package io;

public class RequestObject {

    StringBuilder request;

    public RequestObject(){
        this.request = new StringBuilder();
    }

    public void append(String characters){
        this.request.append(characters);
    }

    public void process(){
        System.out.println("z" + this.request.toString());
    }

}
