package com.brannanhancock.convnet.app_fabmodel;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme("my-theme")
public class AppFabmodelApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(AppFabmodelApplication.class, args);
    }
}
