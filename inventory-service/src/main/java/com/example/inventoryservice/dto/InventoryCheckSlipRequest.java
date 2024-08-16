package com.example.inventoryservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InventoryCheckSlipRequest {

    private LocalDateTime inventoryCheckTime;
    private LocalDateTime inventoryBalancingDate;
    private Integer status;
    private Integer totalDiscrepancy;
    private Integer quantityDiscrepancyIncrease;
    private Integer quantityDiscrepancyDecrease;
    private String notes;
    private Long employeeId;
    private List<InventoryCheckDetailRequest> inventoryCheckDetails;


    public InventoryCheckSlipRequest(LocalDateTime inventoryCheckTime, LocalDateTime inventoryBalancingDate, Integer status, Long totalDiscrepancy, Integer quantityDiscrepancyIncrease, Integer quantityDiscrepancyDecrease, String notes, Long employeeId, List<InventoryCheckDetailRequest> inventoryCheckDetails) {
        this.inventoryCheckTime = inventoryCheckTime;
        this.inventoryBalancingDate = inventoryBalancingDate;
        this.status = status;

        this.quantityDiscrepancyIncrease = quantityDiscrepancyIncrease;
        this.quantityDiscrepancyDecrease = quantityDiscrepancyDecrease;
        this.notes = notes;
        this.employeeId = employeeId;
        this.inventoryCheckDetails = inventoryCheckDetails;
    }

    public InventoryCheckSlipRequest() {
    }

    public LocalDateTime getInventoryCheckTime() {
        return this.inventoryCheckTime;
    }

    public LocalDateTime getInventoryBalancingDate() {
        return this.inventoryBalancingDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getTotalDiscrepancy() {
        return this.totalDiscrepancy;
    }

    public Integer getQuantityDiscrepancyIncrease() {
        return this.quantityDiscrepancyIncrease;
    }

    public Integer getQuantityDiscrepancyDecrease() {
        return this.quantityDiscrepancyDecrease;
    }

    public String getNotes() {
        return this.notes;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public List<InventoryCheckDetailRequest> getInventoryCheckDetails() {
        return this.inventoryCheckDetails;
    }

    public void setInventoryCheckTime(LocalDateTime inventoryCheckTime) {
        this.inventoryCheckTime = inventoryCheckTime;
    }

    public void setInventoryBalancingDate(LocalDateTime inventoryBalancingDate) {
        this.inventoryBalancingDate = inventoryBalancingDate;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTotalDiscrepancy(Integer totalDiscrepancy) {
        this.totalDiscrepancy = totalDiscrepancy;
    }

    public void setQuantityDiscrepancyIncrease(Integer quantityDiscrepancyIncrease) {
        this.quantityDiscrepancyIncrease = quantityDiscrepancyIncrease;
    }

    public void setQuantityDiscrepancyDecrease(Integer quantityDiscrepancyDecrease) {
        this.quantityDiscrepancyDecrease = quantityDiscrepancyDecrease;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setInventoryCheckDetails(List<InventoryCheckDetailRequest> inventoryCheckDetails) {
        this.inventoryCheckDetails = inventoryCheckDetails;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof InventoryCheckSlipRequest)) return false;
        final InventoryCheckSlipRequest other = (InventoryCheckSlipRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$inventoryCheckTime = this.getInventoryCheckTime();
        final Object other$inventoryCheckTime = other.getInventoryCheckTime();
        if (this$inventoryCheckTime == null ? other$inventoryCheckTime != null : !this$inventoryCheckTime.equals(other$inventoryCheckTime))
            return false;
        final Object this$inventoryBalancingDate = this.getInventoryBalancingDate();
        final Object other$inventoryBalancingDate = other.getInventoryBalancingDate();
        if (this$inventoryBalancingDate == null ? other$inventoryBalancingDate != null : !this$inventoryBalancingDate.equals(other$inventoryBalancingDate))
            return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$totalDiscrepancy = this.getTotalDiscrepancy();
        final Object other$totalDiscrepancy = other.getTotalDiscrepancy();
        if (this$totalDiscrepancy == null ? other$totalDiscrepancy != null : !this$totalDiscrepancy.equals(other$totalDiscrepancy))
            return false;
        final Object this$quantityDiscrepancyIncrease = this.getQuantityDiscrepancyIncrease();
        final Object other$quantityDiscrepancyIncrease = other.getQuantityDiscrepancyIncrease();
        if (this$quantityDiscrepancyIncrease == null ? other$quantityDiscrepancyIncrease != null : !this$quantityDiscrepancyIncrease.equals(other$quantityDiscrepancyIncrease))
            return false;
        final Object this$quantityDiscrepancyDecrease = this.getQuantityDiscrepancyDecrease();
        final Object other$quantityDiscrepancyDecrease = other.getQuantityDiscrepancyDecrease();
        if (this$quantityDiscrepancyDecrease == null ? other$quantityDiscrepancyDecrease != null : !this$quantityDiscrepancyDecrease.equals(other$quantityDiscrepancyDecrease))
            return false;
        final Object this$notes = this.getNotes();
        final Object other$notes = other.getNotes();
        if (this$notes == null ? other$notes != null : !this$notes.equals(other$notes)) return false;
        final Object this$employeeId = this.getEmployeeId();
        final Object other$employeeId = other.getEmployeeId();
        if (this$employeeId == null ? other$employeeId != null : !this$employeeId.equals(other$employeeId))
            return false;
        final Object this$inventoryCheckDetails = this.getInventoryCheckDetails();
        final Object other$inventoryCheckDetails = other.getInventoryCheckDetails();
        if (this$inventoryCheckDetails == null ? other$inventoryCheckDetails != null : !this$inventoryCheckDetails.equals(other$inventoryCheckDetails))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof InventoryCheckSlipRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $inventoryCheckTime = this.getInventoryCheckTime();
        result = result * PRIME + ($inventoryCheckTime == null ? 43 : $inventoryCheckTime.hashCode());
        final Object $inventoryBalancingDate = this.getInventoryBalancingDate();
        result = result * PRIME + ($inventoryBalancingDate == null ? 43 : $inventoryBalancingDate.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $totalDiscrepancy = this.getTotalDiscrepancy();
        result = result * PRIME + ($totalDiscrepancy == null ? 43 : $totalDiscrepancy.hashCode());
        final Object $quantityDiscrepancyIncrease = this.getQuantityDiscrepancyIncrease();
        result = result * PRIME + ($quantityDiscrepancyIncrease == null ? 43 : $quantityDiscrepancyIncrease.hashCode());
        final Object $quantityDiscrepancyDecrease = this.getQuantityDiscrepancyDecrease();
        result = result * PRIME + ($quantityDiscrepancyDecrease == null ? 43 : $quantityDiscrepancyDecrease.hashCode());
        final Object $notes = this.getNotes();
        result = result * PRIME + ($notes == null ? 43 : $notes.hashCode());
        final Object $employeeId = this.getEmployeeId();
        result = result * PRIME + ($employeeId == null ? 43 : $employeeId.hashCode());
        final Object $inventoryCheckDetails = this.getInventoryCheckDetails();
        result = result * PRIME + ($inventoryCheckDetails == null ? 43 : $inventoryCheckDetails.hashCode());
        return result;
    }

    public String toString() {
        return "InventoryCheckSlipRequest(inventoryCheckTime=" + this.getInventoryCheckTime() + ", inventoryBalancingDate=" + this.getInventoryBalancingDate() + ", status=" + this.getStatus() + ", totalDiscrepancy=" + this.getTotalDiscrepancy() + ", quantityDiscrepancyIncrease=" + this.getQuantityDiscrepancyIncrease() + ", quantityDiscrepancyDecrease=" + this.getQuantityDiscrepancyDecrease() + ", notes=" + this.getNotes() + ", employeeId=" + this.getEmployeeId() + ", inventoryCheckDetails=" + this.getInventoryCheckDetails() + ")";
    }
}
