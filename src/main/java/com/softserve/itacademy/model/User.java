package com.softserve.itacademy.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "First name mustn't be null")
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, message = "First name must be min 2 symbols")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(-[A-Z][a-zA-Z]*)*$", message = "First name must contain at least one uppercase letter, one lowercase letter")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull(message = "Last name mustn't be null")
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, message = "First name must be min 2 symbols")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(-[A-Z][a-zA-Z]*)*$", message = "Last name must contain at least one uppercase letter, one lowercase letter")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be empty")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    //@Getter(AccessLevel.NONE)
    //@Setter(AccessLevel.NONE)
    @NotNull(message = "Password mustn't be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-\\[\\]{}:;',.?/\\\\~$^+=<>]).{8,20}$",
            message = "Password must be at least 8 characters long and must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany
    @JoinTable(name = "todo_collaborator",
            joinColumns = @JoinColumn(name = "collaborator_id"),
            inverseJoinColumns = @JoinColumn(name = "todo_id"))
    private List<ToDo> todos;

    @OneToMany(mappedBy = "owner")
    private List<ToDo> ownedToDoList;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + (role != null ? role.getName() : "null") +
                '}';
    }
}