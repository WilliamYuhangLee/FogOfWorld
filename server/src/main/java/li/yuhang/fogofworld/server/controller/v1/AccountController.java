package li.yuhang.fogofworld.server.controller.v1;

import li.yuhang.fogofworld.server.dto.AccountDto;
import li.yuhang.fogofworld.server.request.SignUpRequest;
import li.yuhang.fogofworld.server.util.Response;
import li.yuhang.fogofworld.server.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public Response signUp(@RequestBody @Valid SignUpRequest request) {
        AccountDto accountDto = accountService.signUp(new AccountDto()
                                                      .setUsername(request.getUsername())
                                                      .setPassword(request.getPassword()));
        return Response.withStatus(HttpStatus.OK).setPayload(accountDto);
    }
}
