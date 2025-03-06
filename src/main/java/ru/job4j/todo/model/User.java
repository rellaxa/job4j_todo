package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "todo_user")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String login;

	private String password;
}
