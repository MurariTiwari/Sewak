package com.subarnarekha.softwares.sewak.searchresults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Modal {

    String desc ;
    String address ;
    double distance ;
    String images ;
    String from ;
    String service ;
    List<String> allImages ;
    ArrayList<Map<String,Object>> serviceMenu ;
    String user ;
    String allowPhone ;
    String serviceNumber;




    public Modal(String desc,
                 String address,
                 String from,
                 String user,
                 String service,
                 String allowPhone,
                 double distance,
                 String images,
                 List<String> allImages,
                 ArrayList<Map<String, Object>> serviceMenu,
                 String serviceNumber) {
        this.desc = desc;
        this.address = address;
        this.distance = distance;
        this.images = images;
        this.from = from;
        this.service = service;
        this.allImages = allImages;
        this.serviceMenu = serviceMenu;
        this.user = user;
        this.allowPhone = allowPhone;
        this.serviceNumber = serviceNumber;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setAllImages(List<String> allImages) {
        this.allImages = allImages;
    }

    public void setServiceMenu(ArrayList<Map<String, Object>> serviceMenu) {
        this.serviceMenu = serviceMenu;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAllowPhone(String allowPhone) {
        this.allowPhone = allowPhone;
    }

    public String getDesc() {
        return desc;
    }

    public String getAddress() {
        return address;
    }

    public double getDistance() {
        return distance;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public String getImages() {
        return images;
    }

    public String getFrom() {
        return from;
    }

    public String getService() {
        return service;
    }

    public List<String> getAllImages() {
        return allImages;
    }

    public ArrayList<Map<String, Object>> getServiceMenu() {
        return serviceMenu;
    }

    public String getUser() {
        return user;
    }

    public String getAllowPhone() {
        return allowPhone;
    }
}
