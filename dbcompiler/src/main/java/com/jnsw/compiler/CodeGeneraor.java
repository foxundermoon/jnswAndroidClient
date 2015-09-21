package com.jnsw.compiler;

import com.j256.ormlite.table.DatabaseTable;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeName.get;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by foxundermoon on 2015/7/31.
 */
public class CodeGeneraor {
    private static final String CLASS_NAME = "DataTables";

    public static TypeSpec generateClass(List<AnnotatedClass> classes) throws ClassNotFoundException {
        TypeSpec.Builder builder = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC, FINAL);
        for (AnnotatedClass anno : classes) {
//            builder.addMethod(makeCreateStringMethod(anno));
            builder.addField(creatTableField(anno));
        }
        builder.addMethod(makeGetTableClazzs(classes));
//        builder.addMethod()
        return builder.build();
    }

    /**
     * @return a createString() method that takes annotatedClass's type as an input.
     */
    private static FieldSpec creatTableField(AnnotatedClass annotatedClass) throws ClassNotFoundException {
        return  FieldSpec.builder(Class.class,
//                firstCharToLower(annotatedClass.typeElement.getSimpleName().toString()) ,
                annotatedClass.typeElement.getAnnotation(DatabaseTable.class).tableName(),
                PUBLIC,STATIC ).initializer(annotatedClass.annotatedClassName+".class")
                .build();
    }

    private static String firstCharToLower(String input) {
        String firstChar = input.substring(0, 1).toLowerCase();
        return firstChar + input.substring(1, input.length());
    }

    private static MethodSpec makeGetTableClazzs(List<AnnotatedClass> classes) {
//        List<java.lang.reflect.Type> rt = new ArrayList<>();
//        for (AnnotatedClass annotatedClass : classes) {
//            rt.add(annotatedClass.typeElement.getQualifiedName().toString() +".class");
//        }
        ClassName list =ClassName.get(List.class);
        ClassName arrayList = ClassName.get(ArrayList.class);
        ClassName type = ClassName.get(Class.class);
        TypeName listOfType = ParameterizedTypeName.get(list, type);

        StringBuilder sb = new StringBuilder();
        sb.append(" java.util.List<Class> rt = new java.util.ArrayList<>();");
        for (AnnotatedClass annotatedClass : classes) {
            sb.append(" rt.add(").append(annotatedClass.typeElement.getQualifiedName().toString()).append(".class) ;");
        }
        sb.append("return rt");

        return methodBuilder("getTableClazzs")
                .addModifiers(PUBLIC, STATIC)
                .addStatement(sb.toString())
                .returns(listOfType)
                .build();
    }
    private static MethodSpec makeCreateStringMethod(AnnotatedClass annotatedClass) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("return \"%s{\" + ", annotatedClass.annotatedClassName));
        for (String variableName : annotatedClass.variableNames) {
            builder.append(String.format(" \"%s='\" + String.valueOf(instance.%s) + \"',\" + ",
                    variableName, variableName));
        }
        builder.append("\"}\"");
        return methodBuilder("createString")
                .addJavadoc("@return string suitable for {@param instance}'s toString()")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(get(annotatedClass.getType()), "instance")
                .addStatement(builder.toString())
                .returns(String.class)
                .build();
    }
}
