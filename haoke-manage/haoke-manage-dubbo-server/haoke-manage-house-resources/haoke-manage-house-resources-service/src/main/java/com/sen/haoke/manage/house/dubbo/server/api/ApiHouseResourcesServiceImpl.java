package com.sen.haoke.manage.house.dubbo.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.sen.haoke.manage.house.dubbo.server.pojo.HouseResources;
import com.sen.haoke.manage.house.dubbo.server.service.HouseResourcesService;
import com.sen.haoke.manage.house.dubbo.server.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: Sen
 * @Date: 2020/2/9 22:53
 * @Description: Dubbo服务提供者
 */
@Service(version = "1.0.0")
public class ApiHouseResourcesServiceImpl implements ApiHouseResourcesService {

    @Autowired
    private HouseResourcesService houseResourcesService;

    @Override
    public int saveHouseResources(HouseResources houseResources) {
        return houseResourcesService.saveHouseResources(houseResources);
    }

    @Override
    public PageInfo<HouseResources> queryHouseResourcesList(int page, int pageSize, HouseResources queryCondition) {
        return houseResourcesService.queryHouseResourcesList(page, pageSize, queryCondition);
    }

    @Override
    public HouseResources queryHouseResourcesById(Long id) {
        return houseResourcesService.queryHouseResourcesById(id);
    }
}
