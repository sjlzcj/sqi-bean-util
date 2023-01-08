package test;

import com.sqi.beanutil.annotation.SqiBeanMapping;
import com.sqi.beanutil.annotation.SqiBeanMappings;
import com.sqi.beanutil.annotation.SqiParameter;

import java.util.Date;

public interface MyProcessorTest {
//    @SqiBeanMappings(mappings = {
//            @SqiBeanMapping()
//    })
//    void nonPara();

    @SqiBeanMappings(mappings = {
            @SqiBeanMapping(source = "SOURCE", target = "TARGET")
    })
    Date primaryPara(String a);

//    @SqiBeanMappings(mappings = {
//            @SqiBeanMapping(source = "objectPara_s", target = "objectPara_t")
//    })
//    void objectPara(Object obj);
//
//    @SqiBeanMappings(mappings = {
//            @SqiBeanMapping(source = "annotationPara_s", target = "annotationPara_t")
//    })
//    void annotationPara(@SqiParameter(name = "AAA") String s);
}
