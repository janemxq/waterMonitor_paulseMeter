(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d2085f3"],{a509:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("tableTemplate",{attrs:{addModule:!1,columns:t.columns,seek:t.seek,params:t.params,dataCallBack:t.dataCallBack,title:t.title}})},l=[];function r(){return[{key:"id",align:"center",title:"编号"},{key:"remark",align:"center",title:"备注"},{key:"runDate",align:"center",title:"执行时间"},{key:"createTime",align:"center",title:"创建时间"}]}var c={current:1,size:10},i="自动任务执行日志",s={name:"autoLog",data:function(){return{api:this.$api.Pays,columns:r.call(this),params:c,title:i}},methods:{dataCallBack:function(t){return this.api.postWsCrontabLogPage(t).then((function(t){return{content:t.data.records,total:t.data.total}}))}}},o=s,u=a("2877"),d=Object(u["a"])(o,n,l,!1,null,"78c8eec8",null);e["default"]=d.exports}}]);