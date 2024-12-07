package Controller;

import io.javalin.http.Context;

import Model.Account;
import Service.AccountService;

public class AccountController {
    private AccountService accounts;

    public AccountController(AccountService accounts) {
        this.accounts = accounts;
    }

    protected void register(Context ctx) {
        var newUser = ctx.bodyAsClass(Account.class);
        if(
            newUser.username.isBlank()
            || newUser.password.length() < 4
            || accounts.get(newUser.username) != null
        ) {
            // Invalid registration request
            ctx.status(400);
        } else {
            // Register new user
            var createdUser = accounts.create(newUser);
            ctx.json(createdUser);
        }
    }

    protected void login(Context ctx) {
        var login = ctx.bodyAsClass(Account.class);
        var existing = accounts.get(login.username);
        if (
            existing != null 
            && existing.password.equals(login.password)) {
                ctx.json(existing);
        } else {
            ctx.status(401);
        }
    }
}
