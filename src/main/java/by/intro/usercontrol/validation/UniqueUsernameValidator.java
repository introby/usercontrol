package by.intro.usercontrol.validation;

import by.intro.usercontrol.controller.UserAccountController;
import by.intro.usercontrol.model.UserAccount;
import by.intro.usercontrol.repository.UserAccountRepository;
import by.intro.usercontrol.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public void initialize(UniqueUsername annotation) {
        // Intentionally empty: nothing to initialize
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {

        if (value == null) {
            return true;
        }

        if (value.equals(UserAccountController.oldUsername))
            return true;

        boolean usernameExists = false;

        if (userAccountRepository != null)
            usernameExists = userAccountRepository.findByUsername(value).isPresent();

        return !usernameExists;
    }
}
