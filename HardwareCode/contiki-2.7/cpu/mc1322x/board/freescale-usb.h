/*
 * freescale-usb.h
 *
 *  Created on: 4 Sep 2011
 *      Author: mcphillips
 */

#ifndef FREESCALE_USB_H_
#define FREESCALE_USB_H_

#define LED1		GPIO_09

#define LED1_OFF	(GPIO->DATA_RESET.LED1 = 1)
#define LED1_ON		(GPIO->DATA_SET.LED1 = 1)

#define LED_GREEN		9

/* XTAL TUNE parameters */
/* see http://devl.org/pipermail/mc1322x/2009-December/000162.html */
/* for details about how to make this measurement */

/* Coarse tune: add 4pf */
#define CTUNE_4PF 1
/* Coarse tune: add 0-15 pf */
#define CTUNE 8
/* Fine tune: add FTUNE * 156fF (FTUNE is 4bits) */
#define FTUNE 15

#include <std_conf.h>

#endif /* FREESCALE_USB_H_ */
