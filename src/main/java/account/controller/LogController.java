package account.controller;

import account.entity.Log;
import account.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {

    @Autowired
    LogRepository logRepository;

    @GetMapping("api/auth/signup")
    public List<Log> getLogs(@AuthenticationPrincipal UserDetails user) {
        return (List<Log>) logRepository.findAll();
    }
}
