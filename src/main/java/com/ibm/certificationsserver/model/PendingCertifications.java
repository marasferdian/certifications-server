package com.ibm.certificationsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pending_certification",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "category"}))
public class PendingCertifications {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name="category")
    private Category category;

    @Column(name="cost")
    private double cost;

    public void setCategory(String category){
        this.category=Category.valueOf(category.toUpperCase());
    }
}
