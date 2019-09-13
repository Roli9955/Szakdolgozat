package hu.ELTE.Szakdolgozat.Entity;

import java.io.Serializable;
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
public class UserActivity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JoinColumn
    @NotNull
    @ManyToOne
    private User user;
    
    @JoinColumn
    @NotNull
    @ManyToOne
    private Activity activity;
    
    @JoinColumn
    @NotNull
    @ManyToOne
    private User owner;
    
}
