package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Class.Result;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.ActivityGroupRepository;
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
public class TestActivityGroupService {

    @Mock
    private ActivityGroupRepository activityGroupRepository;

    @Mock
    private WorkGroupRepository workGroupRepository;

    @InjectMocks
    private ActivityGroupService activityGroupService;

    private User userNotNull;

    private ActivityGroup activityGroupNotNull;
    private ActivityGroup activityGroupNotNull2;
    private ActivityGroup activityGroupNotNull3;

    private WorkGroup workGroupNotNull;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;

        activityGroupNotNull = TestGlobalMocks.activityGroupNotNull;
        activityGroupNotNull2 = TestGlobalMocks.activityGroupNotNull2;
        activityGroupNotNull3 = TestGlobalMocks.activityGroupNotNull3;

        workGroupNotNull = TestGlobalMocks.workGroupNotNull;

    }

    @Test
    public void testGetAllActivityGroup(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        list.add(activityGroupNotNull2);

        Mockito.when(activityGroupRepository.findByDeletedFalse()).thenReturn(list);

        Iterable<ActivityGroup> res = activityGroupService.getAllActivityGroup();

        assertEquals(res, list);
    }

    @Test
    public void testGetAllActivityGroupsWithOutEasyLogIn(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        list.add(activityGroupNotNull2);

        Mockito.when(activityGroupRepository.findByDeletedFalseAndEasyLogInFalse()).thenReturn(list);

        Iterable<ActivityGroup> res = activityGroupService.getAllActivityGroupsWithOutEasyLogIn();

        assertEquals(res, list);
    }

    @Test
    public void testGetAllEasyLogInActivityGroups(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        list.add(activityGroupNotNull2);

        Mockito.when(activityGroupRepository.findByDeletedFalseAndEasyLogInTrue()).thenReturn(list);

        Iterable<ActivityGroup> res = activityGroupService.getAllEasyLogInActivityGroups();

        assertEquals(res, list);
    }

    @Test
    public void testAddNewActivityGroupForEasyLogIn(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        Mockito.when(activityGroupRepository.findByNameAndDeletedFalseAndEasyLogInTrue(activityGroupNotNull.getName())).thenReturn(list);

        Result<ActivityGroup, String> fvres = activityGroupService.addNewActivityGroupForEasyLogIn(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "Ilyen néven már létezik egyszerűsített bejelentkezési feladat");

        assertEquals(fvres.getRes1(), res.getRes1());
        assertEquals(fvres.getRes2(), res.getRes2());
    }

    @Test
    public void testAddNewActivityGroupForEasyLogIn2(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        Mockito.when(activityGroupRepository.findByNameAndDeletedFalseAndEasyLogInTrue(activityGroupNotNull.getName())).thenReturn(list);
        Mockito.when(activityGroupRepository.save(activityGroupNotNull)).thenReturn(activityGroupNotNull);

        Result<ActivityGroup, String> fvres = activityGroupService.addNewActivityGroupForEasyLogIn(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(activityGroupNotNull, "");

        assertEquals(fvres.getRes1(), res.getRes1());
        assertEquals(fvres.getRes2(), res.getRes2());
    }

    @Test
    public void testaddNewActivityGroup(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        Mockito.when(activityGroupRepository.findByNameAndDeletedFalseAndEasyLogInFalse(activityGroupNotNull.getName())).thenReturn(list);
        Mockito.when(activityGroupRepository.save(activityGroupNotNull)).thenReturn(activityGroupNotNull);

        Result<ActivityGroup, String> fvres = activityGroupService.addNewActivityGroup(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(activityGroupNotNull, "");

        assertEquals(fvres.getRes1(), res.getRes1());
        assertEquals(fvres.getRes2(), res.getRes2());
    }

    @Test
    public void testaddNewActivityGroup2(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        activityGroupNotNull.setParent(null);
        Mockito.when(activityGroupRepository.findByNameAndDeletedFalseAndEasyLogInFalse(activityGroupNotNull.getName())).thenReturn(list);

        Result<ActivityGroup, String> fvres = activityGroupService.addNewActivityGroup(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "Ilyen néven már létezik feladat");

        assertEquals(fvres.getRes1(), res.getRes1());
        assertEquals(fvres.getRes2(), res.getRes2());
    }

    @Test
    public void testaddNewActivityGroup3(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        activityGroupNotNull.setParent(activityGroupNotNull2);
        Mockito.when(activityGroupRepository.findByNameAndDeletedFalseAndEasyLogInFalse(activityGroupNotNull.getName())).thenReturn(list);

        Optional<ActivityGroup> oParent = Optional.empty();
        Mockito.when(activityGroupRepository.findByIdAndDeletedFalse(activityGroupNotNull.getParent().getId())).thenReturn(oParent);

        Result<ActivityGroup, String> fvres = activityGroupService.addNewActivityGroup(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "Ilyen néven már létezik feladat és nem megfelelő szülő");

        assertEquals(fvres.getRes1(), res.getRes1());
        assertEquals(fvres.getRes2(), res.getRes2());
    }

    @Test
    public void testaddNewActivityGroup4(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        activityGroupNotNull.setParent(activityGroupNotNull2);
        Mockito.when(activityGroupRepository.findByNameAndDeletedFalseAndEasyLogInFalse(activityGroupNotNull.getName())).thenReturn(list);

        Optional<ActivityGroup> oParent = Optional.of(activityGroupNotNull2);
        activityGroupNotNull2.setCanAddChild(false);
        Mockito.when(activityGroupRepository.findByIdAndDeletedFalse(activityGroupNotNull.getParent().getId())).thenReturn(oParent);

        Result<ActivityGroup, String> fvres = activityGroupService.addNewActivityGroup(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "A szülő nem bontható");

        assertEquals(fvres.getRes1(), res.getRes1());
        assertEquals(fvres.getRes2(), res.getRes2());
    }

    @Test
    public void testaddNewActivityGroup5(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        list.add(activityGroupNotNull3);
        activityGroupNotNull.setParent(activityGroupNotNull2);
        activityGroupNotNull3.setParent(activityGroupNotNull2);
        Mockito.when(activityGroupRepository.findByNameAndDeletedFalseAndEasyLogInFalse(activityGroupNotNull.getName())).thenReturn(list);

        Optional<ActivityGroup> oParent = Optional.of(activityGroupNotNull2);
        activityGroupNotNull2.setCanAddChild(true);
        Mockito.when(activityGroupRepository.findByIdAndDeletedFalse(activityGroupNotNull.getParent().getId())).thenReturn(oParent);

        Result<ActivityGroup, String> fvres = activityGroupService.addNewActivityGroup(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "Ilyen néven már létezik feladat a kiválasztott szülő alatt");

        assertEquals(fvres.getRes1(), res.getRes1());
        assertEquals(fvres.getRes2(), res.getRes2());
    }
    @Test
    public void testaddNewActivityGroup6(){
        TestGlobalMocks.fill();

        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull3);
        activityGroupNotNull.setParent(activityGroupNotNull2);
        Mockito.when(activityGroupRepository.findByNameAndDeletedFalseAndEasyLogInFalse(activityGroupNotNull.getName())).thenReturn(list);
        Mockito.when(activityGroupRepository.save(activityGroupNotNull)).thenReturn(activityGroupNotNull);

        Optional<ActivityGroup> oParent = Optional.of(activityGroupNotNull2);
        activityGroupNotNull2.setCanAddChild(true);
        Mockito.when(activityGroupRepository.findByIdAndDeletedFalse(activityGroupNotNull.getParent().getId())).thenReturn(oParent);

        Result<ActivityGroup, String> fvres = activityGroupService.addNewActivityGroup(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(activityGroupNotNull, "");

        assertEquals(fvres.getRes1(), res.getRes1());
        assertEquals(fvres.getRes2(), res.getRes2());
    }

    @Test
    public void testEditWorkGroupActivityGroups(){
        TestGlobalMocks.fill();

        Optional<WorkGroup> oWorkGroup = Optional.empty();
        Mockito.when(workGroupRepository.findByIdAndDeletedFalse(workGroupNotNull.getId())).thenReturn(oWorkGroup);

        WorkGroup res = activityGroupService.editWorkGroupActivityGroups(workGroupNotNull);

        assertEquals(res, null);
    }
}
