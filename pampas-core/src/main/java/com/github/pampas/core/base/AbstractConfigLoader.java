/*
 *
 *  *  Copyright 2009-2018.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package com.github.pampas.core.base;

import com.github.pampas.common.config.ConfigLoader;
import com.github.pampas.common.config.Configurable;
import com.github.pampas.common.config.VersionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置管理器
 * Created by darrenfu on 18-3-8.
 *
 * @author: darrenfu
 * @date: 18 -3-8
 */
public abstract class AbstractConfigLoader<T extends VersionConfig> implements ConfigLoader<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final Byte ONE = Byte.valueOf("1");

    private static ConcurrentHashMap<Class<? extends VersionConfig>, WeakHashMap<Configurable, Byte>> configAndConfigurableMap = new ConcurrentHashMap<>();

    //VersionConfig 实例缓存    VersionConfig都保持单例
    private static ConcurrentHashMap<Class<? extends VersionConfig>, VersionConfig> configInstanceMap = new ConcurrentHashMap<>();


    private List<Configurable<T>> configurableList;


    @Override
    public void addListener(Configurable<T> configurable) {
        if (configurableList == null) {
            configurableList = new ArrayList<>();
        }
        if (!configurableList.contains(configurable)) {
            configurableList.add(configurable);
        }
    }

    /**
     * 缓存VersionConfig和Configurable的关系
     *
     * @param configClz    the config clz
     * @param configurable the configurable
     */
    @Override
    public void markConfigurable(Class<? extends VersionConfig> configClz, Configurable configurable) {
        if (configAndConfigurableMap.contains(configClz)) {
            WeakHashMap<Configurable, Byte> instanceMap = configAndConfigurableMap.get(configClz);
            instanceMap.put(configurable, ONE);
        } else {
            WeakHashMap<Configurable, Byte> instanceMap = new WeakHashMap<>();
            instanceMap.put(configurable, ONE);
            configAndConfigurableMap.putIfAbsent(configClz, instanceMap);
        }
    }

    /**
     * Load config t.
     *
     * @return the t
     */
    @Override
    public T loadConfig() {
        Class<T> configClz = configClass();
        log.info("开始加载配置<{}>,加载器:{}", configClz.getSimpleName(), getClass().getSimpleName());
        T t = doConfigLoad();
        log.info("完成加载配置<{}>,结果:{}", configClz.getSimpleName(), t);

        if (t != null && configurableList != null && configurableList.size() > 0) {
            T[] arr = (T[]) Array.newInstance(configClz, 1);
            arr[0] = t;
            for (Configurable<T> configurable : configurableList) {
                configurable.setupWithConfig((T[]) arr);
            }
        }
        return t;
    }

    public abstract T doConfigLoad();

}
