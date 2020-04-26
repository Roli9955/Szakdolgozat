package hu.ELTE.Szakdolgozat.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class ActivityGroup extends BasicEntity implements Serializable {

    public static final String herder[] =  {"Feladat csoport neve", "Szülő feladat neve", "Bontható"};
    public static final Integer columns = herder.length;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    private ActivityGroup parent;
    
    @Column
    @NotNull
    private String name;
    
    @Column
    @NotNull
    private Boolean canAddChild;

    @Column
    @NotNull
    private Boolean easyLogIn;

    @Column
    @NotNull
    private Boolean deleted;
    
    @OneToMany(mappedBy = "activityGroup", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Activity> activties;
    
    @ManyToMany(mappedBy = "activityGroup", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<WorkGroup> workGroup;
    
    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<ActivityGroup> children;

    @Override
    public String getData(Integer id) {
        switch (id){
            case 0:
                return this.name != null ? this.name : "";
            case 1:
                return this.parent != null ? this.parent.getName() : "Nincs";
            case 2:
                return this.canAddChild != null ? this.canAddChild.toString() : "";
            default:
                return "";
        }
    }
}
