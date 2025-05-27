package com.web.entity;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Orders {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Date createDate;
    private String address;
    private String note;
    private String orderStatus;
    private Boolean paymentStatus;
    private Double totalPrice;
    private String fullName;
    private String phone;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy="orders")
    private List<Orderdetails> orderDetails ;
}
