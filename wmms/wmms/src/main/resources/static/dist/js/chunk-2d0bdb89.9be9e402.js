(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0bdb89"],{"2cce":function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("tableTemplate",{attrs:{addModule:!1,columns:e.columns,seek:e.seek,params:e.params,dataCallBack:e.dataCallBack,title:e.title}})},i=[];a("7f7f");function l(){return[{key:"villageName",align:"center",title:"村名称"},{key:"userName",align:"center",title:"用户名称"},{key:"phone",align:"center",title:"手机号"},{key:"cost",align:"center",title:"水费"},{key:"balance",align:"center",title:"余额"},{key:"accountNum",align:"center",title:"收款金额"},{key:"createTime",align:"center",title:"充值时间"}]}function c(){var e=this;return[{type:"seek",key:"userSn",title:"用户名",data:[],remoteMethod:function(t){e.seek[0].loading=!0,e.sysApi.findUser({name:t,current:1,size:99}).then((function(t){e.seek[0].data=t.data.records.map((function(e){return e.value=e.sn,e.name="".concat(e.name,"(").concat(e.phone||e.villageName,")"),e}))})).finally((function(){e.seek[0].loading=!1}))},loading:!1},{key:"phone",type:"input",title:"手机号"},{key:"accountNum",type:"input",title:"收款金额"},{key:"balance",type:"input",title:"余额"},{type:"time",month:"datetime",key:"createdTimeStart",title:"开始时间",format:"yyyy-MM-dd HH:mm:ss"},{type:"time",key:"createdTimeEnd",month:"datetime",title:"结束时间",format:"yyyy-MM-dd HH:mm:ss"}]}var r={current:1,size:10},s="充值记录",o={name:"topUpLog",data:function(){return{api:this.$api.Pays,sysApi:this.$api.System,columns:l.call(this),seek:c.call(this),params:r,title:s}},methods:{dataCallBack:function(e){return this.api.postWsChargePage(e).then((function(e){return{content:e.data.records,total:e.data.total}}))}}},u=o,m=a("2877"),d=Object(m["a"])(u,n,i,!1,null,"58ca1e73",null);t["default"]=d.exports}}]);