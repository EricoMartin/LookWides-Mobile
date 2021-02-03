package com.mobilefintech09.lookwides.entities;


import com.squareup.moshi.Json;

public class OrderResponses {
    @Json(name = "id")
    private Integer id;
    @Json(name = "description")
    private String description;
    @Json(name = "location")
    private String location;
    @Json(name = "destination")
    private String destination;
    @Json(name = "reciever_name")
    private String recieverName;
    @Json(name = "reciever_address")
    private String recieverAddress;
    @Json(name = "reciever_phone")
    private String recieverPhone;
    @Json(name = "num_of_items")
    private String numOfItems;
    @Json(name = "weight")
    private Object weight;
    @Json(name = "avatar")
    private Object avatar;
    @Json(name = "status")
    private String status;
    @Json(name = "price")
    private String price;
    @Json(name = "payment_status")
    private String paymentStatus;
    @Json(name = "tranx_id")
    private String tranxId;
    @Json(name = "user_id")
    private String userId;
    @Json(name = "staff_id")
    private Object staffId;
    @Json(name = "created_at")
    private String createdAt;
    @Json(name = "updated_at")
    private String updatedAt;

    /**
     * No args constructor for use in serialization
     *
     */
    public OrderResponses() {
    }

    public OrderResponses(Integer id, String recieverName, String recieverPhone, String tranxId, String recieverAddress, String status, String price){
        this.id = id;
        this.recieverName = recieverName;
        this.recieverAddress = recieverAddress;
        this.recieverPhone = recieverPhone;
        this.status = status;
        this.price = price;
        this.tranxId = tranxId;
    }

    /**
     *
     * @param destination
     * @param numOfItems
     * @param description
     * @param weight
     * @param recieverName
     * @param avatar
     * @param userId
     * @param tranxId
     * @param createdAt
     * @param recieverAddress
     * @param price
     * @param recieverPhone
     * @param location
     * @param id
     * @param paymentStatus
     * @param staffId
     * @param status
     * @param updatedAt
     */
    public OrderResponses(Integer id, String description, String location, String destination, String recieverName, String recieverAddress, String recieverPhone, String numOfItems, Object weight, Object avatar, String status, String price, String paymentStatus, String tranxId, String userId, Object staffId, String createdAt, String updatedAt) {
        super();
        this.id = id;
        this.description = description;
        this.location = location;
        this.destination = destination;
        this.recieverName = recieverName;
        this.recieverAddress = recieverAddress;
        this.recieverPhone = recieverPhone;
        this.numOfItems = numOfItems;
        this.weight = weight;
        this.avatar = avatar;
        this.status = status;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.tranxId = tranxId;
        this.userId = userId;
        this.staffId = staffId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public String getRecieverAddress() {
        return recieverAddress;
    }

    public void setRecieverAddress(String recieverAddress) {
        this.recieverAddress = recieverAddress;
    }

    public String getRecieverPhone() {
        return recieverPhone;
    }

    public void setRecieverPhone(String recieverPhone) {
        this.recieverPhone = recieverPhone;
    }

    public String getNumOfItems() {
        return numOfItems;
    }

    public void setNumOfItems(String numOfItems) {
        this.numOfItems = numOfItems;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTranxId() {
        return tranxId;
    }

    public void setTranxId(String tranxId) {
        this.tranxId = tranxId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getStaffId() {
        return staffId;
    }

    public void setStaffId(Object staffId) {
        this.staffId = staffId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
