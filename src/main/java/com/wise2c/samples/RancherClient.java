package com.wise2c.samples;

import com.wise2c.samples.action.ServiceRestart;
import com.wise2c.samples.action.ServiceUpgrade;
import com.wise2c.samples.entity.*;

import java.io.IOException;
import java.util.Optional;


/**
 * Note: 获取资源API
 */
public class RancherClient extends HttpClient {


    /**
     * 获取Rancher Client实例对象
     *
     * @param endpoint:  rancher account key api address
     * @param accesskey: rancher account accesskey
     * @param secretKey: rancher account secretkey
     */
    public RancherClient(String endpoint, String accesskey, String secretKey) {
        super(endpoint, accesskey, secretKey);
    }

    /**
     * 获取当前Rancher实例下的所有Environment信息
     * 注意:Rancher API中Project对象对应的就是Environment
     * http://rancher-server/v2-beta/projects
     */
    public Optional<Environments> environments() throws IOException {
        return Optional.ofNullable(get("/projects", Environments.class));
    }

    /**
     * 获取当前Rancher实例下的所有Environment信息
     * 注意:Rancher API中Project对象对应的就是Environment
     * http://rancher-server/v2-beta/stacks
     */
    public Optional<Stacks> stacks() throws IOException {
        return Optional.ofNullable(get("/stacks", Stacks.class));
    }

    public Optional<Stacks> stacks(String environmentId) throws IOException {
        return Optional.ofNullable(get("/projects/" + environmentId + "/stacks", Stacks.class));
    }

    /**
     * 获取Rancher实例下Stack详情
     * 注意:Rancher API中Project对象对应的就是Environment
     * http://rancher-server/v2-beta/stacks/${stackId}
     */
    public Optional<Stack> stack(String stackId) throws IOException {
        return Optional.ofNullable(get("/stacks/" + stackId, Stack.class));
    }

    /**
     * 获取当前Rancher的所有Service信息
     * http://rancher-server/v2-beta/services
     */
    public Optional<Services> services() throws IOException {
        return Optional.ofNullable(get("/services", Services.class));
    }

    /**
     * 获取当前Rancher实例Stack下的所有Service信息
     * http://rancher-server/v2-beta/stacks/${stackId}/services
     */
    public Optional<Services> services(String stackId) throws IOException {
        return Optional.ofNullable(get("/stacks/" + stackId + "/services", Services.class));
    }

    public Optional<LoadBalancerServices> loadBalancerServices() throws IOException {
        return Optional.ofNullable(get("/loadbalancerservices", LoadBalancerServices.class));
    }

    public Optional<LoadBalancerService> loadBalancerService(String serviceId) throws IOException {
        return Optional.ofNullable(get("/loadbalancerservices/" + serviceId, LoadBalancerService.class));
    }

    public Optional<LoadBalancerService> createLoadBalancerServices(String environmentId, LoadBalancerService loadBalancerService) throws IOException {
        return Optional.ofNullable(post(String.format("/projects/%s/loadbalancerservices", environmentId), loadBalancerService, LoadBalancerService.class));
    }

    public Optional<LoadBalancerService> updateLoadBalancerService(String environmentId, String serviceId, LoadBalancerService loadBalancerService) throws IOException {
        return Optional.ofNullable(put(String.format("/projects/%s/loadbalancerservices/%s", environmentId, serviceId), loadBalancerService, LoadBalancerService.class));
    }

    /**
     * 获取Rancher实例下Service详情
     * http://rancher-server/v2-beta/services/${serviceId}
     */
    public Optional<Service> service(String serviceId) throws IOException {
        return Optional.ofNullable(get("/services/" + serviceId, Service.class));
    }

    /**
     * 获取Rancher实例下Service下的容器实例信息
     * * http://rancher-server/v2-beta/services/${serviceId}/instances
     */
    public Optional<Instances> containerInstances(String serviceId) throws IOException {
        return Optional.ofNullable(get("/services/" + serviceId + "/instances", Instances.class));
    }

    /**
     * 获取容器实例详细信息
     * * http://rancher-server/v2-beta/containers/${instanceId}
     */
    public Optional<Instance> containerInstance(String instanceId) throws IOException {
        return Optional.ofNullable(get("/containers/" + instanceId, Instance.class));
    }

    /**
     * 获取当前Rancher实例下的所有Host信息
     * 注意:Rancher API中Project对象对应的就是Environment
     * http://rancher-server/v2-beta/hosts
     */
    public Optional<Hosts> hosts() throws IOException {
        return Optional.ofNullable(get("/hosts", Hosts.class));
    }

    /**
     * 获取当前Rancher实例下Environment所有Host信息
     * 注意:Rancher API中Project对象对应的就是Environment
     * http://rancher-server/v2-beta/projects/${environmentId}/hosts
     */
    public Optional<Hosts> hosts(String environmentId) throws IOException {
        return Optional.ofNullable(get("/projects/" + environmentId + "/hosts", Hosts.class));
    }

    /**
     * 获取当前Rancher实例下Host主机信息
     * http://rancher-server/v2-beta/hosts/${id}
     */
    public Optional<Host> host(String id) throws IOException {
        return Optional.ofNullable(get("/hosts/" + id, Host.class));
    }

