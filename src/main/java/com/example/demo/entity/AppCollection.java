package com.example.demo.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppCollection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="APP_COLLECTION_ID")
	private Long id;
	
	@NotNull
	private String ownerUsername;
	
	@NotNull
	private String name;
	
	private String description;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="APP_COLLECTIONS_DOCUMENTS",
    joinColumns={@JoinColumn(name="APP_COLLECTION_ID")},
    inverseJoinColumns={@JoinColumn(name="DOCUMENT_ID")})
	private Set<Document> documents;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name="APP_COLLECTIONS_USERS",
    joinColumns={@JoinColumn(name="APP_COLLECTION_ID")},
    inverseJoinColumns={@JoinColumn(name="USER_ID")})
	private Set<User> members;
	
	public AppCollection(@NotNull String ownerUsername, @NotNull String name, String description) {
		super();
		this.ownerUsername = ownerUsername;
		this.name = name;
		this.description = description;
	}

}
