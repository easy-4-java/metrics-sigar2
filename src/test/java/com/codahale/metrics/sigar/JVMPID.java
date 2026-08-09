/*
 * Copyright (c) 2018 (https://github.com/loong10k).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.codahale.metrics.sigar;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;  

/** 
 * 适用Windows(其他系统未经测试) 
 */  
public class JVMPID  
{  
    public static void main(String[] args)  
    {  
        // 在windows上，获取到得name格式为 1234@userName  
        // 1234为PID，@为分隔符，userName为当前用户  
        String pid = ManagementFactory.getRuntimeMXBean().getName();  
        int indexOf = pid.indexOf('@');  
        if (indexOf > 0)  
        {  
            pid = pid.substring(0, indexOf);  
        }  
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        System.out.println("当前JVM Process ID: " + pid);
        System.out.println("当前JVM Process ID1: " + pid());  
    }  
    
    public static int pid() {
        try {
            try {
                Class<?> phClass = Class.forName("java.lang.ProcessHandle");
                Object handle = phClass.getMethod("current").invoke(null);
                Object pid = phClass.getMethod("pid").invoke(handle);
                return (int) (long) pid;
            } catch (Throwable ignore) {
                String name = ManagementFactory.getRuntimeMXBean().getName();
                int at = name.indexOf('@');
                if (at > 0) {
                    return Integer.parseInt(name.substring(0, at));
                }
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }
}  