package com.afkl.travel.exercise.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	@NotNull
	@Enumerated(EnumType.STRING)
	private LocationType type;

	@Digits(integer = 2, fraction = 15)
	private BigDecimal latitude;

	@Digits(integer = 3, fraction = 15)
	private BigDecimal longitude;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", updatable = false)
	private Location parent;

	@OneToMany(mappedBy = "location")
	private List<Translation> translation;
}
