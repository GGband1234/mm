package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.example.domain.dto.UserDto;
import org.example.domain.po.User;
import org.example.domain.vo.UserVo;
import org.example.result.Result;
import org.example.service.UserService;
import org.example.utils.JwtUtils;
import org.example.utils.MD5Utils;
import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin
@Tag(name = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String token){
        stringRedisTemplate.delete(token);
        return Result.success();
    }
    @Operation(summary = "用户注册接口测试")
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) throws NoSuchAlgorithmException {
        User u = userService.findByUserName(username);
        if(u==null){
            userService.register(username,password);
            return Result.success();
        }else{
            return Result.error("用户名已被占用");
        }

    }

    @Operation(summary = "用户登录接口测试")
    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password) throws NoSuchAlgorithmException {
        User u = userService.findByUserName(username);
        if(u == null){
            return Result.error("用户名不存在");
        }
        if(MD5Utils.encrypt(password).equals(u.getPassword())){
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",u.getUserId());
            claims.put("username",u.getUsername());
            String token = JwtUtils.getToken(claims);
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

//   (value = "用户信息接口测试")

    @Operation(summary = "查询用户基本信息")
    @GetMapping("/selectUserInfo")
    public Result<UserVo> selectUserInfo(){
        UserVo userVo = userService.selectUserInfo();
        return Result.success(userVo);
    }
//    @ApiOperation(value = "更新用户权限")
    @Operation(summary = "更新用户权限")
    @PutMapping("/updatePermission")
    public Result updatePermission( String permission , String pwd){
        if ("666".equals(pwd)){
            if("管理员".equals(permission)){
                userService.updatePermission(1);
                return Result.success();
            }else if("员工".equals(permission)){
                userService.updatePermission(0);
                return Result.success();
            }else {
                return  Result.error("用户权限只能为管理员或员工");
            }
        }
        return Result.error("密码错误");
    }


    @Operation(summary = "更改用户信息")
    @PutMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody UserDto userDto,@RequestHeader("Authorization") String token){
        String username = ThreadLocalUtil.get();
        User u = userService.findByUserName(userDto.getUsername());
        if (u != null && username.equals(u.getUsername())){
            userService.lambdaUpdate().set(userDto.getPhoneNumber()!=null,User::getPhoneNumber,userDto.getPhoneNumber())
                    .set(userDto.getEmail()!= null,User::getEmail,userDto.getEmail())
                    .eq(User::getUsername,userDto.getUsername())
                    .update();
            return Result.success();
        }
        if(u == null){
            userService.updateUserInfo(userDto);
            stringRedisTemplate.delete(token);
            return Result.success();
        }
        return Result.error("用户名已存在");
    }

    @Operation(summary = "更改密码")
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token) throws NoSuchAlgorithmException {

        String oldPwd =  params.get("old_Pwd");
        String newPwd =  params.get("new_Pwd");
        String reqPwd =  params.get("req_Pwd");
        if (!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||!StringUtils.hasLength(reqPwd)){
            return Result.error("缺少必要的参数");
        }
        String username = ThreadLocalUtil.get();
        User user = userService.findByUserName(username);
        if(!user.getPassword().equals(MD5Utils.encrypt(oldPwd))){
            return Result.error("原密码不正确");
        }
        if (!newPwd.equals(reqPwd)){
            return Result.error("新密码与确认的密码不一致");
        }
        userService.updatePwd(MD5Utils.encrypt(newPwd));
        stringRedisTemplate.delete(token);
        return Result.success();

    }

    @Operation(summary = "根据用户名查询用户信息")
    @GetMapping("/findByName/{username}")
    public User findByUserName(@PathVariable("username") String username){
        return userService.findByUserName(username);
    }

    @Operation(summary = "根据姓名模糊查询户信息")
    @GetMapping("/list")
    public List<User> listByUserName(@RequestParam("username") String username){
        return userService.lambdaQuery()
                .like(username !=null,User::getUsername,username)
                .list();
    }

    @Operation(summary = "根据id查询户信息")
    @GetMapping("/findById/{userId}")
    public User findByUserId(@PathVariable("userId") Integer userId){
        return userService.getById(userId);
    }

}
