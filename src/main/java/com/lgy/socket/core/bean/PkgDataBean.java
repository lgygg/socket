package com.lgy.socket.core.bean;

public class PkgDataBean<T> {
    //命令字
    private byte cmd;
    //数据长度
    private byte dataLength;
    //数据
    private T data;

     public byte getCmd() {
          return cmd;
     }

     public void setCmd(byte cmd) {
          this.cmd = cmd;
     }

     public byte getDataLength() {
          return dataLength;
     }

     public void setDataLength(byte dataLength) {
          this.dataLength = dataLength;
     }

     public T getData() {
          return data;
     }

     public void setData(T data) {
          this.data = data;
     }
}