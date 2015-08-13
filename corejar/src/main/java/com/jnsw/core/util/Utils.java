package com.jnsw.core.util;

import com.jnsw.core.exception.NoPackageNameException;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by foxundermoon on 2015/7/31.
 */
public class Utils {
    private Utils() {
        // no instances
    }
 public  static String getPackageName(Elements elementUtils, TypeElement type)
            throws NoPackageNameException {
        PackageElement pkg = elementUtils.getPackageOf(type);
        if (pkg.isUnnamed()) {
            throw new NoPackageNameException(type);
        }
        return pkg.getQualifiedName().toString();
    }
}
