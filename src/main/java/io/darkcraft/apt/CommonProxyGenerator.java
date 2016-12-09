package io.darkcraft.apt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import com.google.auto.service.AutoService;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public final class CommonProxyGenerator extends AbstractProcessor
{
	private Messager messager;
	private TypeElement annot;
	
	private Elements eutils;
	
	@Override
	public void init(ProcessingEnvironment environment)
	{
		super.init(environment);
		messager = environment.getMessager();
		eutils = environment.getElementUtils();
		annot = environment.getElementUtils().getTypeElement(CommonProxy.class.getName());
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	{
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(CommonProxy.class);
		for(Element e : elements)
		{
			TypeElement te = (TypeElement)e;
			String name = eutils.getBinaryName(te).toString();
			messager.printMessage(Kind.WARNING, name, e);
			messager.printMessage(Kind.NOTE, "Note?");
			for(Element e2 : e.getEnclosedElements())
			{
				ClientMethod cm = e2.getAnnotation(ClientMethod.class);
				if(cm == null)
					continue;
				name = e2.getSimpleName().toString();
				messager.printMessage(Kind.WARNING, name, e2);
			}
		}
		
		if(annotations.contains(annot))
			return true;
		return false;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes()
	{
		return new HashSet<String>(Arrays.asList(CommonProxy.class.getName()));
	}
}
