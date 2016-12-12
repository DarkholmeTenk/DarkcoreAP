package io.darkcraft.apt;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public final class CommonProxyGenerator extends AbstractProcessor
{
	private static final String NBT = "net.minecraft.nbt.NBTTagCompound";
	private static final String PACKET = "io.darkcraft.darkcore.mod.handlers.packets.ProxyHandler";
	
	private Messager messager;
	private TypeElement annot;
	
	private Elements eutils;
	private Filer filer;
	
	@Override
	public void init(ProcessingEnvironment environment)
	{
		super.init(environment);
		messager = environment.getMessager();
		eutils = environment.getElementUtils();
		annot = environment.getElementUtils().getTypeElement(CommonProxy.class.getName());
		filer = environment.getFiler();
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
			Set<ExecutableElement> methods = Sets.newLinkedHashSet();
			for(Element e2 : e.getEnclosedElements())
			{
				if(e2 instanceof ExecutableElement)
				{
					
					ClientMethod cm = e2.getAnnotation(ClientMethod.class);
					if(cm == null)
						continue;
					name = e2.getSimpleName().toString();
					messager.printMessage(Kind.WARNING, name, e2);
					methods.add((ExecutableElement) e2);
				}
			}
			buildClass(te, methods);
		}
		
		if(annotations.contains(annot))
			return true;
		return false;
	}
	
	private void buildClass(TypeElement te, Set<ExecutableElement> methods)
	{
		String packageName = eutils.getPackageOf(te).getQualifiedName().toString();
		String newName = eutils.getBinaryName(te).toString()+"Impl";
		messager.printMessage(Kind.WARNING, packageName + "  -  " + newName); 
		try {
			
			TypeSpec.Builder clazz = TypeSpec.classBuilder(newName)
					.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
			for(ExecutableElement method : methods)
			{
				ClientMethod annot = method.getAnnotation(ClientMethod.class);
				MethodSpec.Builder b = MethodSpec.overriding(method);
				b.addStatement("%s tag = io.darkcraft.darkcore.mod.nbt.NBTHelper.serialise(%s)",
						NBT, MethodHelper.getParametersNames(method, annot.broadcast().skipFirst));
				switch(annot.broadcast())
				{
				case ALL: b.addStatement("%s.sendToAll(tag)", PACKET);
				case DIMENSION: b.addStatement("%s.sendToDimension(%s, tag)", PACKET, MethodHelper.getFirstParameter(method));
				case PLAYER: b.addStatement("%s.sendToPlayer(%s, %tag)", PACKET, MethodHelper.getFirstParameter(method));
				}
				clazz.addMethod(b.build());
			}
			
			JavaFile jf = JavaFile.builder(packageName, clazz.build())
					.build();
			jf.writeTo(System.out);
			jf.writeTo(filer);
		}
		catch(IOException e)
		{
			messager.printMessage(Kind.ERROR, e.getMessage());
		}
	}

	@Override
	public Set<String> getSupportedAnnotationTypes()
	{
		return new HashSet<String>(Arrays.asList(CommonProxy.class.getName()));
	}
}
