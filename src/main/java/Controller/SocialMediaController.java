package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;

    public SocialMediaController() {
        this.accountService = new AccountService();
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

}