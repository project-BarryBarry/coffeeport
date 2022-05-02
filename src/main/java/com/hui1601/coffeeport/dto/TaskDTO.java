package com.hui1601.coffeeport.dto;

import com.hui1601.coffeeport.type.CommandType;
import com.hui1601.coffeeport.type.ContextType;
import com.hui1601.coffeeport.type.LanguageType;

public class TaskDTO {
    private String callback;
    private TaskData data;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public TaskData getData() {
        return data;
    }

    public void setData(TaskData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "callback='" + callback + '\'' +
                ", data=" + data +
                '}';
    }
}
class Data{
    private CommandType cmd;

    public CommandType getCmd() {
        return cmd;
    }

    public void setCmd(CommandType cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "Data{" +
                "cmd=" + cmd +
                '}';
    }
}
class TaskData{
    private CommandType cmd;
    private Data data;

    private int sid;

    public CommandType getCmd() {
        return cmd;
    }

    public void setCmd(CommandType cmd) {
        this.cmd = cmd;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "TaskData{" +
                "cmd=" + cmd +
                ", data=" + data +
                '}';
    }
}
class Configure{
    private String addinfourl;
    private String axinfourl;
    private String browser;
    private String clientinfosendurl;
    private ContextType context;
    private String domain;
    private boolean forceinstall;
    private String installlogsendurl;
    private LanguageType language;
    private String logourl;
    private String msgurl;
    private String selectobject;
    private boolean sendvpinfo;
    private String skin;
    private String type; // ???
    private String webinfourl;

    public ContextType getContext() {
        return context;
    }

    public void setContext(ContextType context) {
        this.context = context;
    }

    public LanguageType getLanguage() {
        return language;
    }

    public void setLanguage(LanguageType language) {
        this.language = language;
    }

    public String getAddinfourl() {
        return addinfourl;
    }

    public void setAddinfourl(String addinfourl) {
        this.addinfourl = addinfourl;
    }

    public String getAxinfourl() {
        return axinfourl;
    }

    public void setAxinfourl(String axinfourl) {
        this.axinfourl = axinfourl;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getClientinfosendurl() {
        return clientinfosendurl;
    }

    public void setClientinfosendurl(String clientinfosendurl) {
        this.clientinfosendurl = clientinfosendurl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getInstalllogsendurl() {
        return installlogsendurl;
    }

    public void setInstalllogsendurl(String installlogsendurl) {
        this.installlogsendurl = installlogsendurl;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getMsgurl() {
        return msgurl;
    }

    public void setMsgurl(String msgurl) {
        this.msgurl = msgurl;
    }

    public String getSelectobject() {
        return selectobject;
    }

    public void setSelectobject(String selectobject) {
        this.selectobject = selectobject;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebinfourl() {
        return webinfourl;
    }

    public void setWebinfourl(String webinfourl) {
        this.webinfourl = webinfourl;
    }

    public void setForceinstall(boolean forceinstall) {
        this.forceinstall = forceinstall;
    }

    public boolean isSendvpinfo() {
        return sendvpinfo;
    }

    public void setSendvpinfo(boolean sendvpinfo) {
        this.sendvpinfo = sendvpinfo;
    }

    @Override
    public String toString() {
        return "Configure{" +
                "addinfourl='" + addinfourl + '\'' +
                ", axinfourl='" + axinfourl + '\'' +
                ", browser='" + browser + '\'' +
                ", clientinfosendurl='" + clientinfosendurl + '\'' +
                ", context=" + context +
                ", domain='" + domain + '\'' +
                ", forceinstall=" + forceinstall +
                ", installlogsendurl='" + installlogsendurl + '\'' +
                ", language=" + language +
                ", logourl='" + logourl + '\'' +
                ", msgurl='" + msgurl + '\'' +
                ", selectobject='" + selectobject + '\'' +
                ", sendvpinfo=" + sendvpinfo +
                ", skin='" + skin + '\'' +
                ", type='" + type + '\'' +
                ", webinfourl='" + webinfourl + '\'' +
                '}';
    }
}