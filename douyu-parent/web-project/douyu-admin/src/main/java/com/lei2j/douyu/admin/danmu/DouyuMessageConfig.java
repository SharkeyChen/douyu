package com.lei2j.douyu.admin.danmu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.danmu.pojo.DouyuMessage;
import com.lei2j.douyu.danmu.service.MessageType;
import com.lei2j.douyu.util.HttpUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by lei2j on 2018/11/25.
 */
class DouyuMessageConfig {

    private static DouyuMessage keepaliveMessage = new DouyuMessage();
    private static DouyuMessage logoutMessage = new DouyuMessage();
    static {
        keepaliveMessage.add("type", MessageType.KEEPALIVE);
        logoutMessage.add("type", MessageType.LOGOUT);
    }

    public static DouyuMessage getLoginMessage(Integer room, String username, String password){
        DouyuMessage douyuMessage = new DouyuMessage();
        String devId = UUID.randomUUID().toString().replace("-","");
        long rt = System.currentTimeMillis()/1000;
        String vk = DigestUtils.md5Hex(rt + "7oE9nPEG9xXV69phU31FYCLUagKeYtsF" + devId);
        douyuMessage.add("type", MessageType.LOGIN).add("roomid", String.valueOf(room))
                .add("username",username).add("password",password)
                .add("ct","0").add("rt",String.valueOf(rt))
                .add("vk",vk).add("ver","20180413")
                .add("aver","118101901").add("ltkid","").add("biz","").add("stk","").add("dfl","")
                .add("devid",devId);
        return douyuMessage;
    }

    public static DouyuMessage getJoinMessage(Integer room){
        DouyuMessage douyuJoin = new DouyuMessage();
        douyuJoin.add("type",MessageType.JOIN_GROUP).add("rid",String.valueOf(room)).add("gid","-9999");
        return douyuJoin;
    }

    public static DouyuMessage getKeepaliveMessage() {
        return keepaliveMessage;
    }

    /**
     * @return DouyuMessage
     */
    public static DouyuMessage getLogoutMessage() {
        return logoutMessage;
    }

    public static class LoginServer {
        private String ip;

        private int port;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

    }

    /**
     * 获取房间弹幕登录服务器列表
     * @return List
     */
    private static List<LoginServer> getServerConfig(Integer room) {
        String s = HttpUtil.get(DouyuApi.ROOM_SERVER_CONFIG.replace("{room}", String.valueOf(room)), null);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String serverConfig = null;
        try {
            serverConfig = URLDecoder.decode(jsonObject.getJSONObject("room_args").getString("server_config"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<LoginServer> loginServerList = JSONArray.parseArray(serverConfig, LoginServer.class);
        return loginServerList;
    }

    /**
     * 从房间弹幕登录服务器组中随机获取一个登录服务器地址
     * @return DouyuAddress
     */
    public static DouyuAddress getLoginServerAddress(Integer room) {
        List<LoginServer> serverConfig = getServerConfig(room);
        Optional<LoginServer> optional = serverConfig.stream().findAny();
        if (!optional.isPresent()) {
            throw new RuntimeException("No Server Found");
        }
        LoginServer loginServer = optional.get();
        DouyuAddress douyuAddress = new DouyuAddress(loginServer.getIp(), loginServer.getPort());
        return douyuAddress;
    }
}
