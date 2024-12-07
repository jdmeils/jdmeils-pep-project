package Controller;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

import Service.AccountService;
import Service.MessageService;

public class SocialMediaController {
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Initalize database service objects to pass to controllers - can easily be adapted to inject these differently later
        AccountService accountService = new AccountService();
        MessageService messageService = new MessageService(); 
        // Initalize API controllers
        AccountController accountController = new AccountController(accountService);
        MessageController messageController = new MessageController(accountService, messageService);

        app.routes(() -> {
            post("/register", accountController::register);
            post("/login", accountController::login);
            path("/messages", () -> {
                post(messageController::createMessage);
                get(messageController::getAllMessages);
                path("/{id}", () -> {
                    get(messageController::getMessageById);
                    delete(messageController::deleteMessage);
                    patch(messageController::updateMessage);
                });
            });
            get("/accounts/{id}/messages", messageController::getMessagesByUser);
        });

        return app;
    }


}