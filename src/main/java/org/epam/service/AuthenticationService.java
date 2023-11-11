package org.epam.service;

import org.epam.model.User;

public interface AuthenticationService {

    boolean checkAccess(String password, User user);
}
