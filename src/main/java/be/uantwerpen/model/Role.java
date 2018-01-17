package be.uantwerpen.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * NV 2018
 */
@Entity
public class Role extends MyAbstractPersistable<Long> {
    @Size(min=2, max=60)
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="ROLE_PERM",
            joinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="PERM_ID", referencedColumnName="ID")})
    private List<Permission> permissions;

    @ManyToMany(mappedBy="roles")
    private List<User> users;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
