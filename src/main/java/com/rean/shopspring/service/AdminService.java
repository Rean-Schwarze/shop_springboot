package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Admin;

public interface AdminService {
    Admin findByName(String name);
}