    /**
     * http://rancher-server/v2-beta/hosts/1h34/?action=deactivate
     */
    public Optional<Host> deactivateHost(String hostId) throws IOException {
        return Optional.ofNullable(post("/hosts/" + hostId + "/?action=deactivate", Host.class));
    }

    /**
     * http://rancher-server/v2-beta/hosts/1h34/?action=activate
     */
    public Optional<Host> activateHost(String hostId) throws IOException {
        return Optional.ofNullable(post("/hosts/" + hostId + "/?action=activate", Host.class));
    }

    /**
     * http://rancher-server/v2-beta/hosts/1h34/?action=evacuate
     * "deactivate" -> "
     */
    public Optional<Host> evacuateHost(String hostId) throws IOException {
        return Optional.ofNullable(post("/hosts/" + hostId + "/?action=evacuate", Host.class));
    }

    /**
     * 更新Rancher实例下Host主机信息
     */
    public Optional<Host> updateHost(String environmentId, Host host) throws IOException {
        return Optional.ofNullable(put("/projects/" + environmentId + "/hosts/" + host.getId(), host, Host.class));
    }

    /**
     * 创建Stack实例
     * http://rancher-server/v2-beta/projects/${project_id}/stack
     */
    public Optional<Stack> createStack(Stack stack, String environmentId) throws IOException {
        return Optional.ofNullable(post(String.format("/projects/%s/stack", environmentId), stack, Stack.class));
    }

    /**
     * 在Stack下创建Service
     * http://rancher-server/v2-beta/projects/${project_id}/service
     */
    public Optional<Service> createService(Service service, String environmentId, String stackId) throws IOException {
        service.setStackId(stackId);
        return Optional.ofNullable(post(String.format("/projects/%s/service", environmentId), service, Service.class));
    }

    /**
     * 在Stack下升级Service
     * http://rancher-server/v2-beta/projects/${project_id}/services/${service_id}/?action=upgrade
     *
     * @param environmentId
     * @param serviceId
     * @param serviceUpgrade
     */
    public Optional<Service> upgradeService(String environmentId, String serviceId, ServiceUpgrade serviceUpgrade) throws IOException {
        return Optional.ofNullable(post(String.format("/projects/%s/services/%s/?action=upgrade", environmentId, serviceId), serviceUpgrade, Service.class));
    }

    /***
     * 在Stack下Service升级回滚
     * http://rancher-server/v2-beta/projects/${project_id}/services/${service_id}/?action=rollback
     *
     * @param environmentId
     * @param serviceId
     */
    public Optional<Service> rollbackService(String environmentId, String serviceId) throws IOException {
        return Optional.ofNullable(post(String.format("/projects/%s/services/%s/?action=rollback", environmentId, serviceId), Service.class));
    }

    /***
     * 在Stack下Service确认升级完成
     * http://rancher-server/v2-beta/projects/${project_id}/services/${service_id}/?action=finishupgrade
     *
     * @param environmentId
     * @param serviceId
     */
    public Optional<Service> finishUpgradeService(String environmentId, String serviceId) throws IOException {
        return Optional.ofNullable(post(String.format("/projects/%s/services/%s/?action=finishupgrade", environmentId, serviceId), Service.class));
    }

    /***
     * 在Stack下Service重启
     * http://rancher-server/v2-beta/projects/${project_id}/services/${service_id}/?action=restart
     *
     * @param environmentId
     * @param serviceId
     * @param serviceRestart
     */
    public Optional<Service> restartService(String environmentId, String serviceId, ServiceRestart serviceRestart) throws IOException {
        return Optional.ofNullable(post(String.format("/projects/%s/services/%s/?action=restart", environmentId, serviceId), serviceRestart, Service.class));
    }

    /***
     * 在Stack下Service停止
     * http://rancher-server/v2-beta/projects/${project_id}/services/${service_id}/?action=deactivate
     *
     * @param environmentId
     * @param serviceId
     */
    public Optional<Service> deactivateService(String environmentId, String serviceId) throws IOException {
        return Optional.ofNullable(post(String.format("/projects/%s/services/%s/?action=deactivate", environmentId, serviceId), Service.class));
    }

    /***
     * 在Stack下Service启动
     * http://rancher-server/v2-beta/projects/${project_id}/services/${service_id}/?action=activate
     *
     * @param environmentId
     * @param serviceId
     */
    public Optional<Service> activateService(String environmentId, String serviceId) throws IOException {
        return Optional.ofNullable(post(String.format("/projects/%s/services/%s/?action=activate", environmentId, serviceId), Service.class));
    }

    /**
     * 在Environment下删除应用堆栈
     * http://rancher-server/v2-beta/projects/${project_id}/services/${serviceId}
     */
    public void deleteService(String environmentId, String serviceId) throws IOException {
        delete(String.format("/projects/%s/services/%s", environmentId, serviceId), Service.class);
    }

    /**
     * 在Environment下删除应用堆栈
     * http://rancher-server/v2-beta/projects/${project_id}/stacks/${stackId}
     */
    public void deleteStack(String id, String environmentID) throws IOException {
        delete(String.format("/projects/%s/stacks/%s", environmentID, id), Stack.class);
    }


}
