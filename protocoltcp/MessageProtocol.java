package com.xizi.protocoltcp;

//协议包
public class MessageProtocol {
    private  int len;  //长度
    private byte[] content; //数据

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
