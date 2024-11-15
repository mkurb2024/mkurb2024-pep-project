package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postAccountRegisterHandler);
        app.post("/login", this::postAccountLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageTextHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postAccountRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        // Check if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist.
        if (account.getUsername() != "" && account.getPassword().length() >= 4 && accountService.getAccountByUsername(account.getUsername()) == null) {
            Account addedAccount = accountService.addAccount(account);
            if(addedAccount != null) {
                ctx.json(mapper.writeValueAsString(addedAccount));
            }
        } else {
            ctx.status(400);
        }
    }

    private void postAccountLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account loginInfo = mapper.readValue(ctx.body(), Account.class);

        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();

        Account existingAcct = accountService.getAccountByUsername(username);
        // check if user and password exists in the database
        if(existingAcct != null && existingAcct.getPassword().equals(password)) {
                ctx.json(mapper.writeValueAsString(existingAcct)).status(200);
        } else {
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Account existingAcct = accountService.getAccountById(message.getPosted_by());

        if(message.getMessage_text() != "" && message.getMessage_text().length() <= 255 && existingAcct != null) {
            Message createdMessage = messageService.createMessage(message);
            
            if(createdMessage != null) {
                ctx.json(createdMessage).status(200);
            } else {
                ctx.status(400);
            }
        } else {
            ctx.status(400);
        }
    }

    public void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        if(messages != null) {
            ctx.json(messages).status(200);
        } else {
            ctx.status(500);
        }
    }

    public void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if(message != null) {
            ctx.json(message).status(200);
        } else {
            ctx.status(200).result("");
        }
    }

    public void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted = messageService.deleteMessage(messageId);
        if(deleted != null) {
            ctx.status(200).json(deleted);
        } else {
            ctx.status(200).result("");
        }
    }

    public void patchMessageTextHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message newMsg = mapper.readValue(ctx.body(), Message.class);
        String newMessageText = newMsg.getMessage_text();

        Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);
        if(updatedMessage != null && newMessageText != "" && newMessageText.length() <= 255) {
            ctx.json(mapper.writeValueAsString(updatedMessage)).status(200);
        } else {
            ctx.status(400);
        }
    }

    public void getMessagesByAccountIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        int accountId = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> messages = messageService.getMessagesByAccountId(accountId);

        ctx.json(mapper.writeValueAsString(messages)).status(200);
    }

}