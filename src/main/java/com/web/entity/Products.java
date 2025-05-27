package com.web.entity;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="products")
@Getter
@Setter
public class Products {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String productName;
    private String image;
    private Double discountPrice;
    private Date dateCreate;
    private Double price;
    private Boolean productStatus;
    @Column(columnDefinition = "LONGTEXT")
    private String descriptions;
    private Integer quantity;
    private Integer quantitySold;
    @ManyToOne
    private Categories categories;

}
