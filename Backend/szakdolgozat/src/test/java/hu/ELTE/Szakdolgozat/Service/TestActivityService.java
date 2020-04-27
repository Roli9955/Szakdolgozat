package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.ActivityRepository;
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TestActivityService {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserWorkGroupRepository userWorkGroupRepository;

    @Mock
    private WorkGroupRepository workGroupRepository;

    @InjectMocks
    private ActivityService activityService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private UserRepository userRepository;


    private User userNotNull;
    private User userNotNull2;

    private Activity activityNotNull;
    private Activity activityNotNull2;
    private Activity activityNotNull3;

    private Activity task;
    private Activity task2;
    private Activity task3;

    private WorkGroup workGroupNotNull;

    private UserWorkGroup userWorkGroupNotNull;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;
        userNotNull2 = TestGlobalMocks.userNotNull2;

        activityNotNull = TestGlobalMocks.activityNotNull;
        activityNotNull2 = TestGlobalMocks.activityNotNull2;
        activityNotNull3 = TestGlobalMocks.activityNotNull3;

        workGroupNotNull = TestGlobalMocks.workGroupNotNull;

        task = TestGlobalMocks.task;
        task2 = TestGlobalMocks.task2;
        task3 = TestGlobalMocks.task3;

        userWorkGroupNotNull = TestGlobalMocks.userWorkGroupNotNull;

        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);

    }

    @Test
    public void tesztGetActivitiesByDate(){
        TestGlobalMocks.fill();

        List<Activity> list = new ArrayList<>();
        list.add(activityNotNull);
        list.add(activityNotNull2);
        list.add(activityNotNull3);
        list.add(task);
        list.add(task2);
        Iterable<Activity> input = list;

        List<Activity> resultList = new ArrayList<>();
        resultList.add(activityNotNull);
        resultList.add(activityNotNull2);
        resultList.add(task2);

        Mockito.when(activityRepository.findByUser(authenticatedUser.getUser())).thenReturn(input);

        Iterable<Activity> iAcitivity = activityService.getActivitiesByDate(2020,4,25);
        Iterable<Activity> res = resultList;

        assertEquals(res, iAcitivity);
    }

    @Test
    public void testAddNewActivity(){
        TestGlobalMocks.fill();

        List<UserWorkGroup> uwg = new ArrayList<>();
        uwg.add(userWorkGroupNotNull);

        Mockito.when(userWorkGroupRepository.findByUser(authenticatedUser.getUser())).thenReturn(uwg);
        Mockito.when(activityRepository.save(activityNotNull)).thenReturn(activityNotNull);

        Activity res = activityService.addNewActivity(activityNotNull);

        assertEquals(res, activityNotNull);
    }

    @Test
    public void testAddNewActivity2(){
        TestGlobalMocks.fill();

        List<UserWorkGroup> list = new ArrayList<>();

        Mockito.when(userWorkGroupRepository.findByUser(authenticatedUser.getUser())).thenReturn(list);

        Activity res = activityService.addNewActivity(activityNotNull);

        assertEquals(res, null);
    }

    @Test
    public void testDeleteActivity(){
        Optional<Activity> oActivity = Optional.of(activityNotNull);
        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);

        Activity res = activityService.deleteActivity(activityNotNull.getId());

        assertEquals(res, activityNotNull);
    }

    @Test
    public void testDeleteActivity2(){
        Optional<Activity> oActivity = Optional.empty();
        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);

        Activity res = activityService.deleteActivity(activityNotNull.getId());

        assertEquals(res, null);
    }

    @Test
    public void testEditActivity(){
        TestGlobalMocks.fill();

        Optional<Activity> oActivity = Optional.empty();
        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);

        Activity res = activityService.editActivity(activityNotNull);

        assertEquals(res, null);
    }

    @Test
    public void testEditActivity2(){
        TestGlobalMocks.fill();

        Optional<Activity> oActivity = Optional.of(activityNotNull);
        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);

        activityNotNull.setOwner(userNotNull2);

        Activity res = activityService.editActivity(activityNotNull);

        assertEquals(res, null);
    }

    @Test
    public void testEditActivity3(){
        TestGlobalMocks.fill();

        Optional<Activity> oActivity = Optional.of(activityNotNull);
        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);
        Optional<WorkGroup> oWorkGroup = Optional.empty();
        Mockito.when(workGroupRepository.findById(activityNotNull.getId())).thenReturn(oWorkGroup);

        Activity res = activityService.editActivity(activityNotNull);

        assertEquals(res, null);
    }

    @Test
    public void testEditActivity4(){
        TestGlobalMocks.fill();

        Optional<Activity> oActivity = Optional.of(activityNotNull);
        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);
        Optional<WorkGroup> oWorkGroup = Optional.of(workGroupNotNull);
        Mockito.when(workGroupRepository.findById(activityNotNull.getId())).thenReturn(oWorkGroup);
        Mockito.when(activityRepository.save(activityNotNull)).thenReturn(activityNotNull);

        Activity res = activityService.editActivity(activityNotNull);

        assertEquals(res, activityNotNull);
    }

    @Test
    public void testGetAllActivity(){
        TestGlobalMocks.fill();

        List<Activity> list = new ArrayList<>();
        list.add(activityNotNull);
        list.add(activityNotNull2);
        list.add(activityNotNull3);
        list.add(task);
        list.add(task2);
        list.add(task3);

        Mockito.when(activityRepository.findAll()).thenReturn(list);

        Iterable<Activity> res = activityService.getAllActivity();

        assertEquals(res, list);
    }

    @Test
    public void testCompleteActivity(){
        TestGlobalMocks.fill();
        Optional<Activity> oActivity = Optional.empty();

        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);

        Activity res = activityService.completeActivity(activityNotNull);

        assertEquals(res, null);
    }

    @Test
    public void testCompleteActivity2(){
        TestGlobalMocks.fill();
        Optional<Activity> oActivity = Optional.of(activityNotNull);

        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);
        Mockito.when(activityRepository.save(activityNotNull)).thenReturn(activityNotNull);

        Activity res = activityService.completeActivity(activityNotNull);

        assertEquals(res, activityNotNull);
    }

    @Test
    public void testCompleteActivity3(){
        TestGlobalMocks.fill();

        activityNotNull.setUser(userNotNull2);
        Optional<Activity> oActivity = Optional.of(activityNotNull);

        Mockito.when(activityRepository.findById(activityNotNull.getId())).thenReturn(oActivity);

        Activity res = activityService.completeActivity(activityNotNull);

        assertEquals(res, null);
    }

    @Test
    public void testDeleteTask(){
        TestGlobalMocks.fill();
        Optional<Activity> oActivity = Optional.empty();

        Mockito.when(activityRepository.findById(task.getId())).thenReturn(oActivity);

        Activity res = activityService.deleteTask(task.getId());

        assertEquals(res, null);
    }

    @Test
    public void testDeleteTask2(){
        TestGlobalMocks.fill();
        Optional<Activity> oActivity = Optional.of(task);

        Mockito.when(activityRepository.findById(task.getId())).thenReturn(oActivity);

        Activity res = activityService.deleteTask(task.getId());

        assertEquals(res, task);
    }

    @Test
    public void testAddTask(){
        TestGlobalMocks.fill();
        Optional<User> oUser = Optional.empty();

        Mockito.when(userRepository.findById(task.getUser().getId())).thenReturn(oUser);

        Activity res = activityService.addTask(task);

        assertEquals(res, null);
    }

    @Test
    public void testAddTask2(){
        TestGlobalMocks.fill();
        Optional<User> oUser = Optional.of(userNotNull);

        Mockito.when(userRepository.findById(task.getUser().getId())).thenReturn(oUser);
        Mockito.when(activityRepository.save(task)).thenReturn(task);

        Activity res = activityService.addTask(task);

        assertEquals(res, task);
    }
}
