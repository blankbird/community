package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired          //将调用provider里的实例GithubProvider
    private GithubProvider githubProvider;

    @Value("${github.client.id}")   //Value注解使用application.properties配置里的变量，相当于常量
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request) {    //为了实现cookie和session
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();//创建一个githubDTO对象

        accessTokenDTO.setClient_id(clientId);    //github需要的五个参数https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO .setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.GetAccessToken(accessTokenDTO);//得到token
        GithubUser user = githubProvider.getUser(accessToken);//得到user信息
        if (user != null) {
            //登录成功，写cookie 和 session
            request.getSession().setAttribute("user", user);
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }
    }
}
