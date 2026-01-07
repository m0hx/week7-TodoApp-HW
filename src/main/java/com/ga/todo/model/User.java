package com.ga.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"password", "UserProfile", "itemList", "categoryList"})
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column(unique = true)
    private String emailAddress;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // One User can have only one profile (1 - 1)
    @OneToOne(
            cascade = CascadeType.ALL, fetch = FetchType.LAZY
    )
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile userProfile;


    // User can have more than one Item
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Item> itemList;


    // User can have more than one Category
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Category> categoryList;


    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
