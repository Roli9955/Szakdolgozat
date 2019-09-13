package hu.ELTE.Szakdolgozat.Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
public class ActivityGroup implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    private Activity parent;
    
    @Column
    @NotNull
    private String name;
    
    @Column
    @NotNull
    private Boolean canAddChild;
    
    @Column
    @NotNull
    private Boolean canAddActivity;
    
    @OneToMany(mappedBy = "activityGroup")
    private List<Activity> activties;
    
    @ManyToMany(mappedBy = "activityGroup")
    private List<WorkGroup> workGroup;
    
}
