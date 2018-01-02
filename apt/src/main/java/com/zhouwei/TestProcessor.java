package com.zhouwei;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;


//@AutoService(Processor.class)
//@SupportedAnnotationTypes({
//        "com.example.ProxyUtils"
//})


public class TestProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // 生成目标类
        TypeSpec.Builder type = TypeSpec.classBuilder("TestClass")
                .addModifiers(Modifier.PUBLIC);

        // 生成方法
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args");

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ProxyUtils.class);
        for (TypeElement e : ElementFilter.typesIn(elements)) {
            // 构建代码块
            CodeBlock codeBlock = CodeBlock.builder()
                    .addStatement("$T.out.println(\"$L + $L\")", System.class,
                            e.getAnnotation(ProxyUtils.class).value(), e.getSimpleName())
                    .build();
            // 将代码块加入方法
            methodBuilder.addCode(codeBlock);
        }

        // 将方法加入类
        type.addMethod(methodBuilder.build());

        JavaFile jf = JavaFile.builder("com.example.apt", type.build()).build();
        try {
            jf.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
