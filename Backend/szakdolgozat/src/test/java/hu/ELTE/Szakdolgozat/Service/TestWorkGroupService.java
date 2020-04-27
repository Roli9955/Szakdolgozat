package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.Repository.UserWorkGroupRepository;
import hu.ELTE.Szakdolgozat.Repository.WorkGroupRepository;
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
public class TestWorkGroupService {

    @Mock
    private WorkGroupRepository workGroupRepository;

    @Mock
    private UserWorkGroupRepository userWorkGroupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkGroupService workGroupService;

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
    }

    @Test
    public void testGetWorkGroups(){
        TestGlobalMocks.fill();

        List<WorkGroup> list = new ArrayList<>();
        list.add(workGroupNotNull);
        workGroupNotNull.setUserWorkGroup(new ArrayList<>());
        workGroupNotNull.setActivityGroup(new ArrayList<>());
        list.add(workGroupNotNull2);
        workGroupNotNull2.setUserWorkGroup(new ArrayList<>());
        workGroupNotNull2.setActivityGroup(new ArrayList<>());

        Mockito.when(workGroupRepository.findByDeletedFalse()).thenReturn(list);

        Iterable<WorkGroup> res = workGroupService.getWorkGroups();

        assertEquals(list, res);
    }

    @Test
    public void testAddNewWorkGroup(){
        TestGlobalMocks.fill();

        Mockito.when(workGroupRepository.save(workGroupNotNull)).thenReturn(workGroupNotNull);

        WorkGroup res = workGroupService.addNewWorkGroup(workGroupNotNull);

        assertEquals(workGroupNotNull, res);
    }

    @Test
    public void testAddUserWorkGroups(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.empty();
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);

        List<UserWorkGroup> list = new ArrayList<>();

        Iterable<UserWorkGroup> res = workGroupService.addUserWorkGroups(workGroupNotNull.getId(), list);

        assertEquals(null, res);
    }

    @Test
    public void testAddUserWorkGroups2(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.of(workGroupNotNull);
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);
        Optional<User> oUser = Optional.empty();
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        List<UserWorkGroup> list = new ArrayList<>();
        list.add(userWorkGroupNotNull);

        Iterable<UserWorkGroup> res = workGroupService.addUserWorkGroups(workGroupNotNull.getId(), list);

        assertEquals(null, res);
    }

    @Test
    public void testAddUserWorkGroups3(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.of(workGroupNotNull);
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);
        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        List<UserWorkGroup> list = new ArrayList<>();
        list.add(userWorkGroupNotNull);

        Mockito.when(userWorkGroupRepository.saveAll(list)).thenReturn(list);

        Iterable<UserWorkGroup> fvRes = workGroupService.addUserWorkGroups(workGroupNotNull.getId(), list);

        assertEquals(list, fvRes);
    }

    @Test
    public void testGetUsersInWorkGroup(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.empty();
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);

        List<UserWorkGroup> res = workGroupService.getUsersInWorkGroup(workGroupNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testGetUsersInWorkGroup2(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.of(workGroupNotNull);
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);

        List<UserWorkGroup> res = new ArrayList<>();
        res.add(userWorkGroupNotNull);
        workGroupNotNull.setUserWorkGroup(res);

        List<UserWorkGroup> fvRes = workGroupService.getUsersInWorkGroup(workGroupNotNull.getId());

        assertEquals(res, fvRes);
    }

    @Test
    public void testDeleteWorkGroup(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.empty();
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);

        WorkGroup res = workGroupService.deleteWorkGroup(workGroupNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testDeleteWorkGroup2(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.of(workGroupNotNull);
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);
        Mockito.when(workGroupRepository.save(workGroupNotNull)).thenReturn(workGroupNotNull);

        WorkGroup res = workGroupService.deleteWorkGroup(workGroupNotNull.getId());

        assertEquals(workGroupNotNull, res);
    }

    @Test
    public void testEditWorkGroup(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.empty();
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);

        WorkGroup res = workGroupService.editWorkGroup(workGroupNotNull);

        assertEquals(null, res);
    }

    @Test
    public void testEditWorkGroup2(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.of(workGroupNotNull);
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);
        Mockito.when(workGroupRepository.save(workGroupNotNull)).thenReturn(workGroupNotNull);

        WorkGroup res = workGroupService.editWorkGroup(workGroupNotNull);

        assertEquals(workGroupNotNull, res);
    }

    @Test
    public void testEditUserWorkGroups(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.empty();
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);

        List<UserWorkGroup> list = new ArrayList<>();
        list.add(userWorkGroupNotNull);
        List<UserWorkGroup> res = workGroupService.editUserWorkGroups(workGroupNotNull.getId(), list);

        assertEquals(null, res);
    }

    @Test
    public void testEditUserWorkGroups2(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.of(workGroupNotNull);
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);
        Mockito.when(userWorkGroupRepository.save(userWorkGroupNotNull)).thenReturn(userWorkGroupNotNull);
        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        List<UserWorkGroup> list = new ArrayList<>();
        list.add(userWorkGroupNotNull);
        workGroupNotNull.setUserWorkGroup(list);
        List<UserWorkGroup> res = workGroupService.editUserWorkGroups(workGroupNotNull.getId(), list);

        assertEquals(list, res);
    }
}
