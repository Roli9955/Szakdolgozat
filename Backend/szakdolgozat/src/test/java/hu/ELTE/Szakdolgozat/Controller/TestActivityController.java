package hu.ELTE.Szakdolgozat.Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.ActivityService;
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

import static org.mockito.Mockito.doReturn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ActivityController.class)
public class TestActivityController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ActivityService activityService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private Activity activityNotNull;
    private Activity activityNotNull2;
    private Activity activityNotNull3;

    private Activity task;
    private Activity task2;
    private Activity task3;

    private User userNotNull;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        activityNotNull = TestGlobalMocks.activityNotNull;
        activityNotNull2 = TestGlobalMocks.activityNotNull2;
        activityNotNull3 = TestGlobalMocks.activityNotNull3;

        task = TestGlobalMocks.task;
        task2 = TestGlobalMocks.task2;
        task3 = TestGlobalMocks.task3;

        userNotNull = TestGlobalMocks.userNotNull;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testGetActivityReturnOkWithNull() throws Exception{
        Iterable<Activity> iActivity = null;
        doReturn(iActivity).when(activityService).getActivitiesByDate(2020, 04, 26);
        this.mvc.perform(get("/activity/year/2020/month/4/day/25").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testGetActivityReturnOk() throws Exception{
        List<Activity> tmp = new ArrayList<>();
        tmp.add(activityNotNull);
        Iterable<Activity> iActivity = tmp;
        doReturn(iActivity).when(activityService).getActivitiesByDate(2020, 04, 25);
        this.mvc.perform(get("/activity/year/2020/month/4/day/25").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testGetAllActivityReturnOk() throws Exception{
        List<Activity> tmp = new ArrayList<>();
        tmp.add(activityNotNull);
        tmp.add(activityNotNull2);
        Iterable<Activity> iActivity = tmp;
        doReturn(iActivity).when(activityService).getAllActivity();
        this.mvc.perform(get("/activity/year/2020/month/4/day/25").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testPostActivityReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityNotNull);
        doReturn(activityNotNull).when(activityService).addNewActivity(Mockito.any(Activity.class));
        this.mvc.perform(post("/activity/new").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testDeleteActivityReturnOk() throws Exception{
        doReturn(activityNotNull).when(activityService).deleteActivity(activityNotNull.getId());
        this.mvc.perform(delete("/activity/delete/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testDeleteActivityReturnBadRequest() throws Exception{
        doReturn(activityNotNull).when(activityService).deleteActivity(activityNotNull.getId());
        this.mvc.perform(delete("/activity/delete/2").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testEditActivityReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityNotNull);
        doReturn(activityNotNull3).when(activityService).editActivity(Mockito.any(Activity.class));
        this.mvc.perform(put("/activity/edit").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testGetMyTaskReturnOk() throws Exception{
        List<Activity> db = new ArrayList<>();
        db.add(task);
        db.add(task2);
        db.add(task3);
        Iterable<Activity> iActivity = db;
        doReturn(iActivity).when(activityService).getOwnTasks();
        this.mvc.perform(get("/activity/task/me").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testCompleteATaskReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(task);
        doReturn(task).when(activityService).completeActivity(Mockito.any(Activity.class));
        this.mvc.perform(put("/activity/task/complete").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {})
    public void testCompleteATaskReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(task2);
        doReturn(task).when(activityService).completeActivity(task2);
        this.mvc.perform(put("/activity/task/complete").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testDeleteATaskReturnOk() throws Exception{
        doReturn(task).when(activityService).deleteTask(task.getId());
        this.mvc.perform(delete("/activity/delete/task/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testDeleteATaskReturnBadRequest() throws Exception{
        doReturn(task).when(activityService).deleteTask(task.getId());
        this.mvc.perform(delete("/activity/delete/task/2").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER", "ROLE_ADD_TASK"})
    public void testPostTaskReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(task);
        doReturn(task).when(activityService).addTask(Mockito.any(Activity.class));
        this.mvc.perform(post("/activity/add/task").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

}
