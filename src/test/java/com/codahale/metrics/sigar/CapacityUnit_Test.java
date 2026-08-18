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

import com.codahale.metrics.sigar.utils.CapacityUtils;

import junit.framework.TestCase;

public class CapacityUnit_Test extends TestCase {

	public void test() {
		
		System.out.println(CapacityUtils.getCapacity("10MB").toPlainString());
		
		/*

		double v = CapacityUnit.DOGGABYTES.toKilobytes(100);
		
		System.out.println(v);
		System.out.println(new BigDecimal(v).toPlainString());*/
	}
	
}
