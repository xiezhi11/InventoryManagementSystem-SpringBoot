package com.example.dto;

import com.example.entity.InvoiceStatus;

public class InvoiceStatusRequest {
    private InvoiceStatus targetStatus;
    private String remark;

    public InvoiceStatusRequest() {
    }

    public InvoiceStatus getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(InvoiceStatus targetStatus) {
        this.targetStatus = targetStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
