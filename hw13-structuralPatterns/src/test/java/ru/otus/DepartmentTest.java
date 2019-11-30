package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.SimpleATM;
import ru.otus.department.Department;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentTest {
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        initDepartment();
    }

    @Test
    void getTotal() {
        assertEquals(720, department.getTotal().intValue());
    }

    @Test
    void resetStates() {
        department.restoreState();
        assertEquals(0, department.getTotal().intValue());
    }

    private void initDepartment() {
        var atm1 = new SimpleATM();
        var atm2 = new SimpleATM();
        var atm3 = new SimpleATM();
        var atm4 = new SimpleATM();
        atm1.saveState();
        atm2.saveState();
        atm3.saveState();
        atm4.saveState();
        atm1.insertMoney(10, 10, 10, 5, 15, 50, 100);
        atm2.insertMoney(10, 10, 10, 5, 15, 50, 100);
        atm3.insertMoney(10, 10, 10, 5, 15, 50, 100);
        atm4.insertMoney(10, 10, 10, 5, 15, 50, 100);
        department.addATM(atm1);
        department.addATM(atm2);
        department.addATM(atm3);
        department.addATM(atm4);
    }
}
