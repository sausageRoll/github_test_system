package com.epam.testsystem.github.web.mapper;

import com.epam.testsystem.github.TimeConstant;
import com.epam.testsystem.github.model.Contact;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.UserWithTasks;
import com.epam.testsystem.github.web.model.ContactUI;
import com.epam.testsystem.github.web.model.TaskUI;
import com.epam.testsystem.github.web.model.UserUI;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * github_test
 * Created on 07.07.17.
 */

@Mapper(componentModel = "spring")
public interface MapperUi {

    @Mapping(target = "startTime", source = "registerTime", dateFormat = TimeConstant.FORMAT)
    TaskUI mapTask(Task task);

    List<TaskUI> mapTasks(List<Task> tasks);

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "gitNick", source = "user.gitNick")
    UserUI mapUser(UserWithTasks userWithTasks);

    List<UserUI> mapUsers(List<UserWithTasks> userWithTasksList);

    ContactUI mapContact(Contact contact);
    List<ContactUI> mapContacts(List<Contact> contact);
}
