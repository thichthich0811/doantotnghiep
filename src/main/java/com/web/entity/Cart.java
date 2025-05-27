package com.web.entity;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", columnDefinition = "BIGINT")
    private Products products;

    @ManyToOne
    private User user;
}
