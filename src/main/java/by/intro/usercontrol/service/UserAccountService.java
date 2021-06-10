package by.intro.usercontrol.service;

import by.intro.usercontrol.model.UserAccount;
import by.intro.usercontrol.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccountService() {
    }

    public UserAccount findById(Long accountId) {
        Optional<UserAccount> uaop = userAccountRepository.findById(accountId);
        return uaop.get();
    }

    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }

    public UserAccount saveUserAccount(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }



}
