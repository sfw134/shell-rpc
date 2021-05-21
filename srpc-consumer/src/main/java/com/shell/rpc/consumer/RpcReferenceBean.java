package com.shell.rpc.consumer;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class RpcReferenceBean implements FactoryBean<Object> {

    private String serviceVersion;
    private String registryAddr;
    private long timeout;
    private Class<?> interfaceClass;
    private Object object;

    public void init() throws Exception {
        this.object = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new RpcInvocationHandler(this.serviceVersion,
                        this.timeout,
                        this.registryAddr)
        );
    }

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getRegistryAddr() {
        return registryAddr;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
