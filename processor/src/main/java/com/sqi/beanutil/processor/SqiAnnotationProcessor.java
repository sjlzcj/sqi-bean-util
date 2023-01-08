package com.sqi.beanutil.processor;

import com.google.auto.service.AutoService;
import com.sqi.beanutil.annotation.SqiBeanMapping;
import com.sqi.beanutil.annotation.SqiBeanMappings;
import com.sqi.beanutil.generator.CodeGenerator;
import com.sqi.beanutil.generator.MappingInfo;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class SqiAnnotationProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnv;

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, SqiBeanMappingsGroupedClasses> factoryClasses =
            new LinkedHashMap<String, SqiBeanMappingsGroupedClasses>();
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
        annotations.add(SqiBeanMapping.class.getCanonicalName());
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
                System.out.println("methodElement: " + methodElement);

//                SqiBeanMappingsAnnotatedClass annotatedClass = new SqiBeanMappingsAnnotatedClass(typeElement);
//
////                checkValidClass(annotatedClass);
//
//                // Everything is fine, so try to add
//                SqiBeanMappingsGroupedClasses factoryClass =
//                        factoryClasses.get(annotatedClass.getQualifiedFactoryGroupName());
//                if (factoryClass == null) {
//                    String qualifiedGroupName = annotatedClass.getQualifiedFactoryGroupName();
//                    factoryClass = new SqiBeanMappingsGroupedClasses(qualifiedGroupName);
//                    factoryClasses.put(qualifiedGroupName, factoryClass);
//                }
//
//                // Checks if id is conflicting with another @Factory annotated class with the same id
//                factoryClass.add(annotatedClass);
            }

            // Generate code
            for (SqiBeanMappingsGroupedClasses factoryClass : factoryClasses.values()) {
//                factoryClass.generateCode(elementUtils, filer);
            }
            factoryClasses.clear();
        } catch (ProcessingException e) {
            e.printStackTrace();
            error(e.getElement(), e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//            error(null, e.getMessage());
        }
        MappingInfo mappingInfo = create();
        CodeGenerator.generate(mappingInfo);
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

    private MappingInfo create() {
        MappingInfo mappingInfo = null; // new MappingInfo();

        return mappingInfo;
    }
}
