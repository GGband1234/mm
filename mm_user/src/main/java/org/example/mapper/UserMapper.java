package org.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.domain.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from users where username = #{username}")
    User findByUserName(String username);

    @Insert("insert into users(username,password,create_time,update_time) " +
            "values (#{username},#{password},now(),now())")
    void addUser(String username, String password);

    @Update("update users set permission = #{permission},update_time = now() where username = #{username}")
    void updatePermission(Integer permission,String username);


    @Update("update users set username = #{username},email = #{email}, phone_number = #{phoneNumber}" +
            ",update_time = now() where user_id = #{userId}")
    void updateUesrInfo(User user);

    @Update("update users set password=#{newPwd},update_time=now() where user_id=#{id}")
    void updatePwd(@Param("newPwd") String newPwd,@Param("id") Integer id);


//    @Update("update user set nickname=#{nickname},email=#{email},update_time=#{updateTime} where id=#{id}")
//    void update(User user);
//
//    @Update("update user set user_pic=#{avatar},update_time=now() where id=#{id}")
//    void updateAvatar(String avatar,Integer id);
//

}
