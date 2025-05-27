package com.web.entity;
import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="contact" )
@Getter
@Setter
public class Contacts {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	private Date datecontact ;

	private String category;

	private String title;

	private String meesagecontact;
	
	@ManyToOne
    private User user;
	
	
}
