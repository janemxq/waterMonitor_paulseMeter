/************************************************* 
Copyright: Xiamen Rongta Technology Co.Ltd.
Author: tanglianghua
Date:   2019-05-21
Description: 
**************************************************/ 
#ifndef _TSC_PRINTER_H
#define _TSC_PRINTER_H
#include <Windows.h>
#include "RT_def.h"
#include <iostream>

using namespace std;
/***********************************************************************
	Param :namePort 接口名称
	Param :eState  连接方式
	E_PORT_COM = 0,
	E_PORT_LPT = 1,
	E_PORT_ENET = 2,
	E_PORT_USB  = 3,
	E_PRINTER = 4

	return ture:成功 false:失败

***********************************************************************/
extern "C" _declspec(dllexport) int _stdcall  OpenPort(const char * namePort,int eState);
/***********************************************************************
     关闭打印机接口                                                              
***********************************************************************/
extern "C" _declspec(dllexport) bool _stdcall ClosePort();
/************************************************************************/
/* 清除                                                                     */
/************************************************************************/
//RT_DLL_API bool Clearbuff();
/***********************************************************************
 配置打印机
 Param labelWidth 标签宽度
 Param labelHeight 标签宽度
	Param verticalGap 垂直间隔 
	Direction 出纸方向 0 或1
	speed 打印速度 默认4
	density 打印浓度默认 5

***********************************************************************/
//extern "C" _declspec(dllexport) bool _stdcall Setup(int labelWidth,int labelHeight,int verticalGap = 3,int Direction = 0,int speed = 4,int density = 5);

	

/***********************************************************************
//2019/07/09
 配置打印机
 Param labelWidth 标签宽度
 Param labelHeight 标签宽度
	Param verticalGap 垂直间隔 
	Direction 出纸方向 0 或1
	speed 打印速度 默认4
	density 打印浓度默认 5
	paperMode : 0.连续纸 1.标签纸 2.黑标纸

***********************************************************************/
extern "C" _declspec(dllexport) bool _stdcall Setup(int labelWidth,int labelHeight,int verticalGap = 3,int Direction = 0,int speed = 4,int density = 5,int paperMode = 1);

/***********************************************************************
 图片打印
 Param x Pos x
 Param y Pos Y
	Param fullPath 路径 
***********************************************************************/
extern "C" _declspec(dllexport) bool _stdcall PrintPicture(int x,int y,const char *fullPath);

/***********************************************************************
设置打印份数
 Param setM 默认1份
***********************************************************************/
extern "C" _declspec(dllexport) bool _stdcall SetPrintLabel(int copies = 1);

/***********************************************************************
设置打印自检页
***********************************************************************/

extern "C" _declspec(dllexport) int _stdcall PrintSelf();

/***********************************************************************
 配置IP地址 网络通信前必须先设置Ip
 Param IP IP地址
 Param portNum 端口号
	
***********************************************************************/
extern "C" _declspec(dllexport) int _stdcall ConfigIP(const char * m_IP, int portNum);

extern "C" _declspec(dllexport) int _stdcall Test(int x,int y);

#endif