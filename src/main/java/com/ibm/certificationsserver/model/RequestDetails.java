package com.ibm.certificationsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestDetails {
    private Quarter quarter;
    private String participantName;
    private String certificationTitle;
    private Category category;
    private Status status;
    private double cost;
    private String businessJustification;
}
