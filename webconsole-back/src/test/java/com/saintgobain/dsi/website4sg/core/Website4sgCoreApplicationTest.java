package com.saintgobain.dsi.website4sg.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saintgobain.dsi.website4sg.core.config.SaintGobainProperties;
import com.saintgobain.dsi.website4sg.core.security.Admin;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        Admin.class })
@EnableConfigurationProperties({
        SaintGobainProperties.class })
public class Website4sgCoreApplicationTest {

    @Test
    public void contextLoads() {
    }

}
