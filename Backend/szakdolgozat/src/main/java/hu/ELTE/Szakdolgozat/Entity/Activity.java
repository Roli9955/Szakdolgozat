package hu.ELTE.Szakdolgozat.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Date;
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
public class Activity extends BasicEntity implements Serializable{

    public static final String herder[] =  {"Feladat neve", "Leírás", "Perc", "Határidő", "Teljesített", "Dátum", "Felhasználó", "Tulajdonos", "Projekt neve"};
    public static final Integer columns = herder.length;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    private String description;
    
    @Column
    @NotNull
    private Boolean isTask;
    
    @Column
    private Integer min;
    
    @Column
    private Date deadline;
    
    @Column
    private Boolean isCompleted;
    
    @Column
    @NotNull
    private Boolean locked;
    
    @Column
    @NotNull
    private Date date;
    
    @JoinColumn
    @ManyToOne
    @NotNull
    private User user;
    
    @JoinColumn
    @ManyToOne
    @NotNull
    @JsonIgnore
    private User owner;

    @JoinColumn
    @ManyToOne
    private ActivityGroup activityGroup;
    
    @JoinColumn
    @ManyToOne
    private WorkGroup workGroup;

    @Override
    public String getData(Integer id){
        switch (id){
            case 0:
                return this.activityGroup != null ? this.activityGroup.getName() : "";
            case 1:
                return this.description != null ? this.description : "";
            case 2:
                return this.min != null ? this.min.toString() : "";
            case 3:
                return this.deadline != null ? this.deadline.toString() : "";
            case 4:
                return this.isCompleted != null ? this.isCompleted.toString() : "";
            case 5:
                return this.date != null ? this.date.toString() : "";
            case 6:
                return this.user != null ? this.user.getLoginName() : "";
            case 7:
                return this.owner != null ? this.owner.getLoginName() : "";
            case 8:
                return this.workGroup != null ? this.workGroup.getName() : "";
            default:
                return "";
        }
    }

}
