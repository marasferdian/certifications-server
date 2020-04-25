package com.ibm.certificationsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="id_user")
    private Long idUser;

    @Column(name="id_certificate")
    private Long idCertificate;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name="quarter")
    private Quarter quarter;

    @Column(name="business_justification")
    private String businessJustification;

}
