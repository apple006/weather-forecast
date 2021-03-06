package com.earthchen.spring.cloud.weather.task;

import com.earthchen.spring.cloud.weather.service.WeatherDataCollectionService;
import com.earthchen.spring.cloud.weather.vo.City;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 天气数据同步任务
 */
@Service
@Slf4j
public class WeatherDataSyncTask extends BaseTask {


    @Autowired
    private WeatherDataCollectionService weatherDataCollectionService;

    /**
     * 半小时同步一次
     *
     * @return
     */
    @Override
    public String getCronExpression() {
        return "* 0/30 * * * ? *";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Weather Data Sync Job. Start！");
        // 获取城市ID列表
        // TODO 改为由城市数据API微服务来提供数据
        List<City> cityList = null;

        try {

            // TODO 改为由城市数据API微服务提供数据
            cityList = new ArrayList<>();
            City city = new City();
            city.setCityId("101280601");
            cityList.add(city);

        } catch (Exception e) {
            log.error("Exception!", e);
        }

        // 遍历城市ID获取天气
        for (City city : cityList) {
            String cityId = city.getCityId();
            log.info("Weather Data Sync Job, cityId:" + cityId);

            weatherDataCollectionService.syncDateByCityId(cityId);
        }

        log.info("Weather Data Sync Job. End！");
    }
}
