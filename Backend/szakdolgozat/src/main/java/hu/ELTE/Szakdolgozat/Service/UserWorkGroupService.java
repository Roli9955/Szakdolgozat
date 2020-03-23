package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.UserWorkGroupRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserWorkGroupService {

    @Autowired
    private UserWorkGroupRepository userWorkGroupRepository;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    public Iterable<WorkGroup> getUserWorkGroupByDate(Integer year, Integer month, Integer day) {

        Date date = Date.valueOf(year + "-" + month + "-" + day);

        Iterable<UserWorkGroup> iUserWorkGroups = this.userWorkGroupRepository.findByUser(this.authenticatedUser.getUser());
        List<WorkGroup> workGroups = new ArrayList();

        for (UserWorkGroup uwg : iUserWorkGroups) {

            int dateStatusFrom = date.compareTo(uwg.getInWorkGroupFrom());
            int dateStatusTo;

            if (uwg.getInWorkGroupTo() != null) {
                dateStatusTo = date.compareTo(uwg.getInWorkGroupTo());
                if(dateStatusFrom >= 0 && dateStatusTo <= 0){
                    workGroups.add(uwg.getWorkGroup());
                }
            } else {
                if (dateStatusFrom >= 0) {
                    workGroups.add(uwg.getWorkGroup());
                }
            }
        }
        
        Iterable<WorkGroup> result =  workGroups;

        return result;
    }

}
