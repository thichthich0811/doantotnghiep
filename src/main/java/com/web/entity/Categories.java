package com.web.entity;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="categories" )
@Getter
@Setter
public class Categories {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;

    private String categoryName ;

}
