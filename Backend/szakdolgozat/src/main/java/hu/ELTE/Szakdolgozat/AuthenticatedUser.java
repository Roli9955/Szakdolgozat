package hu.ELTE.Szakdolgozat;

import hu.ELTE.Szakdolgozat.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AuthenticatedUser {
    
    private User user;
    
}
