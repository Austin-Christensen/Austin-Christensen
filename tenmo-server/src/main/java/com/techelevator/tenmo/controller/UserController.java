package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthorized")
public class UserController {

    @Autowired
    private UserDao userDao;

    @PreAuthorize("permitAll")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDao.getUsers();
    }

    //TODO fix pathing for separate calls or combine
//    @PreAuthorize("permitAll")
//    @RequestMapping(path = "", method = RequestMethod.GET)
//    public User listUserById(@PathVariable int userId){
//        User user = userDao.getUserById(userId);
//        if(user == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//        }else {
//            return user;
//        }
//    }
//
//    @PreAuthorize("permitAll")
//    @RequestMapping(path = "", method = RequestMethod.GET)
//    public User listUserByUsername(@PathVariable String userName){
//        User user = userDao.getUserByUsername(userName);
//        if(user == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//        }else {
//            return user;
//        }
//    }

}
