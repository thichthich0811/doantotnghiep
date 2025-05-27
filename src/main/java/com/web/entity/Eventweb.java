package com.web.entity;
import java.sql.Date;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="eventweb")
@Setter
@Getter
public class Eventweb {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String nameEvent;

    private Date dateStart;

    private Date dateEnd;

    private String image;

    @Column(columnDefinition = "LONGTEXT")
    private String descriptions;
    
    private String eventType;

}
