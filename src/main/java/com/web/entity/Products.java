package com.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


/**
 * JPA entity class for "Products"
 *
 * @author Telosys
 *
 */
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
