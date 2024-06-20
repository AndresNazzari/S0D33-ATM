package core.Application.services;

import core.domain.User;
import infrastructure.repositories.UserRepository;

public class UserService {

    public static User getUserByDni(int dni) {
        return UserRepository.getUserByDni(dni);
    }
}
