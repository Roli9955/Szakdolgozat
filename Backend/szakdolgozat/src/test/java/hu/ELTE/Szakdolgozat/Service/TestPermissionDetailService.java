package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Repository.PermissionDetailRepository;
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

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestPermissionDetailService {

    @Mock
    private PermissionDetailRepository permissionDetailRepository;

    @InjectMocks
    private PermissionDetailService permissionDetailService;

    private PermissionDetail permissionDetailNotNull;
    private PermissionDetail permissionDetailNotNull2;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        permissionDetailNotNull = TestGlobalMocks.permissionDetailNotNull;
        permissionDetailNotNull2 = TestGlobalMocks.permissionDetailNotNull2;

    }

    @Test
    public void testGetAll(){
        TestGlobalMocks.fill();

        List<PermissionDetail> list = new ArrayList<>();
        list.add(permissionDetailNotNull);
        list.add(permissionDetailNotNull2);

        Mockito.when(permissionDetailRepository.findAll()).thenReturn(list);

        Iterable<PermissionDetail> res = permissionDetailService.getAll();

        assertEquals(list, res);
    }
}
