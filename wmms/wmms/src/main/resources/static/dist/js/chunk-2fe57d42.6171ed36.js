(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2fe57d42"],{"16dd":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("tableTemplate",{attrs:{slot:"right",addModule:!0,handleSubmit:e.handleSubmit,modalParams:e.modalParams,columns:e.columns,seek:e.seek,params:e.params,dataCallBack:e.dataCallBack,title:e.title,titleSlot:!0,selectChange:e.onSelectFn},slot:"right"},[a("label",{staticClass:"titleLabel",attrs:{slot:"fun"},slot:"fun"},[a("a",{attrs:{href:"javascript:void(0)"},on:{click:e.deleteFn}},[e._v("多项删除")]),a("a",{attrs:{href:"javascript:void(0)"},on:{click:e.add}},[e._v("新增")])]),a("addOrEdit",{ref:"add",attrs:{slot:"add",show:e.modalParams.show,modalParams:e.modalParams},slot:"add"}),a("addOrEdit",{ref:"edit",attrs:{slot:"edit",show:e.modalParams.show,data:e.roleData,modalParams:e.modalParams},slot:"edit"}),a("editPassword",{ref:"editPassword",attrs:{slot:"editPassword",show:e.modalParams.show,data:e.roleData,modalParams:e.modalParams},slot:"editPassword"})],1)},l=[],r=(a("8e6e"),a("ac6a"),a("456d"),a("7f7f"),a("20d6"),a("ade3"));function s(){var e=this;return[{type:"selection",width:60,align:"center"},{key:"name",align:"center",title:"名称"},{key:"villageName",align:"center",title:"所在村落"},{key:"typeName",align:"center",title:"类型"},{key:"idCard",align:"center",title:"身份证"},{key:"phone",align:"center",title:"手机号"},{key:"loginAccount",align:"center",title:"账号"},{key:"updateTime",align:"center",title:"最后修改时间"},{key:"remark",align:"center",title:"备注"},{key:"",title:"操作",align:"center",width:220,render:function(t,a){return t("div",[t("Button",{props:{type:"warning",size:"small"},on:{click:function(){e.edit(a.row)}}},"修改"),t("Button",{props:{type:"success",size:"small"},style:{marginLeft:"8px",textAlign:"left",display:"1"===a.row.deleted?"none":""},on:{click:function(){e.editPassword(a.row)}}},"重置密码"),t("Poptip",{style:{marginLeft:"8px",textAlign:"left",marginRight:"8px",display:"1"===a.row.deleted?"none":""},props:{confirm:!0,title:"是否确认删除"},on:{"on-ok":function(){e.handleDelete(a.row)}}},[t("Button",{props:{type:"error",size:"small"}},"删除")])])}}]}function o(){return[{key:"villageSn",title:"村名",type:"select",data:[]},{key:"name",title:"姓名",type:"input"},{key:"phone",title:"手机号",type:"input"}]}var n={current:1,size:10},d={show:!1,width:600,modalLoading:!0,type:"add",title:"新增",maskClosable:!1},c="用户列表",u=a("2f62"),m=a("23f4");function f(e,t){var a=Object.keys(e);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);t&&(i=i.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),a.push.apply(a,i)}return a}function p(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?arguments[t]:{};t%2?f(Object(a),!0).forEach((function(t){Object(r["a"])(e,t,a[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(a)):f(Object(a)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(a,t))}))}return e}var h={name:"Village",data:function(){return{columns:s.call(this),seek:o.call(this),params:n,modalParams:d,roleData:{},title:c,api:this.$api.Org,deleteData:[]}},components:{addOrEdit:a("40f1").default,editPassword:a("6812").default,groupTree:m["a"],layout:m["b"]},created:function(){var e=this;this.api.getWsVillageAll().then((function(t){e.seek[0].data=t.data.map((function(e){return{value:e.sn,name:e.villageName}}))}))},methods:p(p({},Object(u["e"])(["setCount"])),{},{dataCallBack:function(e){return this.api.getTSysUser(e).then((function(e){return{content:e.data.records,total:e.data.total}}))},onSelectFn:function(e){console.log(e),this.deleteData=e},add:function(){Object.assign(this.modalParams,{show:!0,type:"add",title:"新增"})},deleteFn:function(){var e=this;if(this.deleteData.length){var t=[];this.deleteData.findIndex((function(e){t.push(e.id)})),this.api.postSysUserMultiDel(t).then((function(t){console.log(t),e.setCount(),e.$Message.success("删除删除成功!"),e.deleteData=[]}))}else this.$Message.error("请先选择要删除的用户!")},handleDelete:function(e){var t=this;this.api.delTSysUser({id:e.id}).then((function(){t.$Message.success("删除用户".concat(e.name,"成功！")),t.setCount()}))},handleSubmit:function(){var e=this;return this.$refs[this.modalParams.type].handleSubmit().then((function(t){var a=e.modalParams.type;return"add"===a?e.api.addTSysUser(t).then((function(a){return e.$Message.success("新增用户".concat(t.name,"成功！")),e.setCount(),Promise.resolve(a)})).catch((function(e){return Promise.reject(e)})):e.api.updateTSysUser(t).then((function(i){return"edit"===a&&e.$Message.success("修改用户".concat(t.name,"成功！")),"editPassword"===a&&e.$Message.success("重置用户".concat(t.name,"的密码成功！")),e.setCount(),Promise.resolve(i)})).catch((function(e){return Promise.reject(e)}))}))},edit:function(e){Object.assign(this.roleData,e),Object.assign(this.modalParams,{show:!0,width:600,modalLoading:!0,type:"edit",title:"修改"})},editPassword:function(e){Object.assign(this.roleData,e),Object.assign(this.modalParams,{show:!0,width:600,modalLoading:!0,type:"editPassword",title:"重置密码"})}})},g=h,b=(a("84bd"),a("2877")),v=Object(b["a"])(g,i,l,!1,null,"28c8f8e9",null);t["default"]=v.exports},"40f1":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("Form",{ref:"form",attrs:{model:e.formValidate,rules:e.ruleValidate,"label-width":90}},[a("FormItem",{attrs:{label:"登录账号",prop:"loginAccount"}},[a("Input",{attrs:{placeholder:"请输入登录账号",clearable:""},model:{value:e.formValidate.loginAccount,callback:function(t){e.$set(e.formValidate,"loginAccount",t)},expression:"formValidate.loginAccount"}})],1),"edit"!==e.modalParams.type?a("FormItem",{attrs:{label:"登录密码",prop:"loginPass"}},[a("Input",{attrs:{placeholder:"请输入登录密码(默认111111)",type:"password",password:""},model:{value:e.formValidate.loginPass,callback:function(t){e.$set(e.formValidate,"loginPass",t)},expression:"formValidate.loginPass"}})],1):e._e(),a("FormItem",{attrs:{label:"姓名",prop:"name"}},[a("Input",{attrs:{placeholder:"请输入姓名",clearable:""},model:{value:e.formValidate.name,callback:function(t){e.$set(e.formValidate,"name",t)},expression:"formValidate.name"}})],1),a("FormItem",{attrs:{label:"村庄",prop:"villageId"}},[a("Select",{on:{"on-change":e.onChange},model:{value:e.formValidate.villageId,callback:function(t){e.$set(e.formValidate,"villageId",e._n(t))},expression:"formValidate.villageId"}},e._l(e.VillageAll,(function(t){return a("Option",{key:t.id,attrs:{value:t.id}},[e._v(e._s(t.villageName))])})),1)],1),a("FormItem",{attrs:{label:"手机号",prop:"phone"}},[a("Input",{attrs:{placeholder:"请输入手机号",clearable:""},model:{value:e.formValidate.phone,callback:function(t){e.$set(e.formValidate,"phone",t)},expression:"formValidate.phone"}})],1),a("FormItem",{attrs:{label:"身份证号",prop:"idCard"}},[a("Input",{attrs:{placeholder:"请输入身份证号",clearable:""},model:{value:e.formValidate.idCard,callback:function(t){e.$set(e.formValidate,"idCard",t)},expression:"formValidate.idCard"}})],1),a("FormItem",{attrs:{label:"是否有效",prop:"isValid"}},[a("i-switch",{attrs:{size:"large"},model:{value:e.formValidate.isValid,callback:function(t){e.$set(e.formValidate,"isValid",t)},expression:"formValidate.isValid"}},[a("span",{attrs:{slot:"open"},slot:"open"},[e._v("有效")]),a("span",{attrs:{slot:"close"},slot:"close"},[e._v("无效")])])],1),a("FormItem",{attrs:{label:"用户角色",prop:"type"}},[a("RadioGroup",{model:{value:e.formValidate.type,callback:function(t){e.$set(e.formValidate,"type",e._n(t))},expression:"formValidate.type"}},e._l(e.typeList,(function(t){return a("Radio",{key:t.id,attrs:{label:t.id}},[a("span",[e._v(e._s(t.roleName))])])})),1)],1),a("FormItem",{attrs:{label:"备注",prop:"remark"}},[a("Input",{attrs:{clearable:"",type:"textarea",placeholder:"请输入备注",rows:3,autosize:{minRows:2,maxRows:6}},model:{value:e.formValidate.remark,callback:function(t){e.$set(e.formValidate,"remark",t)},expression:"formValidate.remark"}})],1)],1)},l=[],r=(a("20d6"),a("6b54"),a("ac6a"),a("456d"),{name:"add",props:{show:Boolean,data:Object,modalParams:Object},data:function(){var e=this;return{VillageAll:[],api:this.$api.System,typeList:[],formValidate:{loginAccount:"",loginPass:"",name:"",phone:"",remark:"",isValid:!0,type:0,villageId:"",idCard:"",villageName:""},ruleValidate:{loginAccount:[{required:!0,message:"请输入登录账号",trigger:"blur"},{validator:function(t,a,i){e.$rule.ruleNoChinese(t,a,i,200)},type:"number",trigger:"blur"}],loginPass:[{validator:function(t,a,i){e.$rule.nonePassword(t,a,i,[6,18])},trigger:"blur"}],name:[{required:!0,message:"请输入姓名",trigger:"blur"},{validator:function(t,a,i){e.$rule.ruleLengthMin(t,a,i,[3,20])},trigger:"blur"}],phone:[{validator:this.$rule.validateContact,trigger:"blur"}],idCard:[{required:!0,message:"请输入身份证号",trigger:"blur"},{validator:this.$rule.validateIdCard,trigger:"blur"}],remark:[{validator:function(t,a,i){e.$rule.ruleLength(t,a,i,500)},trigger:"blur"}]}}},watch:{show:function(){this.init()}},mounted:function(){this.init()},methods:{init:function(){var e=this;this.show?(this.getVillageAll(),this.data&&(Object.keys(this.formValidate).map((function(t){e.formValidate[t]=e.data[t]})),Object.assign(this.formValidate,{isValid:"number"!==typeof this.data.isValid||!!this.data.isValid,serial:"number"===typeof this.data.serial?this.data.serial.toString():this.data.isValid||""}))):this.handleReset()},getVillageAll:function(){var e=this;this.api.getVillageAll().then((function(t){e.VillageAll=t.data})),this.api.getRoleAll().then((function(t){e.typeList=t.data}))},onChange:function(e){var t=this.VillageAll.findIndex((function(t){return t.id===e}));this.formValidate.villageName=this.VillageAll[t].villageName},handleSubmit:function(){var e=this;return new Promise((function(t,a){e.$refs["form"].validate((function(i){i?t(Object.assign({},"edit"===e.modalParams.type&&e.data,"add"===e.modalParams.type&&{createUserId:localStorage.getItem("userId")},e.formValidate,"edit"===e.modalParams.type&&{updateUserId:localStorage.getItem("userId"),loginPass:""},{isValid:e.formValidate.isValid?1:0})):a("填写内容有误，请查验")}))}))},handleReset:function(){this.$refs["form"]&&this.$refs["form"].resetFields()}}}),s=r,o=a("2877"),n=Object(o["a"])(s,i,l,!1,null,"1d7a9a92",null);t["default"]=n.exports},6812:function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("Form",{ref:"form",attrs:{model:e.formValidate,rules:e.ruleValidate,"label-width":90}},["edit"!==e.modalParams.type?a("FormItem",{attrs:{label:"登录密码",prop:"loginPass"}},[a("Input",{attrs:{placeholder:"请输入登录密码",type:"password",password:""},model:{value:e.formValidate.loginPass,callback:function(t){e.$set(e.formValidate,"loginPass",t)},expression:"formValidate.loginPass"}})],1):e._e()],1)},l=[],r=(a("ac6a"),a("456d"),{name:"add",props:{show:Boolean,data:Object,modalParams:Object},data:function(){var e=this;return{formValidate:{loginPass:""},ruleValidate:{loginPass:[{required:!0,message:"请输入密码",trigger:"blur"},{validator:function(t,a,i){e.$rule.nonePassword(t,a,i,[6,18])},trigger:"blur"}]}}},watch:{show:function(){this.init()}},mounted:function(){this.init()},methods:{init:function(){var e=this;this.show?this.data&&(Object.keys(this.formValidate).map((function(t){e.formValidate[t]=e.data[t]})),Object.assign(this.formValidate,{loginPass:""})):this.handleReset()},handleSubmit:function(){var e=this;return new Promise((function(t,a){e.$refs["form"].validate((function(i){i?t(Object.assign({},e.data,e.formValidate)):a("填写内容有误，请查验")}))}))},handleReset:function(){this.$refs["form"]&&this.$refs["form"].resetFields()}}}),s=r,o=a("2877"),n=Object(o["a"])(s,i,l,!1,null,"8129d9fa",null);t["default"]=n.exports},"84bd":function(e,t,a){"use strict";var i=a("ca82"),l=a.n(i);l.a},ca82:function(e,t,a){}}]);