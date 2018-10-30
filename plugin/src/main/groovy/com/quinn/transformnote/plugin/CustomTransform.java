package com.quinn.transformnote.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.SecondaryFile;
import com.android.build.api.transform.SecondaryInput;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by quinn on 2018/10/30
 */
public class CustomTransform extends Transform {


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
        Collection<SecondaryInput> secondaryInputs = transformInvocation.getSecondaryInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        LogUtils.log("inputs Size " + inputs.size());
        LogUtils.log("referencedInputs Size " + referencedInputs.size());
        LogUtils.log("secondaryInputs Size " + secondaryInputs.size());
        for(TransformInput input : inputs) {
            LogUtils.log(input.toString());
            for(JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                LogUtils.log("jarInput " + jarInput.getFile().getAbsolutePath());
                FileUtils.copyFile(jarInput.getFile(), dest);
            }
            for(DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY);
                LogUtils.log("directoryInput " + directoryInput.getFile().getAbsolutePath());
                FileUtils.copyDirectory(directoryInput.getFile(), dest);
            }
        }


    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_RESOURCES;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public Set<QualifiedContent.ContentType> getOutputTypes() {
        return super.getOutputTypes();
    }

    @Override
    public Set<? super QualifiedContent.Scope> getReferencedScopes() {
        return super.getReferencedScopes();
    }

    @Override
    public Collection<SecondaryFile> getSecondaryFiles() {
        return super.getSecondaryFiles();
    }

    @Override
    public Collection<File> getSecondaryFileOutputs() {
        return super.getSecondaryFileOutputs();
    }

    @Override
    public Collection<File> getSecondaryDirectoryOutputs() {
        return super.getSecondaryDirectoryOutputs();
    }

    @Override
    public Map<String, Object> getParameterInputs() {
        return super.getParameterInputs();
    }

    @Override
    public boolean isCacheable() {
        return true;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }
}
