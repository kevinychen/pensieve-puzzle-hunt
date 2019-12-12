package com.kyc.pensieve.server;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import com.kyc.pensieve.server.blind.BlindResource;
import com.kyc.pensieve.server.penultima.PenultimaResource;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PensieveServer extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        System.setProperty("dw.server.applicationConnectors[0].port", "8090");
        System.setProperty("dw.server.adminConnectors[0].port", "8091");
        new PensieveServer().run("server");
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/files", "/files", "index.html", "files"));
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.servlets().addFilter("auth", new AuthFilter()).addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(new GuessResource());
        environment.jersey().register(new BlindResource());
        environment.jersey().register(new PenultimaResource());
    }
}
