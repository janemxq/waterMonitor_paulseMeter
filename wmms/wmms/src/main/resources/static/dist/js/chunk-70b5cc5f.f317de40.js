(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-70b5cc5f"],{"25fe":function(t,e,a){"use strict";a.r(e);var r=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("Form",{ref:"form",attrs:{model:t.formValidate,rules:t.ruleValidate,"label-width":90}},[a("FormItem",{attrs:{label:"收费名称",prop:"displayName"}},[a("Input",{attrs:{placeholder:"请输入收费名称",clearable:""},model:{value:t.formValidate.displayName,callback:function(e){t.$set(t.formValidate,"displayName",e)},expression:"formValidate.displayName"}})],1),a("FormItem",{attrs:{label:"收费标准",prop:"feeStandard"}},[a("Row",[a("Col",{attrs:{span:"7"}},[t._v(" 用水区间(开始) ")]),a("Col",{attrs:{span:"7"}},[t._v(" 用水区间(结束) ")]),a("Col",{attrs:{span:"7"}},[t._v(" 水费标准 ")]),a("Col",{attrs:{span:"3"}},[t._v(" 操作 ")])],1),t._l(t.formValidate.feeStandard,(function(e,r){return a("Row",{key:r,staticStyle:{"margin-bottom":"25px"}},[a("Col",{attrs:{span:"6"}},[a("FormItem",{attrs:{prop:"feeStandard."+r+".start",rules:t.ruleValidate.start}},[a("Input",{attrs:{placeholder:"请输入开始区间"},model:{value:e.start,callback:function(a){t.$set(e,"start",a)},expression:"item.start"}})],1)],1),a("Col",{attrs:{span:"6",offset:"1"}},[a("FormItem",{attrs:{prop:"feeStandard."+r+".end",rules:t.ruleValidate.end}},[a("Input",{attrs:{placeholder:"请输入结束区间"},model:{value:e.end,callback:function(a){t.$set(e,"end",a)},expression:"item.end"}})],1)],1),a("Col",{attrs:{span:"6",offset:"1"}},[a("FormItem",{attrs:{prop:"feeStandard."+r+".money",rules:t.ruleValidate.money}},[a("Input",{attrs:{placeholder:"请输入水费标准"},model:{value:e.money,callback:function(a){t.$set(e,"money",a)},expression:"item.money"}})],1)],1),a("Col",{attrs:{span:"3",offset:"1"}},[a("Button",{attrs:{type:"error"},on:{click:function(e){return t.deletes(r)}}},[t._v("删除")])],1)],1)})),a("Row",[t.isStep?a("Col",{attrs:{span:"24"}},[a("Button",{attrs:{type:"success",long:""},on:{click:t.add}},[t._v("新增")])],1):t._e()],1)],2),a("FormItem",{attrs:{label:"阶梯计价",prop:"isStep"}},[a("i-switch",{on:{"on-change":t.isStepChange},model:{value:t.formValidate.isStep,callback:function(e){t.$set(t.formValidate,"isStep",e)},expression:"formValidate.isStep"}},[a("span",{attrs:{slot:"open"},slot:"open"},[t._v("是")]),a("span",{attrs:{slot:"close"},slot:"close"},[t._v("否")])])],1),a("FormItem",{attrs:{label:"排序",prop:"sortId"}},[a("Input",{attrs:{placeholder:"请输入排序",clearable:""},model:{value:t.formValidate.sortId,callback:function(e){t.$set(t.formValidate,"sortId",t._n(e))},expression:"formValidate.sortId"}})],1)],1)},n=[],s=(a("c5f6"),a("28a5"),a("ac6a"),a("456d"),{name:"add",props:{show:Boolean,data:Object,modalParams:Object},data:function(){var t=this;return{isStep:!1,formValidate:{displayName:"",sortId:"",oldFeeStandard:[],feeStandard:[{start:"",end:"",money:""}]},ruleValidate:{serial:[{required:!0,message:"请输入顺序",trigger:"blur",type:"number"},{validator:function(e,a,r){t.$rule.ruleNumber(e,a,r,5)},trigger:"blur",type:"number"}],displayName:[{required:!0,message:"请输入收费名称",trigger:"blur"},{validator:function(e,a,r){t.$rule.ruleLengthMin(e,a,r,[2,100])},trigger:"blur"}],start:[{required:!0,message:"请输入开始区间",trigger:"blur"},{validator:function(e,a,r){t.$rule.ruleNumbers(e,a,r,10)},trigger:"blur"}],end:[{required:!0,message:"请输入结束区间",trigger:"blur"},{validator:function(e,a,r){t.$rule.ruleNumbers(e,a,r,10)},trigger:"blur"}],money:[{required:!0,message:"请输入水费标准",trigger:"blur"},{validator:function(e,a,r){t.$rule.ruleNumbers(e,a,r,10)},trigger:"blur"}]}}},watch:{show:function(){this.init()}},mounted:function(){this.init()},methods:{init:function(){var t=this;if(this.show){if(this.data){Object.keys(this.formValidate).map((function(e){t.formValidate[e]=t.data[e]}));var e=this.formValidate.feeStandard.split("&").map((function(t){var e=t.split("-")[0],a=t.split("-")[1].split(",")[0],r=t.split("-")[1].split(",")[1];return{start:e,end:a,money:r}}));this.formValidate.isStep?this.oldFeeStandard=e:this.oldFeeStandard=[{start:"",end:"",money:""}],this.isStep=!!this.formValidate.isStep,Object.assign(this.formValidate,{isStep:!!this.formValidate.isStep,feeStandard:e})}}else this.handleReset(),this.oldFeeStandard=[{start:"",end:"",money:""}]},handleSubmit:function(){var t=this;return new Promise((function(e,a){t.$refs["form"].validate((function(r){r?e(Object.assign({},"edit"===t.modalParams.type&&t.data,t.formValidate,{feeStandard:t.formValidate.feeStandard.map((function(t){return"".concat(t.start,"-").concat(t.end,",").concat(t.money)})).join("&"),isStep:Number(t.formValidate.isStep)})):a("填写内容有误，请查验")}))}))},handleReset:function(){this.$refs["form"]&&this.$refs["form"].resetFields()},add:function(){this.formValidate.feeStandard.push({start:"",end:"",money:""})},deletes:function(t){this.formValidate.feeStandard.length<2?this.$Message.error("至少保留一条规则!"):this.formValidate.feeStandard.splice(t,1)},isStepChange:function(t){this.isStep=t,t?this.formValidate.feeStandard=this.oldFeeStandard:(this.oldFeeStandard=this.formValidate.feeStandard,this.formValidate.feeStandard=[{start:"1",end:"999999999",money:""}])}}}),o=s,i=a("2877"),l=Object(i["a"])(o,r,n,!1,null,"28927dc8",null);e["default"]=l.exports},"4bd1":function(t,e,a){"use strict";a.r(e);var r=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("tableTemplate",{attrs:{addModule:!0,handleSubmit:t.handleSubmit,modalParams:t.modalParams,columns:t.columns,seek:t.seek,params:t.params,dataCallBack:t.dataCallBack,title:t.title}},[a("addOrEdit",{ref:"add",attrs:{slot:"add",show:t.modalParams.show,modalParams:t.modalParams},slot:"add"}),a("addOrEdit",{ref:"edit",attrs:{slot:"edit",show:t.modalParams.show,data:t.roleData,modalParams:t.modalParams},slot:"edit"})],1)},n=[],s=(a("8e6e"),a("ac6a"),a("456d"),a("ade3")),o=a("67d3");function i(){var t=this;return[{key:"displayName",align:"center",title:"收费名称"},{key:"feeStandard",align:"center",title:"收费标准"},{key:"sortId",align:"center",title:"排序"},{key:"isStep",align:"center",title:"阶梯计价",render:o["b"]},{key:"",title:"操作",align:"center",width:180,render:function(e,a){return e("div",[e("Button",{props:{type:"warning",size:"small"},on:{click:function(){t.edit(a.row)}}},"修改"),e("Poptip",{style:{marginLeft:"8px",textAlign:"left",marginRight:"8px",display:"1"===a.row.deleted?"none":""},props:{confirm:!0,title:"是否确认删除"},on:{"on-ok":function(){t.handleDelete(a.row)}}},[e("Button",{props:{type:"error",size:"small"}},"删除")])])}}]}function l(){return[]}var d={current:1,size:10},c={show:!1,width:600,modalLoading:!0,type:"add",title:"新增",maskClosable:!1},u="收费标准",m=a("2f62");function f(t,e){var a=Object.keys(t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(t);e&&(r=r.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),a.push.apply(a,r)}return a}function p(t){for(var e=1;e<arguments.length;e++){var a=null!=arguments[e]?arguments[e]:{};e%2?f(Object(a),!0).forEach((function(e){Object(s["a"])(t,e,a[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(a)):f(Object(a)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(a,e))}))}return t}var h={name:"Rates",data:function(){return{columns:i.call(this),seek:l.call(this),params:d,modalParams:c,roleData:{},title:u,api:this.$api.System}},components:{addOrEdit:a("25fe").default},created:function(){},methods:p(p({},Object(m["e"])(["setCount"])),{},{dataCallBack:function(t){return this.api.getWsFeeStandard(t).then((function(t){return{content:t.data.records,total:t.data.total}}))},handleDelete:function(t){var e=this;this.api.delWsFeeStandard({id:t.id}).then((function(){e.$Message.success("删除收费标准".concat(t.displayName,"成功！")),e.setCount()}))},handleSubmit:function(){var t=this;return this.$refs[this.modalParams.type].handleSubmit().then((function(e){var a=t.modalParams.type;return"add"===a?t.api.addWsFeeStandard(e).then((function(a){return t.$Message.success("新增收费标准".concat(e.displayName,"成功！")),t.setCount(),Promise.resolve(a)})).catch((function(t){return Promise.reject(t)})):t.api.updateWsFeeStandard(e).then((function(a){return t.$Message.success("修改收费标准".concat(e.displayName,"成功！")),t.setCount(),Promise.resolve(a)})).catch((function(t){return Promise.reject(t)}))}))},edit:function(t){Object.assign(this.roleData,t),Object.assign(this.modalParams,{show:!0,width:600,modalLoading:!0,type:"edit",title:"修改"})}})},b=h,g=a("2877"),y=Object(g["a"])(b,r,n,!1,null,"e63afa14",null);e["default"]=y.exports},"67d3":function(t,e,a){"use strict";a.d(e,"b",(function(){return r})),a.d(e,"a",(function(){return n}));a("13cb");function r(t,e){return t("span",{style:{width:"12px",height:"12px",borderRadius:"50%",background:e.row[e.column.key]?"#1ba784":"#ff4000",display:"inline-block",boxShadow:"0 0 5px 0 ".concat(e.row[e.column.key]?"#1ba784":"#ff4000"),verticalAlign:"middle"}})}function n(t,e){return t("span",{},e.row.updateUserId?e.row.updateUserId:e.row.createUserId)}}}]);