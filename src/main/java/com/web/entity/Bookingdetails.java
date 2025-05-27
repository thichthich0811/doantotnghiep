package com.web.entity;
import java.sql.Date;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bookingdetails" )
@Getter
@Setter
public class Bookingdetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
    private Date playdate;
    private Double price ;
    @ManyToOne
    private Field field ;
    @ManyToOne
    private Bookings bookings ;
    @ManyToOne
    private Shifts shifts ;

}
