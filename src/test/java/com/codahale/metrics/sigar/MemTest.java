/*
 * Copyright (c) 2018 (https://github.com/easy-4-java).
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

import java.util.Collection;
import java.util.LinkedList;

import com.codahale.metrics.sigar.utils.MemoryWarningSystem;  

public class MemTest {  
  public static void main(String[] args) {
	  
    MemoryWarningSystem.setPercentageUsageThreshold(0.6);  
  
    MemoryWarningSystem mws = new MemoryWarningSystem();  
    mws.addListener(new MemoryWarningSystem.Listener() {  
      public void memoryUsageLow(long usedMemory, long maxMemory) {  
        System.out.println("Memory usage low!!!");  
        double percentageUsed = ((double) usedMemory) / maxMemory;  
        System.out.println("percentageUsed = " + percentageUsed);  
        MemoryWarningSystem.setPercentageUsageThreshold(0.8);  
      }  
    });  
  
    Collection<Double> numbers = new LinkedList<Double>();  
    while (true) {  
      numbers.add(Math.random());  
    }  
  }  
}  