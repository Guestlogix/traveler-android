package com.guestlogix.traveler_annotations_compiler;

import com.guestlogix.traveler_annotations.Keep;
import com.guestlogix.traveler_annotations.TravelerApiModel;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

public class Processor extends AbstractProcessor {

    private static final ClassName classObjectMappingFactory = ClassName.get("com.guestlogix.travelercorekit.network", "ObjectMappingFactory");
    private static final ClassName classObjectMappingException = ClassName.bestGuess("com.guestlogix.travelercorekit.network.ObjectMappingException");
    private static final ClassName classJsonReader = ClassName.bestGuess("android.util.JsonReader");
    private static final ClassName classJsonReaderHelper = ClassName.get("com.guestlogix.travelercorekit.utilities", "JsonReaderHelper");
    private static final ClassName classObjectMappingError = ClassName.bestGuess("com.guestlogix.travelercorekit.models.ObjectMappingError");
    private static final ClassName classTravelerErrorCode = ClassName.bestGuess("com.guestlogix.travelercorekit.models.TravelerErrorCode");

    private Filer filer;
    private Messager messager;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elements = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        if (!roundEnvironment.processingOver()) {

            for (Element element : roundEnvironment.getElementsAnnotatedWith(TravelerApiModel.class)) {

                String packageName = elements.getPackageOf(element).getQualifiedName().toString();
                String typeName = element.getSimpleName().toString();
                ClassName generatedClassName = ClassName
                        .get(packageName, NameStore.geApiModelClassName(typeName));

                // define class and construct the factory method.
                TypeSpec.Builder classBuilder = TypeSpec.classBuilder(generatedClassName)
                        .addSuperinterface(ParameterizedTypeName
                                .get(ClassName.bestGuess("com.guestlogix.travelercorekit.network.ObjectMappingFactory"),
                                        ClassName.get(element.asType())))
                        .addMethod(MethodSpec.methodBuilder("instantiate")
                                .addException(classObjectMappingException)
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(classJsonReader, "reader")
                                .returns(ClassName.get(element.asType()))
                                .beginControlFlow("try")
                                .addCode(getVariables(element))
                                .addStatement("reader.beginObject()")
                                .beginControlFlow("while (reader.hasNext())")
                                .addStatement("String key = reader.nextName()")
                                .beginControlFlow("switch (key)")
                                .addCode(generateCaseStatements(element))
                                .endControlFlow()
                                .endControlFlow()
                                .addStatement("reader.endObject()")
                                .addStatement(constructObjectStatement(element), element.asType())
                                .nextControlFlow("catch ($T e)", IOException.class)
                                .addStatement("throw new $T(new $T($T.INVALID_DATA, \"Invalid Json\"))",
                                        classObjectMappingException, classObjectMappingError, classTravelerErrorCode)
                                .endControlFlow()
                                .build())
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Keep.class);

                // write the defines class to a java file
                try {
                    JavaFile.builder(packageName,
                            classBuilder.build()).indent("    ")
                            .addStaticImport(classObjectMappingFactory, "*")
                            .build()
                            .writeTo(filer);
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.toString(), element);

                }
            }
        }

        return true;
    }

    private CodeBlock generateCaseStatements(Element element) {

        List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());
        CodeBlock.Builder localVariablesCodeBlockBuilder = CodeBlock
                .builder();

        for (Element subElement : fields) {
            localVariablesCodeBlockBuilder
                    .addStatement(String.format("case \"%s\": \n " +
                                    "%s =  $T.readNonNullString(reader)",
                            subElement.getSimpleName().toString(),
                            subElement.getSimpleName().toString()),
                            classJsonReaderHelper);
            localVariablesCodeBlockBuilder.addStatement("break");
        }
        localVariablesCodeBlockBuilder
                .addStatement(String.format("default: \n " +
                        "reader.skipValue()"));
        localVariablesCodeBlockBuilder.addStatement("break");

        return localVariablesCodeBlockBuilder.build();
    }

    private CodeBlock getVariables(Element element) {
        List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());

        CodeBlock.Builder localVariablesCodeBlockBuilder = CodeBlock
                .builder();
        String statement;
        for (Element subElement : fields) {
            statement = String.format("%s %s = \"\"", subElement.asType(), subElement.getSimpleName().toString());
            localVariablesCodeBlockBuilder.addStatement(statement);
        }
        return localVariablesCodeBlockBuilder.build();
    }

    private String constructObjectStatement(Element element) {
        List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());
        String statement = "";
        if (fields.size() > 0) {
            for (int i = 0; i <= fields.size() - 2; i++) {
                statement = statement.concat(String.format("%s, ", fields.get(i).getSimpleName().toString()));
            }
            statement = statement.concat(String.format("%s", fields.get(fields.size() - 1).getSimpleName().toString()));
        }
        statement = String.format("return new $T(%s)", statement);
        return statement;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(
                Keep.class.getCanonicalName(),
                TravelerApiModel.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
