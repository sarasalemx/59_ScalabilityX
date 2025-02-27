package com.example.repository;

import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User>{
    @Override
    protected String getDataPath() {
        return "";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return null;
    }

    public UserRepository() {
    }

}
