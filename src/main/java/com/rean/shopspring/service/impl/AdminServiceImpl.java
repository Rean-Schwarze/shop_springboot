package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.AdminMapper;
import com.rean.shopspring.pojo.Admin;
import com.rean.shopspring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findByName(String name) {return adminMapper.findByName(name);}
}
