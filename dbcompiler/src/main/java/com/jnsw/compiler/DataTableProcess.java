package com.jnsw.compiler;

import com.google.auto.service.AutoService;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.core.anotation.RemoteTable;
import com.jnsw.core.exception.NoPackageNameException;
import com.jnsw.core.util.Utils;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static com.jnsw.core.util.Utils.getPackageName;
import static com.squareup.javapoet.JavaFile.builder;
import static java.util.Collections.singleton;
import static javax.tools.Diagnostic.Kind.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Created by foxundermoon on 2015/7/30.
 */
@AutoService(Processor.class)
public class DataTableProcess extends AbstractProcessor {
    private Messager messager;
    private   Logger logger;
    Elements elementUtils;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        logger = LogManager.getLogger(this.getClass());
        elementUtils = processingEnv.getElementUtils();

    }

    private static final String DataTableAnnotation = "@" + DatabaseTable.class.getSimpleName();
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        ArrayList<AnnotatedClass> annotatedClasses = new ArrayList<>();
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(DatabaseTable.class)) {
            // Our annotation is defined with @Target(value=TYPE). Therefore, we can assume that
            // this annotatedElement is a TypeElement.
            if (annotatedElement.getKind() == ElementKind.CLASS) {
                TypeElement annotatedClass = (TypeElement) annotatedElement;
                if (!isValidClass(annotatedClass)) {
                    return true;
                }
                try {
                    annotatedClasses.add(buildAnnotatedClass(annotatedClass));
                    logger.debug(new StringFormattedMessage(">>>>>>>>>>>>>>>>>>>>>>>" + annotatedClass.getSimpleName()));
                } catch (NoPackageNameException | IOException e) {
                    String message = String.format("Couldn't process class %s: %s", annotatedClass,
                            e.getMessage());
                    messager.printMessage(ERROR, message, annotatedElement);
                }
            }

        }
        for (AnnotatedClass clazz : annotatedClasses) {
            messager.printMessage(Diagnostic.Kind.NOTE,"<<<<<<<<<<<<<<<<<<----"+clazz.toString());
        }
        try {
            generate(annotatedClasses);
        } catch (NoPackageNameException | IOException e) {
            messager.printMessage(ERROR, "Couldn't generate class");
        }
        return true;
    }
    private boolean isValidClass(TypeElement annotatedClass) {
//
//        if (!ClassValidator.isPublic(annotatedClass)) {
//            String message = String.format("Classes annotated with %s must be public.",
//                    DataTableAnnotation);
//            messager.printMessage(ERROR, message, annotatedClass);
//            return false;
//        }
//        if (ClassValidator.isAbstract(annotatedClass)) {
//            String message = String.format("Classes annotated with %s must not be abstract.",
//                    DataTableAnnotation);
//            messager.printMessage(ERROR, message, annotatedClass);
//            return false;
//        }
        return true;
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> rt = new HashSet<>();
        rt.add(DatabaseTable.class.getCanonicalName());
        rt.add(RemoteTable.class.getCanonicalName());
        return rt;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
    private AnnotatedClass buildAnnotatedClass(TypeElement annotatedClass)
            throws NoPackageNameException, IOException {
        ArrayList<String> variableNames = new ArrayList<>();
        for (Element element : annotatedClass.getEnclosedElements()) {
            if (!(element instanceof VariableElement)) {
                continue;
            }
            VariableElement variableElement = (VariableElement) element;
            variableNames.add(variableElement.getSimpleName().toString());
        }
        return new AnnotatedClass(annotatedClass , variableNames);
    }
    private void generate(List<AnnotatedClass> annos) throws NoPackageNameException, IOException {
        if (annos.size() == 0) {
            return;
        }
        String packageName = getPackageName(processingEnv.getElementUtils(),
                annos.get(0).typeElement);
        TypeSpec generatedClass = null;
        try {
            generatedClass = CodeGeneraor.generateClass(annos);
            JavaFile javaFile = builder(packageName, generatedClass).build();
            javaFile.writeTo(processingEnv.getFiler());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            messager.printMessage(ERROR,e.getMessage());
        }


    }
}
