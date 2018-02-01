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

package com.github.df.pampas.common.tools;

/**
 * StringBuilder工厂类
 * Created by darrenfu on 17-7-1.
 */
public class StringBuilderFactory {

    public static final StringBuilderFactory DEFAULT = new StringBuilderFactory();

    private final ThreadLocal<StringBuilder> pool = ThreadLocal.withInitial(() -> new StringBuilder(512));

    /**
     * BEWARE: MUSN'T APPEND TO ITSELF!
     *
     * @return a pooled StringBuilder
     */
    public StringBuilder stringBuilder() {
        StringBuilder sb = pool.get();
        sb.setLength(0);
        return sb;
    }
}
