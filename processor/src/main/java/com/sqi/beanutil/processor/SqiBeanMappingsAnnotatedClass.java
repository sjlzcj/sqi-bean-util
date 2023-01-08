package com.sqi.beanutil.processor;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

import com.sqi.beanutil.annotation.SqiBeanMappings;

/**
 * Holds the information about a class annotated with @Factory
 *
 * @author Hannes Dorfmann
 */
public class SqiBeanMappingsAnnotatedClass {

  private TypeElement annotatedClassElement;
  private String qualifiedGroupClassName;
  private String simpleFactoryGroupName;
  private String id;

  /**
   * @throws ProcessingException if id() from annotation is null
   */
  public SqiBeanMappingsAnnotatedClass(TypeElement classElement) throws ProcessingException {
    this.annotatedClassElement = classElement;
    SqiBeanMappings annotation = classElement.getAnnotation(SqiBeanMappings.class);
    // id = annotation.id();

//    if (StringUtils.isEmpty(id)) {
//      throw new ProcessingException(classElement,
//          "id() in @%s for class %s is null or empty! that's not allowed",
//          Factory.class.getSimpleName(), classElement.getQualifiedName().toString());
//    }

    // Get the full QualifiedTypeName
    try {
//      Class<?> clazz = annotation.type();
//      qualifiedGroupClassName = clazz.getCanonicalName();
//      simpleFactoryGroupName = clazz.getSimpleName();
    } catch (MirroredTypeException mte) {
      mte.printStackTrace();
      DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
      TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
      qualifiedGroupClassName = classTypeElement.getQualifiedName().toString();
      simpleFactoryGroupName = classTypeElement.getSimpleName().toString();
    }
  }

  /**
   * Get the id as specified in {@link Factory#id()}.
   * return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Get the full qualified name of the type specified in  {@link Factory#type()}.
   *
   * @return qualified name
   */
  public String getQualifiedFactoryGroupName() {
    return qualifiedGroupClassName;
  }

  /**
   * Get the simple name of the type specified in  {@link Factory#type()}.
   *
   * @return qualified name
   */
  public String getSimpleFactoryGroupName() {
    return simpleFactoryGroupName;
  }

  /**
   * The original element that was annotated with @Factory
   */
  public TypeElement getTypeElement() {
    return annotatedClassElement;
  }
}