package be.uantwerpen.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  NV 2018
 */
@Entity
public class User extends MyAbstractPersistable<Long> {
    @NotBlank(message = "***")
    private String firstName;
    @NotBlank(message = "***")
    private String lastName;
    @NotBlank(message = "***")
    private String userName;
    @NotBlank(message = "***")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="USER_ROLE",
            joinColumns={@JoinColumn(name="USER_ID",
                    referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID",
                    referencedColumnName="ID")})
    private List<Role> roles;

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = firstName;
        this.password = lastName;
        roles = new ArrayList<>();
    }

    public User(String firstName, String lastName, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public User(String firstName, String lastName, String userName, String password, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean hasPermission(String permission)
    {
        for(Role r: roles)
        {
            for(Permission p: r.getPermissions())
            {
                if(p.getName().equals(permission))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public String toString(){
        return "[User-" + getId() + "] " + getUserName() + " (" + getFirstName() + " - " + getLastName() + ")";
    }
}
