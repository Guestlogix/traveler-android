package com.guestlogix.traveler_annotations_compiler;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ProcessingUtils {

    private ProcessingUtils() {
        // not to be instantiated in public
    }
    public static Set<TypeElement> getSubElementsToProcess(Set<? extends Element> elements,
                                                           Set<? extends Element> supportedAnnotations) {
        Set<TypeElement> typeElements = new HashSet<>();
        log("Elements: " + elements.size());
        for (Element element : elements) {
            if (element instanceof TypeElement) {
                log(" ");
                log("Type Element: " + element.getSimpleName().toString());
                boolean found = false;
                for (Element subElement : element.getEnclosedElements()) {
                    log("Sub Element: " + subElement.getSimpleName().toString());
                    for (AnnotationMirror mirror : subElement.getAnnotationMirrors()) {
                        log("Annotation Mirror: " + mirror.toString());
                        for (Element annotation : supportedAnnotations) {
                            if (mirror.getAnnotationType().asElement().equals(annotation)) {
                                typeElements.add((TypeElement) element);
                                found = true;
                                break;
                            }
                        }
                        if (found) break;
                    }
                    if (found) break;
                }
            }
        }
        return typeElements;
    }

    static void log(String log) {
        try {
            OutputStream logOutputStream = new FileOutputStream(new File("traveler_annotations/build/annotations_logs.txt"), true);
            log = log.concat("\n");
            logOutputStream.write(log.getBytes(), 0, log.length());
            logOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void clearLogs() {
        try {
            OutputStream logOutputStream = new FileOutputStream(new File("traveler_annotations/build/annotations_logs.txt"), false);
            logOutputStream.write("".getBytes(), 0, "".length());
            logOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
