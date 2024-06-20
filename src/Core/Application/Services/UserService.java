package Core.Application.Services;

import Core.Domain.User;
import Infrastructure.Repositories.UserRepository;

public class UserService {

    public static User getUserByDni(int dni) {
        return UserRepository.getUserByDni(dni);
    }
}
