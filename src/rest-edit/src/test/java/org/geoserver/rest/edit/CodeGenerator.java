package org.geoserver.rest.edit;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {
    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // String projectPath = System.getProperty("user.dir");
        String projectPath = "D:\\IdeaProjects\\gis\\geoserver\\src\\rest-edit";
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setFileOverride(true); // 是否覆盖已有文件
        gc.setBaseResultMap(true); // XML是否需要BaseResultMap
        gc.setBaseColumnList(true); // XML是否显示字段
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setAuthor("ily");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(
                "jdbc:postgresql://139.196.57.78:8040/yangzhou?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("org.postgresql.Driver");
        dsc.setUsername("sde");
        dsc.setPassword("sde");
        mpg.setDataSource(dsc);

        // 包配置,自定义输出包的位置
        PackageConfig pc = new PackageConfig();
        pc.setParent("org.geoserver.rest.edit");

        pc.setController("controller.test");
        pc.setEntity("entity.test");
        pc.setMapper("mapper.test");
        pc.setService("service.test");
        pc.setServiceImpl("service.test.impl");

        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg =
                new InjectionConfig() {
                    @Override
                    public void initMap() {
                        // to do nothing
                    }
                };

        // 如果模板引擎是 freemarker
        //  String templatePath = "/templates/mapper.xml.ftl";
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(
                new FileOutConfig(templatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                        return projectPath
                                + "/src/main/resources/mapper/"
                                + "/"
                                + tableInfo.getEntityName()
                                + "Mapper"
                                + StringPool.DOT_XML;
                    }
                });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类

        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        //  mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
