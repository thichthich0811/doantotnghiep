package com.web.entity;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="teamdetails")
@Getter
@Setter
public class Teamdetails{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private LocalDate joinDate;
    private String infoUser;
    private Boolean status;
    @ManyToOne
    private User user;
    @ManyToOne
    private Teams teams;


    

}
