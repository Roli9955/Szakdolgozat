package hu.ELTE.Szakdolgozat.Entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class UserWorkGroup extends BasicEntity implements Serializable {

    public static final String herder[] =  {"Projekt neve", "Felhasználó neve", "Mikortól", "Meddig"};
    public static final Integer columns = herder.length;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    @NotNull
    private Date inWorkGroupFrom;
    
    @Column
    private Date inWorkGroupTo;
    
    @JoinColumn
    @ManyToOne
    private WorkGroup workGroup;
    
    @JoinColumn
    @ManyToOne
    private User user;

    @Override
    public String getData(Integer id) {
        switch (id){
            case 0:
                return this.workGroup != null ? this.workGroup.getName() : "";
            case 1:
                return this.user != null ? this.user.getLastName() + " " + this.user.getFirstName() : "";
            case 2:
                return this.inWorkGroupFrom != null ? this.inWorkGroupFrom.toString() : "";
            case 3:
                return this.inWorkGroupTo != null ? this.inWorkGroupTo.toString() : "";
            default:
                return "";
        }
    }
}
