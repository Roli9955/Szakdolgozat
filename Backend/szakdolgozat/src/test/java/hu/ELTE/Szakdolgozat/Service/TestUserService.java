package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.ActivityGroupRepository;
import hu.ELTE.Szakdolgozat.Repository.ActivityRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.Util.TestGlobalMocks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestUserService {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private ActivityGroupRepository activityGroupRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private UserService userService;

    private User userNotNull;
    private User userNotNull2;

    private ActivityGroup activityGroupNotNull;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;
        userNotNull2 = TestGlobalMocks.userNotNull2;

        activityGroupNotNull = TestGlobalMocks.activityGroupNotNull;

        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
    }

    @Test
    public void testGetAllUser(){
        TestGlobalMocks.fill();

        List<User> list = new ArrayList<>();
        list.add(userNotNull);
        list.add(userNotNull2);
        Mockito.when(userRepository.findByDeletedFalse()).thenReturn(list);

        Iterable<User> res = userService.getAllUser();

        assertEquals(list, res);
    }

    @Test
    public void testGetUsersForHoliday(){
        TestGlobalMocks.fill();

        List<User> list = new ArrayList<>();
        list.add(userNotNull);
        list.add(userNotNull2);
        Mockito.when(userRepository.findAllByOrderByLastName()).thenReturn(list);

        Iterable<User> res = userService.getUsersForHoliday();

        assertEquals(list, res);
    }

    @Test
    public void testAddUser(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findByEmailAndLoginName(userNotNull.getEmail(), userNotNull.getLoginName())).thenReturn(oUser);

        User res = userService.adduser(userNotNull);

        assertEquals(null, res);
    }

    @Test
    public void testAddUser2(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.empty();
        Mockito.when(userRepository.findByEmailAndLoginName(userNotNull.getEmail(), userNotNull.getLoginName())).thenReturn(oUser);
        Mockito.when(userRepository.save(userNotNull)).thenReturn(userNotNull);

        User res = userService.adduser(userNotNull);

        assertEquals(userNotNull, res);
    }

    @Test
    public void testDeleteUser(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.empty();
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        User res = userService.deleteUser(userNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testDeleteUser2(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        User res = userService.deleteUser(userNotNull.getId());

        assertEquals(userNotNull, res);
    }

    @Test
    public void testEditUser(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.empty();
        Mockito.when(userRepository.findByEmailAndLoginName(userNotNull.getEmail(), userNotNull.getLoginName())).thenReturn(oUser);

        User res = userService.editUser(userNotNull);

        assertEquals(null, res);
    }

    @Test
    public void testEditUser2(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findByEmailAndLoginName(userNotNull.getEmail(), userNotNull.getLoginName())).thenReturn(oUser);
        Mockito.when(userRepository.save(userNotNull)).thenReturn(userNotNull);

        User res = userService.editUser(userNotNull);

        assertEquals(userNotNull, res);
    }

    @Test
    public void testLogin(){
        TestGlobalMocks.fill();

        Mockito.when(userRepository.save(authenticatedUser.getUser())).thenReturn(userNotNull);

        User res = userService.login();

        assertEquals(userNotNull, res);
    }

    @Test
    public void testEasyLogIn(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.empty();
        Mockito.when(userRepository.findByLoginName(userNotNull.getLoginName())).thenReturn(oUser);

        User res = userService.easyLogIn(userNotNull, activityGroupNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testEasyLogIn2(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findByLoginName(userNotNull.getLoginName())).thenReturn(oUser);
        Optional<ActivityGroup> oActivityGroup = Optional.empty();
        Mockito.when(activityGroupRepository.findById(activityGroupNotNull.getId())).thenReturn(oActivityGroup);

        User res = userService.easyLogIn(userNotNull, activityGroupNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testEasyLogIn3(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findByLoginName(userNotNull.getLoginName())).thenReturn(oUser);
        Optional<ActivityGroup> oActivityGroup = Optional.of(activityGroupNotNull);
        Mockito.when(activityGroupRepository.findById(activityGroupNotNull.getId())).thenReturn(oActivityGroup);

        User res = userService.easyLogIn(userNotNull, activityGroupNotNull.getId());

        assertEquals(userNotNull, res);
    }

}
