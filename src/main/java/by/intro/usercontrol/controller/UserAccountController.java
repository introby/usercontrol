package by.intro.usercontrol.controller;

import by.intro.usercontrol.model.Status;
import by.intro.usercontrol.model.UserAccount;
import by.intro.usercontrol.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
public class UserAccountController {

    private static String oldPassword = null;
    public static String oldUsername = null;

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('accounts:read')")
    public String findAll(Model model) {
        List<UserAccount> userAccountList = userAccountService.findAll();
        model.addAttribute("userAccountList", userAccountList);
        return "user";
    }

    @GetMapping("/user/new")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String createUserForm(UserAccount userAccount) {
        return "new";
    }

    @PostMapping("/user/new")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String createUserAccount(@Valid UserAccount userAccount, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "new";

        userAccount.setCreatedAt(LocalDate.now());
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccountService.saveUserAccount(userAccount);
        return "redirect:/user";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('accounts:read')")
    public String viewAccount(@PathVariable("id") Long id, Model model) {
        UserAccount userAccount = userAccountService.findById(id);
        model.addAttribute("userAccount", userAccount);
        return "view";
    }

    @GetMapping("/user/{id}/edit")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        UserAccount userAccount = userAccountService.findById(id);
        oldPassword = userAccount.getPassword();
        oldUsername = userAccount.getUsername();
        model.addAttribute("userAccount", userAccount);
        return "edit";
    }

    @PostMapping("/user/{id}/edit")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String editUser(@Valid UserAccount userAccount, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "edit";

        if (!userAccount.getPassword().equals(oldPassword)) {
            userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        }
        userAccountService.saveUserAccount(userAccount);
        return "redirect:/user";
    }

    @PostMapping("/user/{id}/lock")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String lockUnlockUser(@PathVariable("id") Long id) {
        UserAccount userAccount = userAccountService.findById(id);
        if (userAccount.getStatus().equals(Status.ACTIVE)) {
            userAccount.setStatus(Status.INACTIVE);
        } else {
            userAccount.setStatus(Status.ACTIVE);
        }
        userAccountService.saveUserAccount(userAccount);
        return "redirect:/user/{id}";
    }

}
