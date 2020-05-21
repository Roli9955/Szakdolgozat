package hu.ELTE.Szakdolgozat.Entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Holiday extends BasicEntity implements Serializable {

    public static final String herder[] =  {"Felhasználó neve", "Mikortól", "Meddig"};
    public static final Integer columns = herder.length;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    @NotNull
    private Date holidayFrom;
    
    @Column
    @NotNull
    private Date holidayTo;

    @Column
    @NotNull
    private Integer days;

    @Column
    @NotNull
    private Boolean deleted;

    @Transient
    private Integer userId;
    
    @JoinColumn
    @ManyToOne
    @JsonIgnore
    @NotNull
    private User user;

    @Override
    public String getData(Integer id) {
        switch (id){
            case 0:
                return this.user != null ? this.user.getLastName() + " " + this.user.getFirstName() : "";
            case 1:
                return this.holidayFrom != null ? this.holidayFrom.toString() : "";
            case 2:
                return this.holidayTo != null ? this.holidayTo.toString() : "";
            default:
                return "";
        }
    }
    
}
