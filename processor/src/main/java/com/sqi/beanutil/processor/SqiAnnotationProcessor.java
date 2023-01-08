package com.sqi.beanutil.processor;

import com.google.auto.service.AutoService;
import com.sqi.beanutil.annotation.SqiBeanMapping;
import com.sqi.beanutil.annotation.SqiBeanMappings;
import com.sqi.beanutil.generator.CodeGenerator;
import com.sqi.beanutil.generator.MappingInfo;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.Set;

@AutoService(Processor.class)
public class SqiAnnotationProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
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
        System.out.println("SQI.PROCESSOR--->" + getSupportedAnnotationTypes());
        MappingInfo mappingInfo = create();
        CodeGenerator.generate(mappingInfo);
        return true;
    }

    private MappingInfo create() {
        MappingInfo mappingInfo = null; // new MappingInfo();

        return mappingInfo;
    }
}
