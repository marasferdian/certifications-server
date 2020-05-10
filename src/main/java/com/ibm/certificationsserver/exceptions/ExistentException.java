package com.ibm.certificationsserver.exceptions;

public class ExistentException extends Throwable{
    private String message;
    public ExistentException(){

    }

    public void setMessage(String msg){
        message=msg;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
