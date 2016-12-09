package io.darkcraft.apt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import com.google.auto.service.AutoService;

@AutoService(Processor.class)
public final class CommonProxyGenerator extends AbstractProcessor
{
	private Messager messager;
	
	@Override
	public void init(ProcessingEnvironment environment)
	{
		super.init(environment);
		messager = environment.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	{
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(CommonProxy.class);
		for(Element e : elements)
			messager.printMessage(Kind.ERROR, e.getAnnotation(CommonProxy.class).toString(), e);
		return false;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes()
	{
		return new HashSet<String>(Arrays.asList(CommonProxy.class.getName()));
	}
}
