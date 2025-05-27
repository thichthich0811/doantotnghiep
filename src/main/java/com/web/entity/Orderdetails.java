package com.web.entity;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderdetails")
@Getter
@Setter
public class Orderdetails{

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	private Double price;
	private Integer quantity;
	@ManyToOne
	private Products products;
	@ManyToOne
	private Orders orders;

}
