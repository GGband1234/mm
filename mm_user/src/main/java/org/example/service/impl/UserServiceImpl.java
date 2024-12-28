package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.dto.UserDto;
import org.example.domain.po.User;
import org.example.domain.vo.UserVo;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.example.utils.MD5Utils;
import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public void register(String username, String password) throws NoSuchAlgorithmException {
        String md5String = MD5Utils.encrypt(password);
        userMapper.addUser(username,md5String);

    }

    @Override
    public User select(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public void updatePermission(Integer permission) {
        String username  = ThreadLocalUtil.get();

        userMapper.updatePermission(permission,username);
    }
    public void updateUserInfo(UserDto userDto){
        String username = ThreadLocalUtil.get();
        lambdaUpdate().set(userDto.getUsername() != null,User::getUsername,userDto.getUsername())
                .set(userDto.getEmail() != null,User::getEmail,userDto.getEmail())
                .set(userDto.getEmail() != null,User::getEmail,userDto.getEmail())
                .set(userDto.getPhoneNumber() != null,User::getPhoneNumber,userDto.getPhoneNumber())
                .eq(User::getUsername,username)
                .update();
    }

    @Override
    public void updatePwd(String newPwd) {
        String username = ThreadLocalUtil.get();

        lambdaUpdate().set(User::getPassword,newPwd)
                .eq(User::getUsername,username)
                .update();
    }

    @Override
    public UserVo selectUserInfo() {
        String username = ThreadLocalUtil.get();
        User user = userMapper.findByUserName(username);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        if(user.getPermission() == null){
            userVo.setPermission(null);
        }else if (user.getPermission() == 0){
            userVo.setPermission("员工");
        }else{
            userVo.setPermission("管理员");
        }
        return userVo;
    }

//    @Override
//    public void update(User user) {
//        user.setUpdateTime(LocalDateTime.now());
//        userMapper.update(user);
//    }
//
//    @Override
//    public void updateAvatar(String avatar) {
//        Map<String,Object> cliams = ThreadLocalUtil.get();
//        Integer id = (Integer) cliams.get("id");
//        userMapper.updateAvatar(avatar,id);
//    }
//


}
