package com.wise2c.samples;

import com.wise2c.samples.entity.Environment;
import com.wise2c.samples.entity.Environments;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.wise2c.samples.RancherConfig.ACCESSKEY;
import static com.wise2c.samples.RancherConfig.ENDPOINT;
import static com.wise2c.samples.RancherConfig.SECRET_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RancherWebSocketClientTest {

    @Test
    public void should_connection_rancher_with_web_socket() throws URISyntaxException, IOException, InterruptedException {

        RancherClient rancherClient = new RancherClient(ENDPOINT, ACCESSKEY, SECRET_KEY);
        Optional<Environments> environments = rancherClient.getEnvironments();
        assertThat(environments.isPresent(), is(true));

        Optional<Environment> environment = environments.get().getData().stream().findAny();

        assertThat(environment.isPresent(), is(true));

        RancherWebSocketClient c = new RancherWebSocketClient(ENDPOINT, ACCESSKEY, SECRET_KEY);
        c.connection(environment.get());

        Thread.sleep(10000);

    }

}