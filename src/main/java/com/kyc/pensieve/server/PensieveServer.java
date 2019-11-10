package com.kyc.pensieve.server;

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
        new PensieveServer().run("server");
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(new BlindResource());
        environment.jersey().register(new PenultimaResource());
    }
}
