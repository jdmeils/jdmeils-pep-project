package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.http.Context;

public class MessageController {
    private AccountService accounts;
    private MessageService messages;

    public MessageController(AccountService accounts, MessageService messages) {
        this.accounts = accounts;
        this.messages = messages;
    }

    // POST /messages
    protected void createMessage(Context ctx) {
        var message = ctx.bodyAsClass(Message.class);
        var user = accounts.get(message.posted_by);
        if (
            message.message_text.isBlank()
            || message.message_text.length() > 255
            || user == null
        ) {
            // validation error
            ctx.status(400); 
        } else {
            // post new message
            var newMsg = messages.create(message);
            ctx.json(newMsg);
        }
    }

    // GET /messages
    protected void getAllMessages(Context ctx) {
        ctx.json(messages.get());
    }

    // GET /accounts/{id}/messages
    protected void getMessagesByUser(Context ctx) {
        var requestedUser = ctx.pathParam("id");
        var userId = Integer.parseInt(requestedUser);
        var userMessages = messages.getByUser(userId);
        if (userMessages == null) {
            userMessages = new ArrayList<>();
        }
        ctx.json(userMessages);
    }

    // GET /messages/{id}
    protected void getMessageById(Context ctx) {
        var requestedId = ctx.pathParam("id");
        var messageId = Integer.parseInt(requestedId);
        var message = messages.getById(messageId);
        if (message != null) {
            ctx.json(message);
        }
    }

    // DELETE /messages/{id}
    protected void deleteMessage(Context ctx) {
        var requestedId = ctx.pathParam("id");
        var messageId = Integer.parseInt(requestedId);
        var deleting = messages.getById(messageId);
        if (deleting != null) {
            messages.delete(deleting.message_id);
            ctx.json(deleting);
        }
    }

    // PATCH /messages/{id}
    protected void updateMessage(Context ctx) {
        var message = ctx.bodyAsClass(Message.class);
        var newContent = message.message_text;
        var requestedId = ctx.pathParam("id");
        var messageId = Integer.parseInt(requestedId);
        var existing = messages.getById(messageId);
        if (
            existing == null
            || newContent.isBlank()
            || newContent.length() > 255
        ) {
            ctx.status(400);
        } else {
            messages.update(existing.message_id, newContent);
            var updated = new Message(
                existing.message_id,
                existing.posted_by,
                newContent,
                existing.time_posted_epoch
            );
            ctx.json(updated);
        }
    }
}
