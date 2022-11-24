package account.logDAO;

import account.entity.Log;
import account.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.List;

public class LogDaoImpl implements LogDAO {

    @Autowired
    LogRepository logRepository;

    @Override
    public void addLog(Log log) {
        logRepository.save(log);
    }

    @Override
    public List<Log> getLogsOrderById() {
        return (List<Log>) logRepository.findAll();
    }

}
