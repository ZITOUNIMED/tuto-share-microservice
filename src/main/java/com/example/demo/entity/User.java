package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name="APP_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

    private String firstname;

    private String lastname;

    @Column(unique=true)
    private String username;

    private String password;

    private boolean enable;

    private String email;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="USERS_ROLES",
            joinColumns={@JoinColumn(name="USER_ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID")})
    private List<Role> roles;

	@Override
	public String toString() {
		return "User [firstname=" + firstname + ", lastname=" + lastname + ", username=" + username + ", roles=" + roles
				+ "]";
	}
    
    
}