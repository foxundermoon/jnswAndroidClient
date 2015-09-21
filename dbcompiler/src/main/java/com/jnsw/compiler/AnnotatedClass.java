package com.jnsw.compiler;

import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

class AnnotatedClass {
    public final String annotatedClassName;
    public final List<String> variableNames;
    public final TypeElement typeElement;

    public AnnotatedClass(TypeElement typeElement, List<String> variableNames) {
        this.annotatedClassName = typeElement.getSimpleName().toString();
        this.variableNames = variableNames;
        this.typeElement = typeElement;
    }

    public TypeMirror getType() {
        return typeElement.asType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n")
                .append("annotatedClassName:").append(annotatedClassName)
                .append("\n");
        sb.append("variableNames:[\n");
        for (String var : variableNames) {
            sb.append(var).append(",");
        }
        sb.append("]").append("\n");
        sb.append("}");
        return sb.toString();
    }
}
