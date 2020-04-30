package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.HolidayRepository;
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TestHolidayService {

    @InjectMocks
    private HolidayService holidayService;

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUser authenticatedUser;

    private User userNotNull;

    private Holiday holidayNotNull;
    private Holiday holidayNotNull2;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;

        holidayNotNull = TestGlobalMocks.holidayNotNull;
        holidayNotNull2 = TestGlobalMocks.holidayNotNull2;

        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
    }

    @Test
    public void testGetHolidayByUser(){
        TestGlobalMocks.fill();

        List<Holiday> list = new ArrayList<>();
        list.add(holidayNotNull);
        list.add(holidayNotNull2);

        Mockito.when(holidayRepository.findByUserAndDeletedFalse(userNotNull)).thenReturn(list);

        Iterable<Holiday> res = holidayService.getHolidayByUser();

        assertEquals(list, res);
    }

    @Test
    public void testGetYears(){
        TestGlobalMocks.fill();

        List<Integer> years = new ArrayList<>();
        years.add(2020);
        years.add(2019);

        Mockito.when(holidayRepository.getYears()).thenReturn(years);

        Iterable<Integer> res = holidayService.getYears();

        assertEquals(years, res);
    }

    @Test
    public void testAddNewHolidayByUserId(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.empty();
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        Holiday res = holidayService.addNewHolidayByUserId(userNotNull.getId(), holidayNotNull);

        assertEquals(null, res);
    }

    @Test
    public void testAddNewHolidayByUserId2(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        holidayNotNull.setHolidayFrom(null);
        holidayNotNull.setHolidayTo(null);

        Holiday res = holidayService.addNewHolidayByUserId(userNotNull.getId(), holidayNotNull);

        assertEquals(null, res);
    }

    @Test
    public void testAddNewHolidayByUserId3(){
        TestGlobalMocks.fill();

        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);
        Mockito.when(holidayRepository.save(holidayNotNull)).thenReturn(holidayNotNull);

        Holiday res = holidayService.addNewHolidayByUserId(userNotNull.getId(), holidayNotNull);

        assertEquals(holidayNotNull, res);
    }

    @Test
    public void testDeleteHoliday(){
        TestGlobalMocks.fill();

        Optional<Holiday> oHoliday = Optional.empty();
        Mockito.when(holidayRepository.findByIdAndDeletedFalse(userNotNull.getId())).thenReturn(oHoliday);

        Holiday res = holidayService.deleteHoliday(holidayNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testDeleteHoliday2(){
        TestGlobalMocks.fill();

        Optional<Holiday> oHoliday = Optional.of(holidayNotNull);
        Mockito.when(holidayRepository.findByIdAndDeletedFalse(userNotNull.getId())).thenReturn(oHoliday);

        Holiday res = holidayService.deleteHoliday(holidayNotNull.getId());
        holidayNotNull.setDeleted(true);

        assertEquals(holidayNotNull, res);
    }

    @Test
    public void testGetHolidays(){
        TestGlobalMocks.fill();

        List<Holiday> list = new ArrayList<>();
        list.add(holidayNotNull);
        list.add(holidayNotNull2);

        Mockito.when(holidayRepository.findByDeletedFalse()).thenReturn(list);

        Iterable<Holiday> res = holidayService.getHolidays();

        assertEquals(list, res);
    }

}
