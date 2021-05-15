package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.todo.app.data.util.base.AuditModel;
import com.todo.app.security.util.enums.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = "psw", allowSetters = true)
public class User extends AuditModel<User> {

    @ApiModelProperty(position = 0, example = "example@mail.ru")
    @NotNull
    @Size(min = 7, max = 255)
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,63})$")
    @Column(unique = true, nullable = false, length = 256)
    protected String email;

    @ApiModelProperty(position = 1, example = "password1")
    @NotNull
    @Size(min = 8, max = 1181)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Zа-яА-Я]).*$")
    @Column(nullable = false, length = 1181)
    protected String psw;

    @ApiModelProperty(position = 2, example = "Ivan")
    @Size(max = 50)
    @Column(length = 50)
    protected String name;

    @ApiModelProperty(position = 3, example = "Ivanov")
    @Size(max = 50)
    @Column(length = 50)
    protected String surname;

    @ApiModelProperty(position = 4, example = "Ivanovich")
    @Size(max = 50)
    @Column(length = 50)
    protected String patronymic;

    @ApiModelProperty(position = 5, example = "2020-12-31")
    protected Date birthdate;

    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, position = 6)
    @Enumerated(EnumType.STRING)
    protected Role role = Role.BASIC;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Category> categories = new HashSet<>();
    
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Refresh> refreshes = new HashSet<>();

    public void addCategory(Category category) {
        this.categories.add(category);
        category.setUser(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.setUser(null);
    }

    public void addRefresh(Refresh refresh) {
        this.refreshes.add(refresh);
        refresh.setUser(this);
    }

    public void removeRefresh(Refresh refresh) {
        this.refreshes.remove(refresh);
        refresh.setUser(null);
    }
}
