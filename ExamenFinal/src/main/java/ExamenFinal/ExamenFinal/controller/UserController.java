package ExamenFinal.ExamenFinal.controller;

import ExamenFinal.ExamenFinal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping
    public String prueba(){
        return "si sirvo";
    }

}
