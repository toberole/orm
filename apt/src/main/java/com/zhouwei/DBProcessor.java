package com.zhouwei;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.squareup.javapoet.TypeSpec.classBuilder;

/**
 * Created by zhouwei on 2017/12/23.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({// 被处理的注解的全限定名称
        "com.zhouwei.AutoIncrement", "com.zhouwei.DBEntity", "com.zhouwei.Primarykey", "com.zhouwei.Property"
})
public class DBProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private ClassName sqLiteOpenHelper_;
    private ClassName sqLiteDatabase_;
    private ClassName context_;
    private String packageName;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 获取所有的带有DB注解的类,一个类一张表
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(DBEntity.class);

        if (elements.size() == 0) return true;

        for (Element e : elements) {
            packageName = getPackageName((TypeElement) e);
        }

        // dbName,element 用户可以指定数据库的名字
        Map<String, List<Element>> dbs = new HashMap<>();

        //获取dbname
        for (Element e : elements) {
            DBEntity annation = e.getAnnotation(DBEntity.class);
            String dbName = annation.dbName();
            List<Element> es = dbs.get(dbName);
            if (null == es) {
                es = new ArrayList<>();
                dbs.put(dbName, es);
            }
            es.add(e);
        }

        create_Table_DB_Mapping(dbs);
        create_DBName_DB_Mapping(dbs);
        createDB_go(dbs);
        return true;
    }

    private void create_DBName_DB_Mapping(Map<String, List<Element>> dbs) {
        // 数据库名字与SQLiteOpenHelper之间的映射关系
        // String packageName = null;
        String dbName = null;
        for (Map.Entry<String, List<Element>> en : dbs.entrySet()) {
            dbName = en.getKey();
            List<Element> es = en.getValue();
            if (null == packageName && es.size() > 0) {
                packageName = getPackageName((TypeElement) es.get(0));
            }
            break;
        }

        // packageName.substring(0, packageName.lastIndexOf("."))
        ClassName dao = ClassName.get(/*packageName*/"com.dao", "SQLiteDB");

        MethodSpec getDaoSession = MethodSpec.methodBuilder("getDaoSession")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(sqLiteOpenHelper_)
                .addParameter(context_, "context")
                // 库名暂时写死 SQLiteDb
                // .addParameter(String.class, "dbName")
                // 库名暂时写死 SQLiteDb
                .addStatement("return new $T(context)", dao)
                .build();

        TypeSpec type = TypeSpec.classBuilder("DaoMaster")
                .addMethod(getDaoSession)
                .addModifiers(Modifier.PUBLIC)
                .build();

        JavaFile javaFile = JavaFile.builder(/*packageName.substring(0, packageName.lastIndexOf(".") + 1) + "dao"*/"com.dao", type).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void create_Table_DB_Mapping(Map<String, List<Element>> dbs) {
        // 表与库之间的映射关系
    }

    private void makeJavaFile(String packageName, TypeSpec type) {
        JavaFile javaFile = JavaFile.builder(/*packageName*/"com.dao", type).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 在同一个库里面的bean的得在一个包里面
    private void createDB_go(Map<String, List<Element>> dbs) {
        // log("createDB_go " + dbs.size());
        for (Map.Entry<String, List<Element>> en : dbs.entrySet()) {
            String dbname = en.getKey();
            List<String> sqls = new ArrayList<>();
            List<Element> value = en.getValue();
            StringBuilder logMsg = new StringBuilder();

            String packageName = getPackageName((TypeElement) value.get(0));
            logMsg.append(packageName);
            for (Element element : value) {
                String sql = makeSql((TypeElement) element);
                logMsg.append(sql);
                sqls.add(sql);
            }
            // log(logMsg.toString());

            MethodSpec constructor = makeConstructorMethod();
            MethodSpec onCreate = makeOnCreateMethod(sqls);
            MethodSpec onUpgrade = makeOnUpgradeMethod();

            TypeSpec type = makeClass(dbname, constructor, onCreate, onUpgrade);
            makeJavaFile(packageName, type);
        }

    }

    private TypeSpec makeClass(String className, MethodSpec constructor, MethodSpec onCreate, MethodSpec onUpgrade) {
        return classBuilder(className)
                .addMethod(constructor)
                .superclass(sqLiteOpenHelper_)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(onCreate).addMethod(onUpgrade)
                .build();
    }

    private MethodSpec makeOnUpgradeMethod() {
        return MethodSpec.methodBuilder("onUpgrade")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(sqLiteDatabase_, "db")
                .addParameter(int.class, "oldVersion")
                .addParameter(int.class, "newVersion")
                .build();
    }

    private MethodSpec makeOnCreateMethod(List<String> sqls) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(sqLiteDatabase_, "db");
        for (int i = 0; i < sqls.size(); i++) {
            builder.addStatement("db.execSQL(\"" + sqls.get(i) + "\")");
        }

        return builder.build();
    }

    private MethodSpec makeConstructorMethod() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(context_, "context")
                // 库名统一 SQLiteDb
                // .addParameter(String.class, "name")
                .addStatement("super(context, " + "\"SQLiteDb\"" + ", null, 1)")
                .build();
    }

    private String makeSql(TypeElement element) {
        // 构造sql语句 获取该类上所有成员[方法，属性，内部类...]
        List<? extends Element> members = elementUtils.getAllMembers(element);
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS");
        sb.append(" " + element.getSimpleName() + " (");

        StringBuilder logs = new StringBuilder();


        // 为类构建对应的表
        for (int i = 0; i < members.size(); i++) {
            Element e = members.get(i);

            // e.getKind().isField()属性
            // e.getKind().isClass() 类
            // e.getKind().isInterface() 接口
//            if (!e.getKind().isField() && !e.getKind().isClass() && !e.getKind().isInterface()) {
//                logs.append("方法：" + e.getSimpleName());
//            }

            List<Element> fields = new ArrayList<>();

            if (e.getKind().isField()) {
                logs.append("name: " + e.getSimpleName() + " ");
                logs.append("type: " + e.asType() + " ");

                createSql(sb, e);
            }
        }

        // log(logs.toString());
        return sb.substring(0, sb.length() - 1) + ")";
    }

    // 处理每一个带有注解的元素
    private void createSql(StringBuilder sb, Element e) {
        String memberName = e.getSimpleName().toString();

        // 获取该元素上的所有注解
        List<? extends AnnotationMirror> annotationMirrors = e.getAnnotationMirrors();
        if (annotationMirrors.size() == 1) {
            AnnotationMirror annotationMirror = annotationMirrors.get(0);
            String annotationName = annotationMirror.getAnnotationType().asElement().getSimpleName().toString();
            //(name===Property  type=String  name=dada  name===Primarykey  )
            if (Property.class.getSimpleName().equals(annotationName)) {
                // 处理Property注解上面的值
                Map<? extends ExecutableElement, ? extends AnnotationValue> values = annotationMirror.getElementValues();
                if (values != null && values.size() == 0) {
                    sb.append(memberName + " TEXT,");
                    return;
                }
                // annotationMirror 封装的是注解的 属性和值的键值对
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : values.entrySet()) {
                    ExecutableElement ee = en.getKey();
                    AnnotationValue av = en.getValue();
                    String annotationPramName = ee.getSimpleName().toString();// type
                    String annotationPramValue = av.getValue().toString().trim();//String
                    if ("type".equals(annotationPramName)) {
                        if ("String".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("byte".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("int".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("short".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("long".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("double".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                            sb.append(memberName + " TEXT,");
                        } else if ("float".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else {
                            sb.append(memberName + " TEXT,");
                        }
                    } else {
                        sb.append(memberName + " TEXT,");
                    }
                }
            } else if (Primarykey.class.getSimpleName().equals(annotationName)) {
                // 处理Primarykey注解上面的值
                Map<? extends ExecutableElement, ? extends AnnotationValue> values = annotationMirror.getElementValues();
                // annotationMirror 封装的是注解的 属性和值的键值对
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : values.entrySet()) {
                    ExecutableElement ee = en.getKey();
                    AnnotationValue av = en.getValue();
                    String annotationPramName = ee.getSimpleName().toString();// type
                    String annotationPramValue = av.getValue().toString().trim();//String
                    if ("type".equals(annotationPramName)) {
                        if ("String".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("byte".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("int".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("short".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("long".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else if ("double".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                            sb.append(memberName + " TEXT,");
                        } else if ("float".equals(annotationPramValue + "")) {
                            sb.append(memberName + " TEXT,");
                        } else {
                            sb.append(memberName + " TEXT,");
                        }
                    } else {
                        sb.append(memberName + " TEXT,");
                    }
                }
            } else if (AutoIncrement.class.getSimpleName().equals(annotationName)) {
                sb.append(memberName + " INTEGER PRIMARY KEY AUTOINCREMENT,");
            }
        }
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        sqLiteOpenHelper_ = ClassName.get("android.database.sqlite", "SQLiteOpenHelper");
        sqLiteDatabase_ = ClassName.get("android.database.sqlite", "SQLiteDatabase");
        context_ = ClassName.get("android.content", "Context");
    }

    private String getPackageName(TypeElement typeElement) {
        return elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    public void log(String msg) {
        MethodSpec methodSpec = MethodSpec.methodBuilder("log")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addStatement(msg)
                .build();
        TypeSpec type = TypeSpec.classBuilder("Log" + System.currentTimeMillis())
                .addMethod(methodSpec)
                .addModifiers(Modifier.PUBLIC)
                .build();
        makeJavaFile("com.zw", type);
    }
}
