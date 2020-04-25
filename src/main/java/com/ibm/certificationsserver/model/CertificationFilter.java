package com.ibm.certificationsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@NoArgsConstructor
@Getter
public class CertificationFilter {
    private Status status;
    private Quarter quarter;

    public void setStatus(String status){
        this.status=Status.valueOf(status.toUpperCase());
    }

    public void setQuarter(String quarter){
        this.quarter=Quarter.valueOf(quarter.toUpperCase());
    }
}
