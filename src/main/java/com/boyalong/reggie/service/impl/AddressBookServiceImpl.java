package com.boyalong.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyalong.reggie.entity.AddressBook;
import com.boyalong.reggie.mapper.AddressBookMapper;
import com.boyalong.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
