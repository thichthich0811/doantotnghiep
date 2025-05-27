package com.web.entity;
import java.sql.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="bookings" )
public class Bookings {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private Date bookingDate;
    private Double bookingPrice;
    private String phone;
    private String note;
    private String fullName;
    private String bookingStatus;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "bookings", cascade = CascadeType.REMOVE)
    private List<Bookingdetails> bookingdetails;

}
