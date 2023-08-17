package com.java6.nhom1.model;

import com.java6.nhom1.validation.constraints.PasswordConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "Id")
    @NotNull(message = "User ID không để trống")
    private String id;

    @Column(name = "`Password`")
    @PasswordConstraint(message = "Mật khẩu không hợp lệ", groups = Basic.class)
    private String password;

    @Column(name = "Fullname")
    @NotNull(message = "Tên không để trống", groups = Basic.class)
    private String fullName;

    @Column(name = "Email")
    @Email(message = "Email không hợp lệ", groups = Basic.class)
    @NotNull(message = "Email không để trống", groups = Basic.class)
    private String email;

    @ManyToOne
    @JoinColumn(name = "`Role`")
    @NotNull(message = "Role không bỏ trống", groups = Admin.class)
    private Role role;

    public interface Basic{}
    public interface Admin{}
}
