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
	Param :namePort �ӿ�����
	Param :eState  ���ӷ�ʽ
	E_PORT_COM = 0,
	E_PORT_LPT = 1,
	E_PORT_ENET = 2,
	E_PORT_USB  = 3,
	E_PRINTER = 4

	return ture:�ɹ� false:ʧ��

***********************************************************************/
extern "C" _declspec(dllexport) int _stdcall  OpenPort(const char * namePort,int eState);
/***********************************************************************
     �رմ�ӡ���ӿ�                                                              
***********************************************************************/
extern "C" _declspec(dllexport) bool _stdcall ClosePort();
/************************************************************************/
/* ���                                                                     */
/************************************************************************/
//RT_DLL_API bool Clearbuff();
/***********************************************************************
 ���ô�ӡ��
 Param labelWidth ��ǩ���
 Param labelHeight ��ǩ���
	Param verticalGap ��ֱ��� 
	Direction ��ֽ���� 0 ��1
	speed ��ӡ�ٶ� Ĭ��4
	density ��ӡŨ��Ĭ�� 5

***********************************************************************/
//extern "C" _declspec(dllexport) bool _stdcall Setup(int labelWidth,int labelHeight,int verticalGap = 3,int Direction = 0,int speed = 4,int density = 5);

	

/***********************************************************************
//2019/07/09
 ���ô�ӡ��
 Param labelWidth ��ǩ���
 Param labelHeight ��ǩ���
	Param verticalGap ��ֱ��� 
	Direction ��ֽ���� 0 ��1
	speed ��ӡ�ٶ� Ĭ��4
	density ��ӡŨ��Ĭ�� 5
	paperMode : 0.����ֽ 1.��ǩֽ 2.�ڱ�ֽ

***********************************************************************/
extern "C" _declspec(dllexport) bool _stdcall Setup(int labelWidth,int labelHeight,int verticalGap = 3,int Direction = 0,int speed = 4,int density = 5,int paperMode = 1);

/***********************************************************************
 ͼƬ��ӡ
 Param x Pos x
 Param y Pos Y
	Param fullPath ·�� 
***********************************************************************/
extern "C" _declspec(dllexport) bool _stdcall PrintPicture(int x,int y,const char *fullPath);

/***********************************************************************
���ô�ӡ����
 Param setM Ĭ��1��
***********************************************************************/
extern "C" _declspec(dllexport) bool _stdcall SetPrintLabel(int copies = 1);

/***********************************************************************
���ô�ӡ�Լ�ҳ
***********************************************************************/

extern "C" _declspec(dllexport) int _stdcall PrintSelf();

/***********************************************************************
 ����IP��ַ ����ͨ��ǰ����������Ip
 Param IP IP��ַ
 Param portNum �˿ں�
	
***********************************************************************/
extern "C" _declspec(dllexport) int _stdcall ConfigIP(const char * m_IP, int portNum);

extern "C" _declspec(dllexport) int _stdcall Test(int x,int y);

#endif