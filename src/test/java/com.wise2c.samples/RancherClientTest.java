package com.wise2c.samples;

import com.wise2c.samples.action.InServiceStrategy;
import com.wise2c.samples.action.ServiceUpgrade;
import com.wise2c.samples.entity.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.wise2c.samples.RancherConfig.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RancherClientTest extends TestBase {


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

    @Test
    public void should_update_service_in_stack() throws Exception {

        // given
        String expectedStackName = "stack-" + UUID.randomUUID().toString();

        Service service = new Service();
        service.setScale(1);
        service.setName("busybox");

        LaunchConfig launchConfig = new LaunchConfig();
        launchConfig.setImageUuid("docker:busybox");
        service.setLaunchConfig(launchConfig);

        Environment target = getEnvironment();

        Stack stack = new Stack();
        stack.setName(expectedStackName);
        Optional<Stack> newStack = rancherClient.createStack(stack, target.getId());
        assertThat(newStack.isPresent(), is(true));
        Optional<Service> newService = rancherClient.createService(service, target.getId(), newStack.get().getId());
        assertThat(newService.isPresent(), is(true));

        waitUntilServiceStateIs(newService.get().getId(), "active");

        // when
        ServiceUpgrade serviceUpgrade = new ServiceUpgrade();
        InServiceStrategy inServiceStrategy = new InServiceStrategy();
        inServiceStrategy.setLaunchConfig(newService.get().getLaunchConfig());
        inServiceStrategy.getLaunchConfig().setImageUuid("docker:busybox:1.26.2-glibc");
        inServiceStrategy.getLaunchConfig().setPorts(Collections.emptyList());

        serviceUpgrade.setInServiceStrategy(inServiceStrategy);

        Optional<Service> upgradedService = rancherClient.upgradeService(target.getId(), newService.get().getId(), serviceUpgrade);
        waitUntilServiceStateIs(newService.get().getId(), "upgraded");

        rancherClient.finishUpgrade(target.getId(), newService.get().getId());
        waitUntilServiceStateIs(newService.get().getId(), "active");

        // then
        assertThat(upgradedService.isPresent(), is(true));
        assertThat(upgradedService.get().getLaunchConfig().getImageUuid(), is("docker:busybox:1.26.2-glibc"));

        // teardown
        rancherClient.deleteStack(newStack.get().getId(), target.getId());

    }

    private void waitUntilServiceStateIs(String serviceId, String targetState) throws Exception {
        int i = 30;
        while (i-- > 0) {
            Optional<Service> checkService = rancherClient.getService(serviceId);
            String state = checkService.get().getState();
            if (state.equals(targetState)) {
                break;
            }
            System.out.println(state);
            Thread.sleep(2000);
        }
        if (i <= 0) {
            throw new Exception("Service[" + serviceId + "] State not invalidate[" + targetState + "], current state is " + rancherClient.getService(serviceId).get().getState());
        }

    }

    private Environment getEnvironment() throws IOException {
        Optional<Environments> environments = rancherClient.getEnvironments();
        assertThat(environments.isPresent(), is(true));
        Optional<Environment> environment = environments.get().getData().stream().findAny();
        assertThat(environment.isPresent(), is(true));
        return environment.get();
    }

}
