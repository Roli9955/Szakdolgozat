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
public class WorkGroup extends BasicEntity implements Serializable {

    public static final String herder[] =  {"Projekt neve"};
    public static final Integer columns = herder.length;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    @NotNull
    private String name;
    
    @Column
    @NotNull
    private Integer scale;

    @Column
    @NotNull
    private Boolean deleted;
    
    @JoinTable
    @ManyToMany
    private List<ActivityGroup>  activityGroup;

    @Transient
    private Integer userCount;

    @Transient
    private Integer activityGroupCount;
    
    @OneToMany(mappedBy = "workGroup", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserWorkGroup> userWorkGroup;
    
    @OneToMany(mappedBy = "workGroup", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Activity> activities;

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
