package org.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.UserDto;
import org.example.domain.po.User;
import org.example.domain.vo.UserVo;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

public interface UserService extends IService<User> {
    User findByUserName(String username);

    void register(String username, String password) throws NoSuchAlgorithmException;

    User select(String username);
    @Transactional
    void updatePermission(Integer permission);
    @Transactional
    void updateUserInfo(UserDto user);
    @Transactional
    void updatePwd(String newPwd);

    UserVo selectUserInfo();

//    void update(User user);
//
//    void updateAvatar(String avatar);
//

}
