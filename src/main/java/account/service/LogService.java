package account.service;

import account.dto.AccountSignUpDTO;
import account.entity.Log;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface LogService {

    public Log addLogSignUp(Event eventName, @Valid AccountSignUpDTO account, HttpServletRequest request);

    public Log addLogChangePassword(Event eventName, String account, HttpServletRequest request);

    public Log addLogGrantRole(Event eventName, String account, HttpServletRequest request, String role);

    public Log addLogRemoveRole(Event eventName, String account, HttpServletRequest request, String role);

    public Log addLogDeleteUser(Event eventName, String account, HttpServletRequest request);

}
