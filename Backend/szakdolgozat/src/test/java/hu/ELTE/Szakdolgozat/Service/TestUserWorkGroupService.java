package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.Repository.UserWorkGroupRepository;
import hu.ELTE.Szakdolgozat.Util.TestGlobalMocks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestUserWorkGroupService {

    @Mock
    private UserWorkGroupRepository userWorkGroupRepository;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserWorkGroupService userWorkGroupService;

    private User userNotNull;

    private WorkGroup workGroupNotNull;
    private WorkGroup workGroupNotNull2;

    private UserWorkGroup userWorkGroupNotNull;
    private UserWorkGroup userWorkGroupNotNull2;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;

        workGroupNotNull = TestGlobalMocks.workGroupNotNull;
        workGroupNotNull2 = TestGlobalMocks.workGroupNotNull2;

        userWorkGroupNotNull = TestGlobalMocks.userWorkGroupNotNull;
        userWorkGroupNotNull2 = TestGlobalMocks.userWorkGroupNotNull2;

        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
    }

    @Test
    public void testGetUserWorkGroupByDate(){
        TestGlobalMocks.fill();

        List<UserWorkGroup> list = new ArrayList<>();
        list.add(userWorkGroupNotNull);
        Mockito.when(userWorkGroupRepository.findByUser(authenticatedUser.getUser())).thenReturn(list);

        Iterable<WorkGroup> fvres = userWorkGroupService.getUserWorkGroupByDate(2020, 4, 1);
        List<WorkGroup> res = new ArrayList<>();
        res.add(workGroupNotNull);

        assertEquals(res, fvres);
    }

    @Test
    public void testGetWorkGroupByUser(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.empty();
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        Iterable<UserWorkGroup> res = userWorkGroupService.getWorkGroupyByUser(userNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testGetWorkGroupByUser2(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        List<UserWorkGroup> res = new ArrayList<>();
        res.add(userWorkGroupNotNull);
        res.add(userWorkGroupNotNull2);
        Mockito.when(userWorkGroupRepository.findByUser(userNotNull)).thenReturn(res);

        Iterable<UserWorkGroup> fvRes = userWorkGroupService.getWorkGroupyByUser(userNotNull.getId());

        assertEquals(res, fvRes);
    }
}
