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
public class Activity implements Serializable {
    
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
    @JsonIgnore
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
}
