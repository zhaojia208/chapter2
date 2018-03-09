package org.smart4j.chapter2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;

import java.util.List;
import java.util.Map;

/*
* 提供客户数据服务
* */
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    DatabaseHelper databaseHelper = new DatabaseHelper();

    /*
    * 获取客户列表
    * */
    public List<Customer> getCustomerList() {
        try {
            List<Customer> customerList;
            String sql = "select * from customer";
            customerList = databaseHelper.queryEntityList(Customer.class, sql);
            return customerList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*
    * 获取客户
    * */
    public Customer getCustomer(Long id) {
        String sql = " select * from customer where id = ?";
        return DatabaseHelper.queryEntity(Customer.class,sql,id);
    }

    /*
    * 创建客户
    * */
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    /*
    * 更新客户
    * */
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    /*
    * 删除客户
    * */
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
