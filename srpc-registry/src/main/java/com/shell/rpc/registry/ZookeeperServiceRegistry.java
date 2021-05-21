package com.shell.rpc.registry;

import com.shell.rpc.common.ServiceHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;

public class ZookeeperServiceRegistry implements ServiceRegistry {

    private final ServiceDiscovery<ServiceMetadata> serviceDiscovery;

    public ZookeeperServiceRegistry(String registryAddr) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(1000, 3));
        client.start();
        JsonInstanceSerializer<ServiceMetadata> serializer = new JsonInstanceSerializer<>(ServiceMetadata.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetadata.class)
                .client(client)
                .serializer(serializer)
                .basePath("/srpc")
                .build();
        this.serviceDiscovery.start();
    }

    @Override
    public void registry(ServiceMetadata serviceMetadata) throws Exception {
        ServiceInstance<ServiceMetadata> serviceInstance = ServiceInstance.<ServiceMetadata>builder()
                .name(ServiceHelper.buildServiceKey(serviceMetadata.getServiceName(), serviceMetadata.getServiceVersion()))
                .address(serviceMetadata.getServiceAddr())
                .port(serviceMetadata.getServicePort())
                .payload(serviceMetadata)
                .build();
        this.serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegistry(ServiceMetadata serviceMetadata) throws Exception {
        ServiceInstance<ServiceMetadata> serviceInstance = ServiceInstance.<ServiceMetadata>builder()
                .name(ServiceHelper.buildServiceKey(serviceMetadata.getServiceName(), serviceMetadata.getServiceVersion()))
                .address(serviceMetadata.getServiceAddr())
                .port(serviceMetadata.getServicePort())
                .payload(serviceMetadata)
                .build();
        this.serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public ServiceMetadata discovery(String serviceName) throws Exception {
        Collection<ServiceInstance<ServiceMetadata>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        if (serviceInstances != null) {
            return serviceInstances.iterator().next().getPayload();
        }
        return null;
    }

    @Override
    public void destroy() throws Exception {
        this.serviceDiscovery.close();
    }
}
