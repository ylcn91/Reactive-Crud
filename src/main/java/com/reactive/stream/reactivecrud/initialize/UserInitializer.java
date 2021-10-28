package com.reactive.stream.reactivecrud.initialize;
import com.reactive.stream.reactivecrud.model.Department;
import com.reactive.stream.reactivecrud.model.User;
import com.reactive.stream.reactivecrud.repository.DepartmentRepository;
import com.reactive.stream.reactivecrud.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class UserInitializer implements CommandLineRunner {


    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public UserInitializer(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void run(String... args) {
        initialDataSetup();
    }

    private List<User> getData(){
        return Arrays.asList(new User(null,"Yalcin Doksanbir",30,10000),
                new User(null,"Nil Dora",5,1000),
                new User(null,"Alp Demir",40,1000000));
    }

    private List<Department> getDepartments(){
        return Arrays.asList(new Department(null,"Mechanical",1,"Istanbul"),
                new Department(null,"Computer",2,"Istanbul"));
    }

    private void initialDataSetup() {
        userRepository.deleteAll()
                .thenMany(Flux.fromIterable(getData()))
                .flatMap(userRepository::save)
                .thenMany(userRepository.findAll())
                .subscribe(user -> {
                    log.info("User Inserted from CommandLineRunner " + user);
                });

        departmentRepository.deleteAll()
                .thenMany(Flux.fromIterable(getDepartments()))
                .flatMap(departmentRepository::save)
                .thenMany(departmentRepository.findAll())
                .subscribe(user -> {
                    log.info("Department Inserted from CommandLineRunner " + user);
                });

    }

}
