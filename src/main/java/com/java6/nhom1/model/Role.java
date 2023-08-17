package com.java6.nhom1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Table(name = "roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "id")
    private String roleId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "`desc`")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<User> users;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleId;
    }
}
