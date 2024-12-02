package ExamenFinal.ExamenFinal.service;

import ExamenFinal.ExamenFinal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;

}
