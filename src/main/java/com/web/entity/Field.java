package com.web.entity;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="field" )
@Getter
@Setter
public class Field {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String nameField;
    @Column(columnDefinition = "LONGTEXT")
    private String descriptionField;
    private Double price;
    private String image;
    private String address;
    private Boolean status;
    @ManyToOne
    private Sporttype sporttype;
    @OneToMany(mappedBy="field")
    private List<Bookingdetails> listOfBookingdetails ; 

}
