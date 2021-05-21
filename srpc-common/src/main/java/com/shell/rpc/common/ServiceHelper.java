package com.shell.rpc.common;

public class ServiceHelper {

    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }
}
