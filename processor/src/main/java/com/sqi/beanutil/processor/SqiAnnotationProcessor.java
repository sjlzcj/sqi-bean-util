package com.sqi.beanutil.processor;

import com.google.auto.service.AutoService;
import com.sqi.beanutil.annotation.SqiBeanMappings;
import com.sqi.beanutil.generator.*;
import com.sun.tools.javac.code.Symbol;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class SqiAnnotationProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnv;

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnv = processingEnv;
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(SqiBeanMappings.class.getCanonicalName());
//        annotations.add(SqiBeanMapping.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() == 0) {
            return true;
        }
        System.out.println("SQI.PROCESSOR--->" + getSupportedAnnotationTypes());
        try {
            System.out.println("annotations: " + annotations);
            System.out.println("roundEnv: " + roundEnv);
            // Scan classes
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SqiBeanMappings.class)) {
                System.out.println(String.format("annotatedElement>> owner: %s, name: %s", roundEnv.getRootElements(), annotatedElement.getSimpleName()));

                // Check if a method has been annotated with @SqiBeanMappings
                if (annotatedElement.getKind() != ElementKind.METHOD) {
                    throw new ProcessingException(annotatedElement, "Only classes can be annotated with @%s, not %s",
                            SqiBeanMappings.class.getSimpleName(), annotatedElement.getKind());
                }

                // We can cast it, because we know that it of ElementKind.CLASS
                System.out.println(annotatedElement.getClass());
//                com.sun.tools.javac.code.Symbol.MethodSymbol
                ExecutableElement methodElement = (ExecutableElement) annotatedElement;
                MappingInfo mappingInfo = create(methodElement);
                CodeGenerator.generate(mappingInfo);
            }

        } catch (ProcessingException e) {
            e.printStackTrace();
            error(e.getElement(), e.getMessage());
        }

        return true;
    }
    /**
     * Prints an error message
     *
     * @param e The element which has caused the error. Can be null
     * @param msg The error message
     */
    public void error(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg, e);
    }

    private MappingInfo create(ExecutableElement methodElement) {
        Symbol.MethodSymbol method = (Symbol.MethodSymbol)methodElement;

        SqiBeanMappings mappings = methodElement.getAnnotation(SqiBeanMappings.class);
        List<Mapping> mappingList = Arrays.stream(mappings.mappings())
                .map(mappingAnno -> {
                    String sourceFieldName = mappingAnno.source();
                    Class sourceFieldType = mappingAnno.sourceType();
                    String targetFieldName = mappingAnno.target();
                    Class targetFieldType = mappingAnno.targetType();
                    Class<? extends Function> convertProviderClass =  mappingAnno.convertProvider();
                    Class<? extends Function> formatClass =  mappingAnno.format();
                    return new Mapping(sourceFieldName, sourceFieldType, targetFieldName, targetFieldType, convertProviderClass, formatClass);
                }).collect(Collectors.toList());


        String interfaceName = method.owner.getQualifiedName().toString();
        Boolean withSameName = true;

        String methodName = method.getQualifiedName().toString();
        Class returnType = method.getReturnType().getClass();
        List<ParameterInfo> parameterInfo = method.getParameters().stream()
                .map(varSymbol -> {
                    String type = varSymbol.type.toString();
                    String name = varSymbol.getQualifiedName().toString();
                    return new ParameterInfo(name, type);
                }).collect(Collectors.toList());

        MethodInfo methodInfo = new MethodInfo(methodName, returnType, parameterInfo);

        MappingInfo mappingInfo =  new MappingInfo(interfaceName, methodInfo, withSameName, mappingList);
        return mappingInfo;
    }
}
