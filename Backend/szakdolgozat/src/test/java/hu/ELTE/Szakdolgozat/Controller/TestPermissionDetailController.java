package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.PermissionDetailService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PermissionDetailController.class)
public class TestPermissionDetailController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PermissionDetailService permissionDetailService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private User userNotNull;

    private PermissionDetail permissionDetailNotNull;
    private PermissionDetail permissionDetailNotNull2;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        permissionDetailNotNull = TestGlobalMocks.permissionDetailNotNull;
        permissionDetailNotNull2 = TestGlobalMocks.permissionDetailNotNull2;

        userNotNull = TestGlobalMocks.userNotNull;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {})
    public void testGetAllPermissionDetailsReturnOk() throws Exception{
        List<PermissionDetail> iPermissionDetails = new ArrayList<>();
        iPermissionDetails.add(permissionDetailNotNull);
        iPermissionDetails.add(permissionDetailNotNull2);
        Iterable<PermissionDetail> res = iPermissionDetails;
        doReturn(res).when(permissionDetailService).getAll();
        this.mvc.perform(get("/permission-detail").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {})
    public void testGetAllPermissionDetailsReturnOk2() throws Exception{
        List<PermissionDetail> iPermissionDetails = new ArrayList<>();
        Iterable<PermissionDetail> res = iPermissionDetails;
        doReturn(res).when(permissionDetailService).getAll();
        this.mvc.perform(get("/permission-detail").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

}
