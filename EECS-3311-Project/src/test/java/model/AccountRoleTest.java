package model;

import static org.junit.jupiter.api.Assertions.*;

import org.openjfx.EECS_3311_Project.model.AccountRole;
import org.junit.jupiter.api.Test;


class AccountRoleTest {
    @Test
    void constructor_with_valid_params() {
        AccountRole role = new AccountRole("Manager", "R001", 25.5);

        assertEquals("R001", role.getId());
        assertEquals("Manager", role.getRoleName());
        assertEquals(25.5, role.getHourlyRate());
    }

    @Test
    void default_constructor_and_setters_with_valid_param() {
        AccountRole role = new AccountRole();

        role.setId("R002");
        role.setRoleName("Developer");
        role.setHourlyRate(30.0);

        assertEquals("R002", role.getId());
        assertEquals("Developer", role.getRoleName());
        assertEquals(30.0, role.getHourlyRate());
    }

    @Test
    void csv_constructor_with_valid_param() {
        String csv = "R003, Tester, 18.75";
        AccountRole role = new AccountRole(csv);

        assertEquals("R003", role.getId());
        assertEquals("Tester", role.getRoleName());
        assertEquals(18.75, role.getHourlyRate());
    }

    @Test
    void csv_constructor_trims_white_space() {
        String csv = "  R004  ,   Analyst   ,  40.0  ";
        AccountRole role = new AccountRole(csv);

        assertEquals("R004", role.getId());
        assertEquals("Analyst", role.getRoleName());
        assertEquals(40.0, role.getHourlyRate());
    }

    @Test
    void csv_constructor_invalid_rate() {
        assertThrows(IllegalArgumentException.class,
                () -> new AccountRole("R005, Intern, notANumber"));
    }

    @Test
    void account_role_to_csv_row() {
        AccountRole role = new AccountRole("Support", "R006", 22.5);
        String expected = "R006,Support,22.5";

        assertEquals(expected, role.toCSVRow());
    }
}
