package com.barrybarry.coffeeport.data.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class PluginInstallInfoData {
    @JsonProperty("version")
    private String version;
    @JsonProperty("createDate")
    private String createDate;
    @JsonProperty("allowDomains")
    private String allowDomains;
    @JsonProperty("allowContexts")
    private String allowContexts;
    @JsonProperty("systemID")
    private String systemID;
    @JsonProperty("sslVerify")
    private String sslVerify;
    @Getter
    @Setter
    public static class PluginInfo {
        @JsonProperty("objectName")
        private String objectName;

        @JsonProperty("displayName")
        private String displayName;

        @JsonProperty("objectVersion")
        private String objectVersion;

        @JsonProperty("signVerify")
        private String signVerify;

        @JsonProperty("browserType")
        private String browserType;

        @JsonProperty("objectMIMEType")
        private String objectMIMEType;

        @JsonProperty("browserVersion")
        private String browserVersion;

        @JsonProperty("downloadURL")
        private String downloadUrl;

        @JsonProperty("backupURL")
        private String backupUrl;

        @JsonProperty("systemType")
        private String systemType;

        @JsonProperty("platformMajorOrg")
        private String platformMajorOrg;

        @JsonProperty("platformMajor")
        private String platformMajor;

        @JsonProperty("platformMinor")
        private String platformMinor;

        @JsonProperty("javascriptURL")
        private String javascriptUrl;

        @JsonProperty("objectHash")
        private String objectHash;

        @JsonProperty("uninstallKey")
        private String uninstallKey;

        @JsonProperty("extInfo")
        private String extInfo;

        @JsonProperty("policyInfo")
        private String policyInfo;

        @JsonProperty("description")
        private String description;

        @JacksonXmlProperty(isAttribute = true)
        private String type;
    }
    @JsonProperty("object")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ArrayList<PluginInfo> plugins = new ArrayList<>();
}
