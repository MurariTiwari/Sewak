package com.subarnarekha.softwares.sewak.addService;

import java.io.Serializable;

public class ServiceItemModel implements Serializable {
    private String serviceName;
    private String servicePrice;

    public ServiceItemModel() {
    }

    public ServiceItemModel(String serviceName, String servicePrice) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }
}
