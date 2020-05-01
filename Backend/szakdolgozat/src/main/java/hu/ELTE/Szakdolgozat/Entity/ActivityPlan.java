package hu.ELTE.Szakdolgozat.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ActivityPlan extends BasicEntity implements Serializable {

    public static final String herder[] =  {"Cím", "Leírás", "Dátum", "Felhasználó"};
    public static final Integer columns = herder.length;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    private String title;

    @Column
    private String description;

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
    private User owner;

    @Override
    public String getData(Integer id) {
        switch (id){
            case 0:
                return this.title != null ? this.title : "";
            case 1:
                return this.description != null ? this.description : "";
            case 2:
                return this.date != null ? this.date.toString() : "";
            case 3:
                return this.user != null ? this.user.getLastName() + " " + this.user.getFirstName(): "";
            default:
                return "";
        }
    }
}
