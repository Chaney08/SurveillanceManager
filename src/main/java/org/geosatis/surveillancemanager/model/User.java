package org.geosatis.surveillancemanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long userId;
    @NotBlank(message = "UserName is mandatory")
    private String userName;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Name is mandatory")
    private String fullName;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id"))
    private List<Role> roles;
    @OneToMany(mappedBy="user")
    @JsonBackReference(value="user")
    private List<Schedule> schedules;

    public User() {

    }
    //This method is just for creating default users with roles on startup
    public User(String userName, String encrytedPassword,String username) {
        this.userId = userId;
        this.userName = userName;
        this.fullName = userName;
        this.password = encrytedPassword;


        List<Role> userPermission = new ArrayList<Role>();
        userPermission.add(new Role(1l,"ROLE_ADMIN"));
        this.roles = userPermission;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public String toString() {
        return this.userName + "/" + this.fullName;
    }


}
