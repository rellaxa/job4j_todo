package ru.job4j.todo.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "priorities")
public class Priority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Include
	private int id;

	private String name;

	private int position;

}