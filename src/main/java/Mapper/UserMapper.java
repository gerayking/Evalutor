package Mapper;


import pojo.UserPojo;

public interface UserMapper {
    UserPojo getUser(String email);
}
