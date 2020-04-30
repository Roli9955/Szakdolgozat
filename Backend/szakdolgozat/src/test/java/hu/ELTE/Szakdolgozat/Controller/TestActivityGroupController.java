package hu.ELTE.Szakdolgozat.Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Class.Result;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Service.ActivityGroupService;
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
@WebMvcTest(value = ActivityGroupController.class)
public class TestActivityGroupController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ActivityGroupService activityGroupService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private ActivityGroup activityGroupNotNull;
    private ActivityGroup activityGroupNotNull2;

    private ActivityGroup activityGroupNoEasyLoginNotNull;
    private ActivityGroup activityGroupNoEasyLoginNotNull2;

    private WorkGroup workGroupNotNull;

    private User userNotNull;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        activityGroupNotNull = TestGlobalMocks.activityGroupNotNull;
        activityGroupNotNull2 = TestGlobalMocks.activityGroupNotNull2;

        activityGroupNoEasyLoginNotNull = TestGlobalMocks.activityGroupNoEasyLoginNotNull;
        activityGroupNoEasyLoginNotNull2 = TestGlobalMocks.activityGroupNoEasyLoginNotNull2;

        userNotNull = TestGlobalMocks.userNotNull;

        workGroupNotNull = TestGlobalMocks.workGroupNotNull;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN", "ROLE_LISTING"})
    public void testGetAllActivityGroupReturnOk() throws Exception{
        List<ActivityGroup> tmp = new ArrayList<>();
        tmp.add(activityGroupNotNull);
        tmp.add(activityGroupNotNull2);
        Iterable<ActivityGroup> iActivity = tmp;
        doReturn(iActivity).when(activityGroupService).getAllActivityGroup();
        this.mvc.perform(get("/activity-group").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testGetAllActivityGroupNoEasyLogInReturnOk() throws Exception{
        List<ActivityGroup> tmp = new ArrayList<>();
        tmp.add(activityGroupNotNull);
        Iterable<ActivityGroup> iActivity = tmp;
        doReturn(iActivity).when(activityGroupService).getAllActivityGroupsWithOutEasyLogIn();
        this.mvc.perform(get("/activity-group/no/easy-log-in").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {})
    public void testGetAllActivityGroupOnlyEasyLogInReturnOk() throws Exception{
        List<ActivityGroup> tmp = new ArrayList<>();
        tmp.add(activityGroupNoEasyLoginNotNull);
        Iterable<ActivityGroup> iActivity = tmp;
        doReturn(iActivity).when(activityGroupService).getAllEasyLogInActivityGroups();
        this.mvc.perform(get("/activity-group/easy-log-in").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostActivityGroupReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(activityGroupNotNull, "");
        doReturn(res).when(activityGroupService).addNewActivityGroup(Mockito.any(ActivityGroup.class));
        this.mvc.perform(post("/activity-group/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostActivityGroupSameNameReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "Ilyen néven már létezik feladat");
        doReturn(res).when(activityGroupService).addNewActivityGroup(Mockito.any(ActivityGroup.class));
        this.mvc.perform(post("/activity-group/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostActivityGroupSameNameAndWrongParentReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "Ilyen néven már létezik feladat és nem megfelelő szülő");
        doReturn(res).when(activityGroupService).addNewActivityGroup(Mockito.any(ActivityGroup.class));
        this.mvc.perform(post("/activity-group/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostActivityGroupParentNotCanAddChildReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "A szülő nem bontható");
        doReturn(res).when(activityGroupService).addNewActivityGroup(Mockito.any(ActivityGroup.class));
        this.mvc.perform(post("/activity-group/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostActivityGroupSameNameUnderSameParentReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityGroupNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "Ilyen néven már létezik feladat a kiválasztott szülő alatt");
        doReturn(res).when(activityGroupService).addNewActivityGroup(Mockito.any(ActivityGroup.class));
        this.mvc.perform(post("/activity-group/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostActivityGroupEasyLogInReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityGroupNoEasyLoginNotNull);
        Result<ActivityGroup, String> res = new Result<>(activityGroupNoEasyLoginNotNull, "");
        doReturn(res).when(activityGroupService).addNewActivityGroupForEasyLogIn(Mockito.any(ActivityGroup.class));
        this.mvc.perform(post("/activity-group/add/easy-log-in").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostActivityGroupEasyLogInReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(activityGroupNoEasyLoginNotNull);
        Result<ActivityGroup, String> res = new Result<>(null, "Ilyen néven már létezik egyszerűsített bejelentkezési feladat");
        doReturn(res).when(activityGroupService).addNewActivityGroupForEasyLogIn(Mockito.any(ActivityGroup.class));
        this.mvc.perform(post("/activity-group/add/easy-log-in").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testDeleteActivityGroupReturnOk() throws Exception{
        List<ActivityGroup> list = new ArrayList<>();
        list.add(activityGroupNotNull);
        doReturn(list).when(activityGroupService).deleteActivityGroup(activityGroupNotNull.getId());
        this.mvc.perform(delete("/activity-group/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testDeleteActivityGroupReturnBadRequest() throws Exception{
        doReturn(null).when(activityGroupService).deleteActivityGroup(activityGroupNotNull.getId());
        this.mvc.perform(delete("/activity-group/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostEditWorkGroupReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(workGroupNotNull);
        doReturn(workGroupNotNull).when(activityGroupService).editWorkGroupActivityGroups(Mockito.any(WorkGroup.class));
        this.mvc.perform(put("/activity-group/edit/work-group").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ACTIVITY_GROUP_ADMIN"})
    public void testPostEditWorkGroupReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(workGroupNotNull);
        doReturn(null).when(activityGroupService).editWorkGroupActivityGroups(Mockito.any(WorkGroup.class));
        this.mvc.perform(put("/activity-group/edit/work-group").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
