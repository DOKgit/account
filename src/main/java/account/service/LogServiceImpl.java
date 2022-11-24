package account.service;

import account.dto.AccountSignUpDTO;
import account.entity.Log;
import account.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogRepository logRepository;

//    @Autowired
//    LogDAO logDAO;

    public Log addLogSignUp(Event eventName, @Valid AccountSignUpDTO account, HttpServletRequest request) {

        Log log = new Log();
        log.setAction(eventName.toString());
        log.setSubject("Anonymous");
        log.setObject(account.getEmail().toLowerCase());
        log.setPath(request.getRequestURI());
        logRepository.save(log);
        return log;
    }

    public Log addLogChangePassword(Event eventName, String account, HttpServletRequest request) {

        Log log = new Log();
        log.setAction(eventName.toString());
        log.setSubject(account);
        log.setObject(account);
        log.setPath(request.getRequestURI());
        logRepository.save(log);
        return log;
    }

    public Log addLogGrantRole(Event eventName, String account, HttpServletRequest request, String role) {

        Log log = new Log();
        log.setAction(eventName.toString());
        log.setSubject(request.getUserPrincipal().getName());
        log.setObject(eventName.getEventMessage() + " " + role + " to " + account);
        log.setPath(request.getRequestURI());
        logRepository.save(log);
        return log;
    }

    public Log addLogRemoveRole(Event eventName, String account, HttpServletRequest request, String role) {

        Log log = new Log();
        log.setAction(eventName.toString());
        log.setSubject(request.getUserPrincipal().getName());
        log.setObject(eventName.getEventMessage() + " " + role + " from " + account);
        log.setPath(request.getRequestURI());
        logRepository.save(log);
        return log;
    }

    public Log addLogDeleteUser(Event eventName, String account, HttpServletRequest request) {

        Log log = new Log();
        log.setAction(eventName.toString());
        log.setSubject(request.getUserPrincipal().getName());
        log.setObject(account);
        log.setPath(request.getRequestURI());
        logRepository.save(log);
        return log;
    }

}
