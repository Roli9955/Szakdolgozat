package hu.ELTE.Szakdolgozat.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    @NotNull
    private String firstName;
    
    @Column
    @NotNull
    private String lastName;
    
    @Column
    @NotNull
    private String email;
    
    @Column
    @NotNull
    private String loginName;
    
    @Column
    @NotNull
    private String password;
    
    @Column
    private Date lastLogin;
    
    @Column
    private Integer maxHoliday;
    
    @Column
    private Boolean canLogIn;
    
    @JoinColumn
    @ManyToOne
    private Permission permission;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Holiday> holidays;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Activity> activitys;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Activity> ownedActivity;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserWorkGroup> userWorkGroup;
    
    
}
