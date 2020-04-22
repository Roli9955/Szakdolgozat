package hu.ELTE.Szakdolgozat.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Permission extends BasicEntity implements Serializable {

    public static final String herder[] =  {"Jogosultság csoport"};
    public static final Integer columns = herder.length;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    @NotNull
    private String name;
    
    @OneToMany(mappedBy = "permission")
    @JsonIgnore
    private List<User> users;
    
    @JoinTable
    @ManyToMany
    private List<PermissionDetail> details;

    @Transient
    private Integer userCount;

    @Override
    public String getData(Integer id) {
        switch (id){
            case 0:
                return this.name != null ? this.name : "";
            default:
                return "";
        }
    }
    
}
