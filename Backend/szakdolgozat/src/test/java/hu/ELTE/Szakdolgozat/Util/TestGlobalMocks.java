package hu.ELTE.Szakdolgozat.Util;

import hu.ELTE.Szakdolgozat.Entity.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TestGlobalMocks {

    public static Activity activityNotNull;
    public static Activity activityNotNull2;
    public static Activity activityNotNull3;

    public static Activity task;
    public static Activity task2;
    public static Activity task3;

    public static User userNotNull;
    public static User userNotNull2;

    public static ActivityGroup activityGroupNotNull;
    public static ActivityGroup activityGroupNotNull2;
    public static ActivityGroup activityGroupNotNull3;

    public static ActivityGroup activityGroupNoEasyLoginNotNull;
    public static ActivityGroup activityGroupNoEasyLoginNotNull2;

    public static WorkGroup workGroupNotNull;
    public static WorkGroup workGroupNotNull2;

    public static Permission permissionNotNull;
    public static Permission permissionNotNull2;

    public static PermissionDetail permissionDetailNotNull;
    public static PermissionDetail permissionDetailNotNull2;

    public static Holiday holidayNotNull;
    public static Holiday holidayNotNull2;

    public static UserWorkGroup userWorkGroupNotNull;
    public static UserWorkGroup userWorkGroupNotNull2;

    private static List<ActivityGroup> listActivityGroup = new ArrayList<>();

    static {
        fill();
    }

    public static void fill(){
        permissionNotNull = maketTestPermission(1, "Adminisztráció");
        permissionNotNull2 = maketTestPermission(2, "Alkalmazott");

        permissionDetailNotNull = makeTestPermissionDetail(1, "Felhasználó", "ROLE_USER");
        permissionDetailNotNull2 = makeTestPermissionDetail(2, "Karbantartás", "ROLE_MAINTENANCE");

        workGroupNotNull = makeTestWorkGroup(1, "Fejlesztés", 100, false);
        workGroupNotNull2 = makeTestWorkGroup(2, "Tervezés", 100, false);

        activityGroupNotNull = makeTestActivityGroup(1, "Tervezés", null, true, false, false);
        activityGroupNotNull2 = makeTestActivityGroup(2, "Tervezés", null, true, false, false);
        activityGroupNotNull3 = makeTestActivityGroup(3, "Tervezés2", null, true, false, false);

        activityGroupNoEasyLoginNotNull = makeTestActivityGroup(3, "Munka kezdése", null, false, true, false);
        activityGroupNoEasyLoginNotNull2 = makeTestActivityGroup(3, "Munka kezdése", null, false, true, false);

        userNotNull = makeTestUser(1, "Roland", "Kotroczó", "k.roli9955@gmail.com", "admin", "admin", new java.util.Date(), 20, true, false, permissionNotNull);
        userNotNull2 = makeTestUser(2, "Pista", "Kiss", "teszt@gmail.com", "user", "admin", new java.util.Date(), 25, true, false, permissionNotNull2);

        activityNotNull = makeTestActivity(1, "Teszt üzenet", false, 90, Date.valueOf("2020-04-25"), userNotNull, userNotNull, activityGroupNotNull, workGroupNotNull, false, null);
        activityNotNull2 = makeTestActivity(2, "Teszt üzenet2", false, 90, Date.valueOf("2020-04-25"), userNotNull, userNotNull, activityGroupNotNull, workGroupNotNull, false, null);
        activityNotNull3 = makeTestActivity(3, "Teszt üzenet3", false, 90, Date.valueOf("2020-04-20"), userNotNull, userNotNull, activityGroupNotNull, workGroupNotNull, false, null);

        task = makeTestActivity(1, "Teszt üzenet", true, null, Date.valueOf("2020-04-25"), userNotNull, userNotNull, null, null, false, Date.valueOf("2020-04-25"));
        task2 = makeTestActivity(2, "Teszt üzenet2", true, null, Date.valueOf("2020-04-25"), userNotNull, userNotNull, null, null, true, Date.valueOf("2020-04-25"));
        task3 = makeTestActivity(3, "Teszt üzenet3", true, null, Date.valueOf("2020-04-22"), userNotNull2, userNotNull2, null, null, false, Date.valueOf("2020-04-22"));

        holidayNotNull = makeTestHoliday(1, Date.valueOf("2020-04-20"), Date.valueOf("2020-04-25"), 6, userNotNull);
        holidayNotNull2 = makeTestHoliday(2, Date.valueOf("2020-05-20"), Date.valueOf("2020-05-25"), 6, userNotNull);

        userWorkGroupNotNull = makeTestUserWorkGroup(1, Date.valueOf("2020-03-01"), Date.valueOf("2020-04-30"), workGroupNotNull, userNotNull);
        userWorkGroupNotNull2 = makeTestUserWorkGroup(2, Date.valueOf("2020-03-01"), Date.valueOf("2020-04-30"), workGroupNotNull, userNotNull2);

        listActivityGroup.add(activityGroupNotNull);
        listActivityGroup.add(activityGroupNotNull2);

        setActivityWorkGroup(workGroupNotNull, listActivityGroup);
    }

    private static Activity makeTestActivity(Integer id, String description, Boolean isTask, Integer min, Date date, User user, User owner, ActivityGroup activityGroup, WorkGroup workGroup, Boolean isCompleted, Date deadline){
        Activity activity = new Activity();

        activity.setId(id);
        activity.setDescription(description);
        activity.setIsTask(isTask);
        activity.setMin(min);
        activity.setDate(date);
        activity.setUser(user);
        activity.setOwner(owner);
        activity.setActivityGroup(activityGroup);
        activity.setWorkGroup(workGroup);
        activity.setIsCompleted(isCompleted);
        activity.setDeadline(deadline);

        return activity;
    }

    private static User makeTestUser(Integer id, String firstName, String lastName, String email, String loginName, String password, java.util.Date lastLogin, Integer maxHoliday, Boolean canLogIn, Boolean deleted, Permission permission){
        User user = new User();

        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setLoginName(loginName);
        user.setPassword(password);
        user.setLastLogin(lastLogin);
        user.setMaxHoliday(maxHoliday);
        user.setCanLogIn(canLogIn);
        user.setDeleted(deleted);
        user.setPermission(permission);

        return user;
    }

    private static ActivityGroup makeTestActivityGroup(Integer id, String name, ActivityGroup parent, Boolean canAddChild, Boolean easyLogIn, Boolean deleted){
        ActivityGroup activityGroup = new ActivityGroup();

        activityGroup.setId(id);
        activityGroup.setName(name);
        activityGroup.setParent(parent);
        activityGroup.setCanAddChild(canAddChild);
        activityGroup.setEasyLogIn(easyLogIn);
        activityGroup.setDeleted(deleted);

        return activityGroup;
    }

    private static WorkGroup makeTestWorkGroup(Integer id, String name, Integer scale, Boolean deleted){
        WorkGroup workGroup = new WorkGroup();

        workGroup.setId(id);
        workGroup.setName(name);
        workGroup.setScale(scale);
        workGroup.setDeleted(deleted);

        return workGroup;
    }

    private static void setActivityWorkGroup(WorkGroup workGroup, List<ActivityGroup> lActivityGroup){
        workGroup.setActivityGroup(lActivityGroup);
    }

    private static Permission maketTestPermission(Integer id, String name){
        Permission permission = new Permission();

        permission.setId(id);
        permission.setName(name);

        return permission;
    }


    private static Holiday makeTestHoliday(Integer id, Date from, Date to, Integer days, User user){
        Holiday holiday = new Holiday();

        holiday.setId(id);
        holiday.setHolidayFrom(from);
        holiday.setHolidayTo(to);
        holiday.setDays(days);
        holiday.setUser(user);

        return holiday;
    }

    private static PermissionDetail makeTestPermissionDetail(Integer id, String name, String roleTag){
        PermissionDetail permissionDetail = new PermissionDetail();

        permissionDetail.setId(id);
        permissionDetail.setName(name);
        permissionDetail.setRoleTag(roleTag);

        return permissionDetail;
    }

    private static  UserWorkGroup makeTestUserWorkGroup(Integer id, Date from, Date to, WorkGroup workGroup, User user){
        UserWorkGroup userWorkGroup = new UserWorkGroup();

        userWorkGroup.setId(id);
        userWorkGroup.setInWorkGroupFrom(from);
        userWorkGroup.setInWorkGroupTo(to);
        userWorkGroup.setWorkGroup(workGroup);
        userWorkGroup.setUser(user);

        return userWorkGroup;
    }
}
