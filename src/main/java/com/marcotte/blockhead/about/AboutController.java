package com.marcotte.blockhead.about;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "About Rest API", tags = "About")
@RestController
@RequestMapping("/about")
public class AboutController {

    private static final Logger log = LoggerFactory.getLogger(AboutController.class);

    public static final String applicationName = "BlockHead";
    public static final String version = "2019.12.15";
    @GetMapping("")
    public AboutInfo aboutInfo()
    {
        AboutInfo appinfo = new AboutInfo().setApplicationName(applicationName).setVersion(version);
        return appinfo;
    }

}

