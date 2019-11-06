package li.yuhang.fogofworld.server.controller.v1;

import li.yuhang.fogofworld.server.dto.AccountDto;
import li.yuhang.fogofworld.server.request.AccountRequest;
import li.yuhang.fogofworld.server.util.Response;
import li.yuhang.fogofworld.server.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public Response signUp(@RequestBody @Valid AccountRequest request) {
        AccountDto accountDto = accountService.signUp(new AccountDto()
                                                      .setUsername(request.getUsername())
                                                      .setPassword(request.getPassword()));
        return Response.withStatus(HttpStatus.OK).setPayload(accountDto);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@RequestBody @Valid AccountRequest request) {
        return Response.withStatus(HttpStatus.OK).setPayload("Login Successful. JWT token returned.");
    }
}
