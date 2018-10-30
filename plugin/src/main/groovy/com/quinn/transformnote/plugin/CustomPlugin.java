package com.quinn.transformnote.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;


public class CustomPlugin implements Plugin<Project> {



    @SuppressWarnings("NullableProblems")
    @Override
    public void apply(Project project) {
        LogUtils.log("apply CustomPlugin");
        AppExtension appExtension = (AppExtension)project.getProperties().get("android");
        appExtension.registerTransform(new CustomTransform(), Collections.EMPTY_LIST);

    }

}