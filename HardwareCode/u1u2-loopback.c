/*
 * Copyright (c) 2010, Mariano Alvira <mar@devl.org> and other contributors
 * to the MC1322x project (http://mc1322x.devl.org)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This file is part of libmc1322x: see http://mc1322x.devl.org
 * for details. 
 *
 *
 */

#include <mc1322x.h>
#include <board.h>
#include <unistd.h>
#define DELAY 400
#include <stdio.h>
#include "tests.h"
#include "config.h"
#include "adc.h"

void main(void) {

	trim_xtal();
	uart_init(UART1, 115200);
	uart_init(UART2, 115200);
	adc_init();

		adc_setup_chan(0);
	
	printf("adc test\r\n");

        GPIO->PAD_PU_SEL.U2RX = 1;
	volatile uint32_t i;
	while(1) {

		adc_service();
		for(i=0; i<DELAY; i++) { continue; }
		if(adc_reading[0] < 300 && adc_reading[0] > 200 )
		{
		uart2_putc('a'); //150
		
		}
		if(adc_reading[0] < 400 && adc_reading[0] > 300 )
		{
		uart2_putc('b');
		
		}
		if(adc_reading[0] < 500 && adc_reading[0] > 400 )
		{
		uart2_putc('c');
		}
		if(adc_reading[0] < 600 && adc_reading[0] > 500 )
		{
		uart2_putc('d');
		}
		if(adc_reading[0] < 700 && adc_reading[0] > 600 )
		{
		uart2_putc('e');
		}
		if(adc_reading[0] < 800 && adc_reading[0] > 700 )
		{
		uart2_putc('f');
		}
		if(adc_reading[0] < 900 && adc_reading[0] > 800 )
		{
		uart2_putc('g');
		}
		if(adc_reading[0] < 1000 && adc_reading[0] > 900 )
		{
		uart2_putc('h');
		}
		if(adc_reading[0] < 1100 && adc_reading[0] > 1000 )
		{
		uart2_putc('i');
		}
		if(adc_reading[0] < 1200 && adc_reading[0] > 1100 )
		{
		uart2_putc('j');
		}
		if(adc_reading[0] < 1300 && adc_reading[0] > 1200 )
		{
		uart2_putc('k');
		}
		if(adc_reading[0] < 1400 && adc_reading[0] > 1300 )
		{
		uart2_putc('l');
		}
		if(adc_reading[0] < 1500 && adc_reading[0] > 1400 )
		{
		uart2_putc('m');
		}
		if(adc_reading[0] < 1600 && adc_reading[0] > 1500 )
		{
		uart2_putc('n');
		}
		if(adc_reading[0] < 1700 && adc_reading[0] > 1600 )
		{
		uart2_putc('o');
		}
		if(adc_reading[0] < 1800 && adc_reading[0] > 1700 )
		{
		uart2_putc('p');
		}
		if(adc_reading[0] > 1800 )
		{
		uart2_putc('q');
		}
		
		
		
	}
	
}
