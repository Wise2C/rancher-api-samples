package com.wise2c.samples;

import com.wise2c.samples.entity.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.wise2c.samples.RancherConfig.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RancherClientTest {


    private RancherClient rancherClient;

    @Before
    public void setUp() throws Exception {
        rancherClient = new RancherClient(ENDPOINT, ACCESSKEY, SECRET_KEY);
    }

    @Test
    public void should_get_all_environment_instance() throws IOException {
        // when
        Optional<Environments> environments = rancherClient.getEnvironments();
        // then
        assertThat(environments.isPresent(), is(true));
        System.out.println("current environments: " + environments.get().getData());
    }

    @Test
    public void should_get_all_host_instance() throws IOException {

        // when
        Optional<Hosts> hosts = rancherClient.getHosts();

        // then
        assertThat(hosts.isPresent(), is(true));
        System.out.println(hosts.get());

    }

    @Test
    public void should_get_all_stack_instance() throws IOException {

        Optional<Stacks> stacks = rancherClient.getStacks();
        assertThat(stacks.isPresent(), is(true));
        System.out.println(stacks.get().getData());

    }


    @Test
    public void should_get_stack_instance() throws IOException {

        // given
        Optional<Stacks> stacks = rancherClient.getStacks();
        assertThat(stacks.isPresent(), is(true));
        assertThat(stacks.get().getData().size() > 0, is(true));
        String stackId = stacks.get().getData().stream().findAny().get().getId();

        // when
        Optional<Stack> stack = rancherClient.getStack(stackId);

        // then
        assertThat(stack.isPresent(), is(true));
        System.out.println(stack.get());
    }

    @Test
    public void should_get_all_service_instance_in_rancher() throws IOException {
        // when
        Optional<Services> services = rancherClient.getServices();

        // then
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));
        System.out.println(services.get().getData());
    }

    @Test
    public void should_get_all_service_instance_in_stack() throws IOException {

        // given
        Optional<Stacks> stacks = rancherClient.getStacks();
        assertThat(stacks.isPresent(), is(true));
        assertThat(stacks.get().getData().size() > 0, is(true));

        // when
        Optional<Services> services = rancherClient.getServices(stacks.get().getData().stream().findAny().get().getId());

        // then
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));
        System.out.println(services.get().getData());
    }

    @Test
    public void should_get_service_detail() throws IOException {

        // given
        Optional<Stacks> stacks = rancherClient.getStacks();
        assertThat(stacks.isPresent(), is(true));
        assertThat(stacks.get().getData().size() > 0, is(true));

        Optional<Services> services = rancherClient.getServices(stacks.get().getData().stream().findAny().get().getId());
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));
        // when

        String serviceId = services.get().getData().stream().findAny().get().getId();
        Optional<Service> service = rancherClient.getService(serviceId);

        // then
        assertThat(service.isPresent(), is(true));
        System.out.println(service.get());

    }

    @Test
    public void should_get_container_instances_in_service() throws IOException {

        // given
        Optional<Services> services = rancherClient.getServices();
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));
        System.out.println(services.get().getData());

        String serviceId = services.get().getData().stream().findAny().get().getId();

        // when
        Optional<Instances> instances = rancherClient.getContainerInstances(serviceId);

        assertThat(instances.isPresent(), is(true));
        assertThat(instances.get().getData().size() > 0, is(true));
        System.out.println(instances.get().getData());

    }

    @Test
    public void should_get_container_instance() throws IOException {

        // given
        Optional<Services> services = rancherClient.getServices();
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));

        Optional<Instances> instances = rancherClient.getContainerInstances(services.get().getData().stream().findAny().get().getId());

        assertThat(instances.isPresent(), is(true));
        assertThat(instances.get().getData().size() > 0, is(true));
        System.out.println(instances.get().getData());

        // when
        String containerId = instances.get().getData().stream().findAny().get().getId();
        Optional<Instance> instance = rancherClient.getContainerInstance(containerId);

        // then
        assertThat(instance.isPresent(), is(true));
        System.out.println(instance.get());

    }

    @Test
    public void should_create_rancher_stack_in_environment() throws IOException {

        // given
        String expectedName = "stack-" + UUID.randomUUID().toString();

        Stack stack = new Stack();
        stack.setName(expectedName);

        Environment target = getEnvironment();

        // when

        Optional<Stack> newStack = rancherClient.createStack(stack, target.getId());

        assertThat(newStack.isPresent(), is(true));
        assertThat(newStack.get().getName(), is(expectedName));

        // after
        rancherClient.deleteStack(newStack.get().getId(), target.getId());

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

        Environment target = getEnvironment();

        // when
        Stack stack = new Stack();
        stack.setName(expectedStackName);

        // then

        Optional<Stack> newStack = rancherClient.createStack(stack, target.getId());
        assertThat(newStack.isPresent(), is(true));

        Optional<Service> newService = rancherClient.createService(service, target.getId(), newStack.get().getId());

        assertThat(newService.isPresent(), is(true));

        // teardown
        rancherClient.deleteStack(newStack.get().getId(), target.getId());

    }

    private Environment getEnvironment() throws IOException {
        Optional<Environments> environments = rancherClient.getEnvironments();
        assertThat(environments.isPresent(), is(true));
        Optional<Environment> environment = environments.get().getData().stream().findAny();
        assertThat(environment.isPresent(), is(true));
        return environment.get();
    }

}
