package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired          //将调用provider里的实例GithubProvider
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();//创建一个githubDTO对象

        accessTokenDTO.setClient_id("50e9cb84afc4fe7ad8b9");    //github需要的五个参数https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/
        accessTokenDTO.setClient_secret("3d53aeb48cdd57551863721a5e0cda46931b83ca");
        accessTokenDTO.setCode(code);
        accessTokenDTO .setRedirect_uri("http://localhost:8080/callback");
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.GetAccessToken(accessTokenDTO);//得到token
        GithubUser user = githubProvider.getUser(accessToken);//得到user信息
        System.out.println(user.getName());
        return "index";
    }
}
