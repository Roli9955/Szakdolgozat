package hu.ELTE.Szakdolgozat.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
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
public class User extends BasicEntity implements Serializable {

    public static final String herder[] =  {"Vezetéknév", "Keresztnév", "E-mail", "Bejelentkezési név", "Utolsó bejelentkezés", "Szabadságok száma", "Engedélyezett"};
    public static final Integer columns = herder.length;
    
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
    @JsonIgnore
    private String password;
    
    @Column
    private Date lastLogin;
    
    @Column
    private Integer maxHoliday;
    
    @Column
    private Boolean canLogIn;

    @Column
    @NotNull
    @JsonIgnore
    private Boolean deleted;
    
    @JoinColumn
    @ManyToOne
    private Permission permission;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Holiday> holidays;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Activity> activitys;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Activity> ownedActivity;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserWorkGroup> userWorkGroup;

    @Override
    public String getData(Integer id) {
        switch (id){
            case 0:
                return this.lastName != null ? this.lastName : "";
            case 1:
                return this.firstName != null ? this.firstName : "";
            case 2:
                return this.email != null ? this.email : "";
            case 3:
                return this.loginName != null ? this.loginName : "";
            case 4:
                return this.lastLogin != null ? this.lastLogin.toString() : "";
            case 5:
                return this.holidays != null ? this.holidays.toString() : "";
            case 6:
                return this.canLogIn != null ? this.canLogIn.toString() : "";
            default:
                return "";
        }
    }
}
