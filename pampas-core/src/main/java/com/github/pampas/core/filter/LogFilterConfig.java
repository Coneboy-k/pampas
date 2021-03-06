package com.github.pampas.core.filter;

import com.github.pampas.common.config.DefinableConfig;
import com.github.pampas.common.extension.SpiMeta;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-06
 */
@SpiMeta(name = LogFilterConfig.SPI_META_NAME, desc = "日志记录配置项")
@Data
public class LogFilterConfig implements DefinableConfig {

    public static final String SPI_META_NAME = "config-log-filter";

    private List<PropDefine> propDefineList = new ArrayList<>();

    public LogFilterConfig() {
        PropDefine propDefine = new PropDefine("log.level", "级别", "warn");
        PropDefine propDefine2 = new PropDefine("log.path", "路径", "/var/log");
        propDefineList.add(propDefine);
        propDefineList.add(propDefine2);
    }


    @Override
    public List<PropDefine> propDefines() {
        return propDefineList;
    }

    @Override
    public BigDecimal configVersionNumber() {
        return BigDecimal.ONE;
    }
}
