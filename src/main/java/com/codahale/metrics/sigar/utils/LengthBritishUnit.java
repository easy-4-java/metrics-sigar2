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
package com.codahale.metrics.sigar.utils;

public enum LengthBritishUnit {

	asd;
	/* 
		1 m (meter米) = 10 dm (decimeters分米) = 100 cm = 1,000 mm , 
		1 cm = 10 mm = 0.1 dm = 0.01 m
		1 km (kilometer公里) = 1000 m (meters米) = 100,000 cm (centimeters厘米) = 1,000,000 mm (millimeters毫米)
	 */
	
	// Handy constants for conversion methods
	
	/*
	 	
	 	1微米（um）=1000纳米（nm）；
		1纳米（nm) =1000 皮米(pm)
		1皮米(pm)=1000飞米(fm)
		
	 	京米/吉米(Gm)、兆米(Mm)、千米(km)、分米(dm)、厘米(cm)、毫米(mm)、丝米(dmm)、忽米(cmm)、微米(μm)、纳米(nm)、皮米(pm)、飞米(fm)、阿米(am)
	 	1Gm=1×10^9m
		1Mm=1×10^6m
		1km=1×10^3m
		1dm=1×10^(-1)m
		1cm=1×10^(-2)m
		1mm=1×10^(-3)m
		1dmm=1×10^(-4)m
		1cmm=1×10^(-5)m
		1μm=1×10^(-6)m
		1nm=1×10^(-9)m
		1pm=1×10^(-12)m
		1fm=1×10^(-15)m
		1am=1×10^(-18)m
	 */
	
	/**
	 * am(Attometer:阿米)
	 */
    static final long AM = 1L;
	/**
	 * 1fm(Femtometer：飞米) = 1000am（阿米）
	 */
    static final long FM = AM * 1000L;
	/**
	 * 1pm(Picometer：皮米) =1000fm（飞米）
	 */
    static final long PM = FM * 1000L;
    /**
	 * 1nm（Nanometrer ，纳米、毫微米） = 1000pm（皮米）
	 */
    static final long NM = PM * 1000L;
    /**
	 * 1μm（Micrometrer ：微米 ） = 1000nm（纳米）
	 */
    static final long UM = NM * 1000L;
    /**
	 * 1cmm（Centimillimeter：忽米 ） = 10μm（微米）
	 */
    static final long CMM = UM * 10L;
    /**
	 * 1dmm（Decimillimetrer：丝米 ） = 10cmm（忽米）
	 */
    static final long DMM = CMM * 10L;
	/**
	 * 1mm（Millimeter：毫米 ） = 10dmm（丝米）
	 */
    static final long MM = DMM * 10L;
    /**
	 * 1cm(Centimeter，厘米)=10mm（毫米）
	 */
    static final long CM = MM * 10L;
    /**
   	 * 1dm（Decimeter，分米）=10cm（厘米）
   	 */
    static final long DM = CM * 10L;
    /**
	 * 1m（Meter ：米 ）= 10dm（分米） = 100cm（厘米）
	 */
    static final long M = DM * 10L;
    /**
	 * 1km（Kilometer，千米，又称“公里”）= 1000m（米）
	 */
    static final long KM = M * 1000L;
    /**
   	 * 1Mm（Megameter，兆米）= 1000km（千米）
   	 */
    static final long Mm = KM * 1000L;
    /**
   	 * 1Gm（Gigametrer， 京米/吉米）= 1000Mm(兆米) 
   	 */
    static final long Gm = Mm * 1000L;
    /**
	 * 1ly（Light-Year， 光年）= 9460730472581km（千米）
	 */
    static final long LY = KM * 9460730472581L;
    /**
	 * 1au（Astronomical Unit，天文单位）= 0.0000158ly（光年）
	 */
    static final float AU = LY * 0.0000158F;
    
    
	/*注意: 
	
		1弗隆(fur)=110英寻(fm)
		1英寻(fm)=0.0009875海里(nmi)
		1海里(nmi)=1.1507794英里(mi)
		1英里(mi)=1760码(yd)
	 	1码(yd)=3英尺(ft)
	 	1英尺(ft)=12英寸(in) 
	 	1英寸(in)=2.54厘米(cm)
    	
	*/
    /**
   	 * 1in（inch ，英寸）= 2.54cm（厘米）
   	 */
    static final float IN = CM * 2.54F;
    /**
   	 * 1f（feet ，英尺）= 12in（英寸） = 30.48cm（厘米）
   	 */
    static final float FT = IN * 12L;     
    /**
	 * 1yd（yard ，码）= 3ft（英尺）
	 */
    static final float YD = FT * 3L;
    /**
   	 * 1mi（mile ，英里）= 1760yd（码）
   	 */
    static final float MI = YD * 1760L;
    /**
   	 * 1nmi（nautical mile ，海里）= 1.1507794yd（英里）
   	 */
    static final float NMI = MI * 1.1507794F;
    /**
   	 * 1fm（fathom ，英寻）= 0.0009875nmi（海里）
   	 */
    static final float FM2 = MI * 1.1507794F;
     
    /**
     * Scale d by m, checking for overflow.
     * This has a short name to make above code more readable.
     */
    static long x(long d, long m, long over) {
        if (d >  over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }
    
    public long convert(long sourceDuration, CapacityUnit sourceUnit) {
        throw new AbstractMethodError();
    }

    public long toBits(long duration) {
        throw new AbstractMethodError();
    }
    
    public long toBytes(long duration) {
        throw new AbstractMethodError();
    }

    public long toKilobytes(long duration) {
        throw new AbstractMethodError();
    }
	
	
}
