package com.example.orderservice.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class ContactInfo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String district;

    @Column
    private String ward;

    @Column
    private String detailedAddress;

    private Integer status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @JsonIgnore
    @OneToMany(mappedBy = "contactInfo")
    private List<Invoice> invoices;

    public ContactInfo(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder address = new StringBuilder();
        address.append(recipientName != null ? recipientName : "No Name");
        address.append(" - ");
        address.append(phoneNumber != null ? phoneNumber : "No Phone Number");
        address.append(" - ");
        address.append(detailedAddress != null ? detailedAddress : "No Detailed Address");

        if (ward != null && !ward.isEmpty()) {
            address.append(", ").append(ward);
        }
        if (district != null && !district.isEmpty()) {
            address.append(", ").append(district);
        }
        if (province != null && !province.isEmpty()) {
            address.append(", ").append(province);
        }

        return address.toString();
    }



    public ContactInfo() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
