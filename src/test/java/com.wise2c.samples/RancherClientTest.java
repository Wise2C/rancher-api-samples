package com.wise2c.samples;

import com.wise2c.samples.action.InServiceStrategy;
import com.wise2c.samples.action.ServiceRestart;
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
        Optional<Environments> environments = rancherClient.environments();
        // then
        assertThat(environments.isPresent(), is(true));
        System.out.println("current environments: " + environments.get().getData());
    }

    @Test
    public void should_get_all_host_instance() throws IOException {

        // when
        Optional<Hosts> hosts = rancherClient.hosts();

        // then
        assertThat(hosts.isPresent(), is(true));
        System.out.println(hosts.get());

    }

    @Test
    public void should_get_host_instance() throws IOException {
        // given
        Optional<Host> host = getHost();
        // when
        Optional<Host> host2 = rancherClient.host(host.get().getId());

        // then
        assertThat(host.get().getId().equals(host2.get().getId()), is(true));

    }

    @Test
    public void should_evacuate_host_instance() throws Exception {
        // given
        Optional<Host> host = getHost();
        // when
        Optional<Host> evacuateHost = rancherClient.evacuateHost(host.get().getId());

        // then
        assertThat(evacuateHost.isPresent(), is(true));
        waitUntilHostStateIs(host.get().getId(), "inactive");

        Optional<Host> activateHost = rancherClient.activateHost(host.get().getId());
        assertThat(activateHost.isPresent(), is(true));
        waitUntilHostStateIs(host.get().getId(), "active");
    }


    @Test
    public void should_deactivate_host_instance() throws Exception {
        // given
        Optional<Host> host = getHost();
        // when
        Optional<Host> deactivateHost = rancherClient.deactivateHost(host.get().getId());
        // then
        assertThat(deactivateHost.isPresent(), is(true));
        waitUntilHostStateIs(host.get().getId(), "inactive");

        Optional<Host> activateHost = rancherClient.activateHost(host.get().getId());
        assertThat(activateHost.isPresent(), is(true));
        waitUntilHostStateIs(host.get().getId(), "active");
    }

    @Test
    public void should_update_host_instance_info() throws IOException {

        // given
        Optional<Environments> environments = rancherClient.environments();
        assertThat(environments.isPresent(), is(true));
        assertThat(environments.get().getData().size() > 0, is(true));

        Optional<Environment> targetEnvironment = environments.get().getData().stream().findFirst();
        assertThat(targetEnvironment.isPresent(), is(true));

        Optional<Hosts> hosts = rancherClient.hosts(targetEnvironment.get().getId());
        assertThat(hosts.isPresent(), is(true));
        assertThat(hosts.get().getData().size() > 0, is(true));

        Optional<Host> host = hosts.get().getData().stream().findAny();
        assertThat(host.isPresent(), is(true));

        Host currentHost = host.get();
        currentHost.getLabels().put("label1", "value1");

        // when
        Optional<Host> update = rancherClient.updateHost(targetEnvironment.get().getId(), currentHost);

        // then
        assertThat(update.isPresent(), is(true));
        assertThat(update.get().getLabels().containsKey("label1"), is(true));

    }

    @Test
    public void should_get_all_stack_instance() throws IOException {

        Optional<Stacks> stacks = rancherClient.stacks();
        assertThat(stacks.isPresent(), is(true));
        System.out.println(stacks.get().getData());

    }


    @Test
    public void should_get_stack_instance() throws IOException {

        // given
        Optional<Stacks> stacks = rancherClient.stacks();
        assertThat(stacks.isPresent(), is(true));
        assertThat(stacks.get().getData().size() > 0, is(true));
        String stackId = stacks.get().getData().stream().findAny().get().getId();

        // when
        Optional<Stack> stack = rancherClient.stack(stackId);

        // then
        assertThat(stack.isPresent(), is(true));
        System.out.println(stack.get());
    }

    @Test
    public void should_get_all_service_instance_in_rancher() throws IOException {
        // when
        Optional<Services> services = rancherClient.services();

        // then
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));
        System.out.println(services.get().getData());
    }

    @Test
    public void should_get_all_service_instance_in_stack() throws IOException {

        // given
        Optional<Stacks> stacks = rancherClient.stacks();
        assertThat(stacks.isPresent(), is(true));
        assertThat(stacks.get().getData().size() > 0, is(true));

        // when
        Optional<Services> services = rancherClient.services(stacks.get().getData().stream().findAny().get().getId());

        // then
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));
        System.out.println(services.get().getData());
    }

    @Test
    public void should_get_service_detail() throws IOException {

        // given
        Optional<Stacks> stacks = rancherClient.stacks();
        assertThat(stacks.isPresent(), is(true));
        assertThat(stacks.get().getData().size() > 0, is(true));

        Optional<Services> services = rancherClient.services(stacks.get().getData().stream().findAny().get().getId());
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));
        // when

        String serviceId = services.get().getData().stream().findAny().get().getId();
        Optional<Service> service = rancherClient.service(serviceId);

        // then
        assertThat(service.isPresent(), is(true));
        System.out.println(service.get());

    }

    @Test
    public void should_get_container_instances_in_service() throws IOException {

        // given
        Optional<Services> services = rancherClient.services();
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));
        System.out.println(services.get().getData());

        String serviceId = services.get().getData().stream().findAny().get().getId();

        // when
        Optional<Instances> instances = rancherClient.containerInstances(serviceId);

        assertThat(instances.isPresent(), is(true));
        assertThat(instances.get().getData().size() > 0, is(true));
        System.out.println(instances.get().getData());

    }

    @Test
    public void should_get_container_instance() throws IOException {

        // given
        Optional<Services> services = rancherClient.services();
        assertThat(services.isPresent(), is(true));
        assertThat(services.get().getData().size() > 0, is(true));

        Optional<Instances> instances = rancherClient.containerInstances(services.get().getData().stream().findAny().get().getId());

        assertThat(instances.isPresent(), is(true));
        assertThat(instances.get().getData().size() > 0, is(true));
        System.out.println(instances.get().getData());

        // when
        String containerId = instances.get().getData().stream().findAny().get().getId();
        Optional<Instance> instance = rancherClient.containerInstance(containerId);

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

        Environment environment = getEnvironment();

        // when
        Optional<Stack> newStack = rancherClient.createStack(stack, environment.getId());

        assertThat(newStack.isPresent(), is(true));
        assertThat(newStack.get().getName(), is(expectedName));

        // after
        rancherClient.deleteStack(newStack.get().getId(), environment.getId());

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

        Environment environment = getEnvironment();

        // when
        Stack stack = new Stack();
        stack.setName(expectedStackName);

        // then
        Optional<Stack> newStack = rancherClient.createStack(stack, environment.getId());
        assertThat(newStack.isPresent(), is(true));

        Optional<Service> newService = rancherClient.createService(service, environment.getId(), newStack.get().getId());

        assertThat(newService.isPresent(), is(true));

        // teardown
        rancherClient.deleteStack(newStack.get().getId(), environment.getId());

    }

    @Test
    public void should_manage_service_lifecycle() throws Exception {

        // given
        String expectedStackName = "stack-" + UUID.randomUUID().toString();

        Service service = new Service();
        service.setScale(1);
        service.setName("busybox");

        LaunchConfig launchConfig = new LaunchConfig();
        launchConfig.setImageUuid("docker:busybox");
        launchConfig.setPorts(Arrays.asList("1234:1234", "4321:4321"));

        service.setLaunchConfig(launchConfig);

        Environment environment = getEnvironment();
        Stack stack = new Stack();
        stack.setName(expectedStackName);
        Optional<Stack> newStack = rancherClient.createStack(stack, environment.getId());
        assertThat(newStack.isPresent(), is(true));

        Optional<Service> newService = rancherClient.createService(service, environment.getId(), newStack.get().getId());
        assertThat(newService.isPresent(), is(true));

        waitUntilServiceStateIs(newService.get().getId(), "active");

        // when
        rancherClient.deactivateService(environment.getId(), newService.get().getId());
        waitUntilServiceStateIs(newService.get().getId(), "inactive");

        rancherClient.activateService(environment.getId(), newService.get().getId());
        waitUntilServiceStateIs(newService.get().getId(), "active");

        rancherClient.restartService(environment.getId(), newService.get().getId(), new ServiceRestart());
        waitUntilServiceStateIs(newService.get().getId(), "active");

        // then
        rancherClient.deleteStack(newStack.get().getId(), environment.getId());

    }

    @Test
    public void should_delete_service_instance() throws Exception {

        // given
        String expectedStackName = "stack-" + UUID.randomUUID().toString();

        Service service = new Service();
        service.setScale(1);
        service.setName("busybox");

        LaunchConfig launchConfig = new LaunchConfig();
        launchConfig.setImageUuid("docker:busybox");
        launchConfig.setPorts(Arrays.asList("1234:1234", "4321:4321"));

        service.setLaunchConfig(launchConfig);

        Environment environment = getEnvironment();

        Stack stack = new Stack();
        stack.setName(expectedStackName);
        Optional<Stack> newStack = rancherClient.createStack(stack, environment.getId());
        assertThat(newStack.isPresent(), is(true));

        Optional<Service> newService = rancherClient.createService(service, environment.getId(), newStack.get().getId());
        waitUntilServiceStateIs(newService.get().getId(), "active");
        assertThat(newService.isPresent(), is(true));

        // when
        rancherClient.deleteService(environment.getId(), newService.get().getId());

        waitUntilServiceStateIs(newService.get().getId(), "removed");
        // then

        // teardown
        rancherClient.deleteStack(newStack.get().getId(), environment.getId());

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

        rancherClient.finishUpgradeService(target.getId(), newService.get().getId());
        waitUntilServiceStateIs(newService.get().getId(), "active");

        // then
        assertThat(upgradedService.isPresent(), is(true));
        assertThat(upgradedService.get().getLaunchConfig().getImageUuid(), is("docker:busybox:1.26.2-glibc"));

        // teardown
        rancherClient.deleteStack(newStack.get().getId(), target.getId());

    }

    private void waitUntilHostStateIs(String hostId, String targetState) throws Exception {
        int i = 30;
        while (i-- > 0) {
            Optional<Host> checkService = rancherClient.host(hostId);
            String state = checkService.get().getState();
            if (state.equals(targetState)) {
                break;
            }
            System.out.println(state);
            Thread.sleep(2000);
        }
        if (i <= 0) {
            throw new Exception("Host[" + hostId + "] State not invalidate[" + targetState + "], current state is " + rancherClient.service(hostId).get().getState());
        }

    }

    private void waitUntilServiceStateIs(String serviceId, String targetState) throws Exception {
        int i = 30;
        while (i-- > 0) {
            Optional<Service> checkService = rancherClient.service(serviceId);
            String state = checkService.get().getState();
            if (state.equals(targetState)) {
                break;
            }
            System.out.println(state);
            Thread.sleep(2000);
        }
        if (i <= 0) {
            throw new Exception("Service[" + serviceId + "] State not invalidate[" + targetState + "], current state is " + rancherClient.service(serviceId).get().getState());
        }

    }

    private Environment getEnvironment() throws IOException {
        Optional<Environments> environments = rancherClient.environments();
        assertThat(environments.isPresent(), is(true));
        Optional<Environment> environment = environments.get().getData().stream().findAny();
        assertThat(environment.isPresent(), is(true));
        return environment.get();
    }

    private Optional<Host> getHost() throws IOException {
        Optional<Hosts> hosts = rancherClient.hosts();
        assertThat(hosts.isPresent(), is(true));
        assertThat(hosts.get().getData().size() > 0, is(true));

        return hosts.get().getData().stream().findFirst();
    }

}
