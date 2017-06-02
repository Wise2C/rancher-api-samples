package com.wise2c.samples;

import com.wise2c.samples.entity.LaunchConfig;
import com.wise2c.samples.entity.Service;
import com.wise2c.samples.entity.Stack;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RancherClientTest {

    public static final String ENDPOINT = "<YOUR ENDPOINT>";
    public static final String ACCESSKEY = "<ACCESS KEY>";
    public static final String SECRET_KEY = "<SECRET_KEY>";
    private RancherClient rancherClient;

    @Before
    public void setUp() throws Exception {
        rancherClient = new RancherClient(ENDPOINT, ACCESSKEY, SECRET_KEY);
    }

    @Test
    public void should_create_rancher_stack() throws IOException {

        // given
        String expectedName = "stack-" + UUID.randomUUID().toString();

        Stack stack = new Stack();
        stack.setName(expectedName);

        // when
        Optional<Stack> newStack = rancherClient.createStack(stack);

        assertThat(newStack.isPresent(), is(true));
        assertThat(newStack.get().getName(), is(expectedName));

        // after
        rancherClient.deleteStack(newStack.get().getId());

    }

    @Test
    public void should_create_service_in_stack() throws IOException {

        // given
        String expectedStackName = "stack-" + UUID.randomUUID().toString();

        Service service = new Service();
        service.setScale(1);
        service.setName("busybox");

        LaunchConfig launchConfig = new LaunchConfig();
        launchConfig.setImageUuid("docker:busybox");
        launchConfig.setPorts(Arrays.asList("1234:1234", "4321:4321"));

        service.setLaunchConfig(launchConfig);

        // when
        Stack stack = new Stack();
        stack.setName(expectedStackName);

        // then
        Optional<Stack> newStack = rancherClient.createStack(stack);
        assertThat(newStack.isPresent(), is(true));

        Optional<Service> newService = rancherClient.createService(service, newStack.get().getId());

        assertThat(newService.isPresent(), is(true));

        // teardown
        rancherClient.deleteStack(newStack.get().getId());

    }

}
