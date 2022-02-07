package com.messanger.engine.uc.message;

/**
 * 
 * @author skoh
 * @deprecated
 */
public class Header {

    volatile char commandId;
    
    volatile char messageId;
    
    volatile short version;
    
    volatile int length;

    public char getCommandId() {
        return commandId;
    }

    public void setCommandId(char commandId) {
        this.commandId = commandId;
    }

    public char getMessageId() {
        return messageId;
    }

    public void setMessageId(char messageId) {
        this.messageId = messageId;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
