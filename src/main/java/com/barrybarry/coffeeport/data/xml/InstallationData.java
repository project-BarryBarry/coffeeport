package com.barrybarry.coffeeport.data.xml;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
@Getter
@Setter
public class InstallationData implements Serializable {
    @SerializedName("backupurl")
    private String backupUrl;

    @SerializedName("browsertype")
    private String browserType = "Mozilla";

    @SerializedName("browserversion")
    private String browserVersion = "-1";

    @SerializedName("description")
    private String description;

    @SerializedName("displayname")
    private String displayName;

    @SerializedName("downloadurl")
    private String downloadUrl;

    @SerializedName("extinfo")
    private String extInfo;

    @SerializedName("forceinstall")
    private String forceInstall = "false";

    @SerializedName("hashcheck")
    private int hashCheck = 1;

    @SerializedName("installstate")
    private boolean installState;

    @SerializedName("objectclsid")
    private String objectClsId;

    @SerializedName("objectname")
    private String objectName;

    @SerializedName("objecttype")
    private int objectType = 1;

    @SerializedName("objectversion")
    private String objectVersion;

    @SerializedName("systemtype")
    private int systemType = 3;

    @SerializedName("updatestate")
    private boolean updateState;

    @SerializedName("version")
    private int version = 3;
}
