package hu.ELTE.Szakdolgozat.Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.HolidayService;
import hu.ELTE.Szakdolgozat.Util.TestGlobalMocks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = HolidayController.class)
public class TestHolidayController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HolidayService holidayService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private Holiday holidayNotNull;
    private Holiday holidayNotNull2;

    private User userNotNull;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        holidayNotNull = TestGlobalMocks.holidayNotNull;
        holidayNotNull2 = TestGlobalMocks.holidayNotNull2;

        userNotNull = TestGlobalMocks.userNotNull;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testGetUsersHolidayReturnOk() throws Exception{
        List<Holiday> iHoliday = new ArrayList<>();
        iHoliday.add(holidayNotNull);
        doReturn(iHoliday).when(holidayService).getHolidayByUser();
        this.mvc.perform(get("/holiday/me").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_HOLIDAY_ADMIN", "ROLE_LISTING"})
    public void testHolidayYearsReturnOk() throws Exception{
        List<Integer> years = new ArrayList<>();
        years.add(2020);
        years.add(2019);
        doReturn(years).when(holidayService).getYears();
        this.mvc.perform(get("/holiday/year").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_HOLIDAY_ADMIN"})
    public void testAddHolidayReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(holidayNotNull);
        doReturn(holidayNotNull).when(holidayService).addNewHolidayByUserId(Mockito.any(Integer.class), Mockito.any(Holiday.class));
        this.mvc.perform(post("/holiday/add/user/1").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_HOLIDAY_ADMIN"})
    public void testAddHolidayReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(holidayNotNull);
        doReturn(null).when(holidayService).addNewHolidayByUserId(Mockito.any(Integer.class), Mockito.any(Holiday.class));
        this.mvc.perform(post("/holiday/add/user/1").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_HOLIDAY_ADMIN"})
    public void testHolidayDeleteReturnOk() throws Exception{
        doReturn(holidayNotNull).when(holidayService).deleteHoliday(Mockito.any(Integer.class));
        this.mvc.perform(delete("/holiday/delete/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_HOLIDAY_ADMIN"})
    public void testHolidayDeleteReturnBadRequest() throws Exception{
        doReturn(null).when(holidayService).deleteHoliday(Mockito.any(Integer.class));
        this.mvc.perform(delete("/holiday/delete/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_LISTING"})
    public void testGetAllHolidaysReturnOk() throws Exception{
        List<Holiday> iHoliday = new ArrayList<>();
        iHoliday.add(holidayNotNull);
        iHoliday.add(holidayNotNull2);
        Iterable<Holiday> res = iHoliday;
        doReturn(res).when(holidayService).getHolidays();
        this.mvc.perform(get("/holiday").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

}
