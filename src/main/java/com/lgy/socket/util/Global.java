package com.lgy.socket.util;

public class Global {

    private ThreadPoolUtils threadPool;

    /**
     * 心跳间隔时间,单位秒
     */
    public final static Integer heartBeatInterval = 10;

    /**
     * 是否发送心跳: 0，服务器和客户端都发生心跳；1，客户端发送心跳； 2，都不发送心跳
     */
    public  final static Integer isSendHeartBeat = 0;
    /**
     * 最大重连次数
     */
    public Integer maxConnectTimes = Integer.MAX_VALUE;
    /**
     * 当前重连次数
     */
    public Integer reconnectNum = 0;
    /**
     * 是否重连
     */
    public Boolean isNeedReconnect = true;
    /**
     * 重连间隔时间
     */
    public Integer reconnectIntervalTime = 5000;

    private Global(){
        threadPool = new ThreadPoolUtils(ThreadPoolUtils.Type.FixedThread,4);
    }
    private static class GlobalInner{
        private static final Global instance = new Global();
    }

    public static Global getInstance(){
        return GlobalInner.instance;
    }

    public ThreadPoolUtils getThreadPool() {
        return threadPool;
    }
}
