package com.icicles.wmms.tools;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动化代码生成，设计好表结构后，自动生成
 * 实体类、服务类、接口类、mapper类等
 * 方法有（分页、全部、根据id查询、增加、删除、根据id更新）
 * 注意以下内容修改变量（【baseProjectPath】、【authorName】、【tables】、【数据库配置四要素】）
 * 生成完成后，注意删除多余生成的字段，主要修改【服务实现类】和【entity.form】中内容
 */
public class MabatisPlusGenerator {

    //生成文件所在项目路径
    private static String baseProjectPath = "F:\\lisyworkspace\\java_code_workspace\\wmms";

    //基本包名
    private static String basePackage = "com.icicles";

    //作者
    private static String authorName = "";
    //要生成的表名
    private static String[] tables = {"sys_menu","sys_role_menus"};    //table前缀
    private static String prefix = "";

    //数据库配置四要素
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://106.13.231.226/wmms_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static String username = "root";
    private static String password = "111111";


    public static void main(String[] args) {


        AutoGenerator gen = new AutoGenerator();

        /**
         * 数据库配置
         */
        gen.setDataSource(new DataSourceConfig()
                .setDbType(DbType.MYSQL)
                .setDriverName(driverName)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setTypeConvert(new MySqlTypeConvert() {
                    // 自定义数据库表字段类型转换【可选】
                   /* @Override
                    public DbColumnType processTypeConvert(String fieldType) {
                        System.out.println("转换类型：" + fieldType);
                        // if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
                        //    return DbColumnType.BOOLEAN;
                        // }
                        return super.processTypeConvert(fieldType);
                    }*/
                }));

        /**
         * 全局配置
         */
        gen.setGlobalConfig(new GlobalConfig()
                .setOutputDir(baseProjectPath + "/src/main/java")//输出目录
                .setFileOverride(true)// 是否覆盖文件
                .setActiveRecord(false)// 开启 activeRecord 模式
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(true)// XML ResultMap
                .setBaseColumnList(true)// XML columList
                .setOpen(false)//生成后打开文件夹
                .setAuthor(authorName)
                // 自定义文件命名，注意 %s 会自动填充表实体属性！
                .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")
                .setSwagger2(true)
        );

        /**
         * 策略配置
         */
        gen.setStrategy(new StrategyConfig()
                        // .setCapitalMode(true)// 全局大写命名
                        //.setDbColumnUnderline(true)//全局下划线命名
                        .setTablePrefix(new String[]{prefix})// 此处可以修改为您的表前缀
                        .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
                        .setInclude(tables) // 需要生成的表
                        .setRestControllerStyle(true)
                        //.setExclude(new String[]{"test"}) // 排除生成的表
                        // 自定义实体父类
                        .setSuperEntityClass("com.icicles.wmms.entity.po.BasePo")
                        // 自定义实体，公共字段
                        .setSuperEntityColumns(new String[]{"id", "update_user_id", "update_time", "create_user_id", "create_time"})
                        //.setTableFillList(tableFillList)
                        // 自定义 mapper 父类 默认BaseMapper
                        //.setSuperMapperClass("com.baomidou.mybatisplus.mapper.BaseMapper")
                        // 自定义 service 父类 默认IService
                        // .setSuperServiceClass("com.baomidou.demo.TestService")
                        // 自定义 service 实现类父类 默认ServiceImpl
                        // .setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl")
                        // 自定义 controller 父类
                        //.setSuperControllerClass("com.kichun."+packageName+".controller.AbstractController")
                        // 【实体】是否生成字段常量（默认 false）
                        // public static final String ID = "test_id";
                        // .setEntityColumnConstant(true)
                        // 【实体】是否为构建者模型（默认 false）
                        // public User setName(String name) {this.name = name; return this;}
                        // .setEntityBuilderModel(true)
                        // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
                        .setEntityLombokModel(true)
                // Boolean类型字段是否移除is前缀处理
                //.setEntityBooleanColumnRemoveIsPrefix(true)
                // .setRestControllerStyle(true)
                // .setControllerMappingHyphenStyle(true)
        );

        /**
         * 包配置
         */
        gen.setPackageInfo(new PackageConfig()
                .setModuleName("wmms")
                .setParent(basePackage)// 自定义包路径
                .setController("controller")// 这里是控制器包名，默认 web
                .setEntity("entity.po")
                .setMapper("DAO")
                .setService("service")
                .setServiceImpl("service.impl")
                .setXml("mapper")
        );

        /**
         * 注入自定义配置
         */
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig abc = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        //自定义文件输出位置（非必须）
        List<FileOutConfig> fileOutList = new ArrayList<>();
        fileOutList.add(new FileOutConfig("template/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseProjectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + ".xml";
            }
        });
        fileOutList.add(new FileOutConfig("template/entity.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseProjectPath + "/src/main/java/com/icicles/wmms/entity/po/" + tableInfo.getEntityName() + ".java";
            }
        });
        fileOutList.add(new FileOutConfig("template/entityForm.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseProjectPath + "/src/main/java/com/icicles/wmms/entity/form/" + tableInfo.getEntityName() + "Form.java";
            }
        });
        fileOutList.add(new FileOutConfig("template/entityQueryForm.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseProjectPath + "/src/main/java/com/icicles/wmms/entity/form/" + tableInfo.getEntityName() + "QueryForm.java";
            }
        });
        fileOutList.add(new FileOutConfig("template/entityQueryParam.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseProjectPath + "/src/main/java/com/icicles/wmms/entity/param/" + tableInfo.getEntityName() + "QueryParam.java";
            }
        });
        abc.setFileOutConfigList(fileOutList);
        gen.setCfg(abc);

        /**
         * 指定模板引擎 默认是VelocityTemplateEngine ，需要引入相关引擎依赖
         */
        gen.setTemplateEngine(new FreemarkerTemplateEngine());

        /**
         * 模板配置
         */
        gen.setTemplate(
                // 关闭默认 xml 生成，调整生成 至 根目录
                new TemplateConfig().setXml(null)
                        // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
                        // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
                        .setController("template/controller.java")
                        .setEntity(null)
                        .setMapper("template/mapper.java")
                        //.setXml("template/mapper.xml")
                        .setService("template/service.java")
                        .setServiceImpl("template/serviceImpl.java")
        );

        // 执行生成
        gen.execute();
    }
}
