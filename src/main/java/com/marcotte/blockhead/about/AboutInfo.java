package com.marcotte.blockhead.about;


public class AboutInfo {
    private String applicationName;
    private String version;

    public AboutInfo()
    {}
    public AboutInfo(String applicationName, String version)
    {
        this.applicationName = applicationName;
        this.version = version;
    }

    public String getApplicationName()
    {
        return applicationName;
    }

    public AboutInfo setApplicationName(String applicationName)
    {
        this.applicationName = applicationName;
        return this;
    }

    public String getVersion()
    {
        return version;
    }

    public AboutInfo setVersion(String version)
    {
        this.version = version;
        return this;
    }
}
