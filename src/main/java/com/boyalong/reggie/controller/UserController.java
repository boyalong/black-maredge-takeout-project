package com.boyalong.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boyalong.reggie.common.R;
import com.boyalong.reggie.entity.User;
import com.boyalong.reggie.service.UserService;
import com.boyalong.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机好
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成四位随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码为：{}",code);
            //调用阿里云短信服务发送验证码
//        SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            //将验证码保存session
//            session.setAttribute(phone,code);
            //将验证码缓存到redis中，有效时间五分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success("验证码短信发送成功");
        }
       return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机好
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中拿出保存验证码
//        Object attribute = session.getAttribute(phone);
        //从redis中取出验证码
        Object attribute = redisTemplate.opsForValue().get(phone);

        //进行验证码比对
        if(attribute != null && attribute.equals(code)){
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            //登入成功删除Redis  123缓存中的验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 退出功能
     * ①在controller中创建对应的处理方法来接受前端的请求，请求方式为post；
     * ②清理session中的用户id
     * ③返回结果（前端页面会进行跳转到登录页面）
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        //清理session中的用户id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
