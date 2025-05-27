package com.web.entity;
import java.time.LocalTime;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="shifts")
@Getter
@Setter
public class Shifts {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String nameShift;
    private LocalTime startTime;
    private LocalTime endTime;
}
