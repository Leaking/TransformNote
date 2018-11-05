package com.quinn.transformnote.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.google.common.collect.ImmutableSet;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by quinn on 2018/10/30
 */
@SuppressWarnings("ALL")
/**
 * 1、Transform的工作:主要是负责将输入的class（可能来自于class文件和jar文件）运输给下一个Transform，运输过程中
 * 你可以对这些class动动手脚，改改字节码。而Transform的输出路径，通过outputProvider获取
 * 2、Transform输入的来源可以通过Scope的概念指定，
 * 3、Transform输入的具体文件类型可以通过ContentType指定
 * 4、Transform可以指定是否支持增量编译，如果增量编译，每次编译，Android编译系统会告诉当前Transform目前哪些文件发生了变化，以及发生
 * 什么变化。
 * 5、Transform的每个输入，并不一定要输出到下一个Transform，它也可以只获取输入，而不输出。输入其实分为两种，一种是消费型输入，需要输出到下个Transform，一种是引用型输入，
 * getScope方法返回的就是消费型输入，getReferencedScopes方法返回的就是引用型输入。
 */
public class CustomTransform extends Transform {

    public static final String TAG = "CustomTransform";

    public CustomTransform() {
        super();
    }

    @Override
    public String getName() {
        return "CustomTransform";
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        LogUtils.log(TAG, "inputs Size " + inputs.size());
        LogUtils.log(TAG, "referencedInputs Size " + referencedInputs.size());
        //scope为空，即使referenceScope不是空，outputProvider就会为NULL
        LogUtils.log(TAG, "outputProvider " + outputProvider);
        for(TransformInput input : inputs) {
            LogUtils.log(TAG, input.toString());
            for(JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                LogUtils.log(TAG, "jarInput " + jarInput.getFile().getAbsolutePath());
                FileUtils.copyFile(jarInput.getFile(), dest);
            }
            for(DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY);
                LogUtils.log(TAG, "directoryInput " + directoryInput.getFile().getAbsolutePath());
                FileUtils.copyDirectory(directoryInput.getFile(), dest);
            }

        }

    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_JARS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return ImmutableSet.of(QualifiedContent.Scope.PROJECT);
    }

    @Override
    public Set<QualifiedContent.ContentType> getOutputTypes() {
        return super.getOutputTypes();
    }

    @Override
    public Set<? super QualifiedContent.Scope> getReferencedScopes() {
        return TransformManager.EMPTY_SCOPES;
    }


    @Override
    public Map<String, Object> getParameterInputs() {
        return super.getParameterInputs();
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

}
