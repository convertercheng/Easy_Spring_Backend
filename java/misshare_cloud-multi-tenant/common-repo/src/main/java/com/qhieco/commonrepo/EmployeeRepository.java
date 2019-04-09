package com.qhieco.commonrepo;

import com.qhieco.commonentity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findById(Integer id);

    void deleteById(Integer id);
}
