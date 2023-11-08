package com.barrybarry.coffeeport.data.xml;

import com.barrybarry.coffeeport.type.ContextType;
import com.barrybarry.coffeeport.type.LanguageType;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
@Getter
@Setter
public class InstallationConfigureData implements Serializable {
    @SerializedName("addinfourl")
    private String additionalInfoUrl;
    @SerializedName("axinfourl")
    private String activexInfoUrl;
    @SerializedName("browser")
    private String browser;
    @SerializedName("clientinfosendurl")
    private String clientInfoSendUrl;
    @SerializedName("context")
    private ContextType context;
    @SerializedName("domain")
    private String domain;
    @SerializedName("forceinstall")
    private boolean forceInstall;
    @SerializedName("installlogsendurl")
    private String installLogSendUrl;
    @SerializedName("language")
    private LanguageType language;
    @SerializedName("logourl")
    private String logoUrl;
    @SerializedName("msgurl")
    private String messageUrl;
    @SerializedName("selectobject")
    private String selectObject;
    @SerializedName("sendvpinfo")
    private boolean sendVeraportInfo;
    @SerializedName("skin")
    private String skin;
    @SerializedName("type")
    private String type; // ???
    @SerializedName("webinfourl")
    private String webInfoUrl;
    @SerializedName("axinfo")
    private String activexInfo;
}
