package hu.ELTE.Szakdolgozat.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    @Column
    private Integer permissionId;
    
}
