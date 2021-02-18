package com.zte.msg.alarmcenter.core.javac;

import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.JavaCodecUtils;
import com.zte.msg.alarmcenter.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.tools.*;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/31 17:35
 */
@Component
@Slf4j
public class CodeJavac {

    @Resource
    private ProviderMapper providerMapper;

    public static Map<String, byte[]> allBuffers;

    private PcClassLoader pcClassLoader;
    private JavaCompiler javaCompiler;
    private ScriptFileManager scriptFileManager;
    private StringWriter errorStringWriter;
    private List<String> options;

    @PostConstruct
    public void init() {
        List<ScriptModel> scripts = providerMapper.selectScripts();
        String projectPath = PathUtil.getAppHomePath();
        String classPath = String.format("%s/pc-script/target/pc-script-1.0.0-jar-with-dependencies.jar", projectPath);
        log.info("classpath: {}", classPath);
        options = new ArrayList<>();
        options.add("-classpath");
        options.add(classPath);
        javaCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
        scriptFileManager = new ScriptFileManager(standardJavaFileManager);
        errorStringWriter = new StringWriter();
        getTask(scripts);
        log.info("========== all compilation script : {} ==========", scripts.size());
    }

    private void getTask(List<ScriptModel> scriptModels) {
        getTask(scriptModels, false);
    }

    private void getTask(List<ScriptModel> scriptModels, boolean throwException) {

        List<PcClassLoader.ScriptName> classInfos = new ArrayList<>(scriptModels.size());
        scriptModels.forEach(o -> {
            String suffix = String.valueOf(RandomUtils.nextInt());
            String className = o.getScriptTag() + Constants.UNDER_LINE + suffix;
            if (StringUtils.isNotBlank(o.getScriptContext()) && StringUtils.isNotBlank(o.getScriptTag())) {
                scriptFileManager.remove(o.getScriptTag());
                Iterable<? extends JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>() {{
                    add(new JavaSourceFromString(className, JavaCodecUtils.replaceCodeJavaName(o.getScriptContext(), className)));
                }};
                boolean ok = javaCompiler.getTask(errorStringWriter, scriptFileManager, diagnostic -> {
                    if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                        errorStringWriter.append(diagnostic.toString());
                    }
                }, options, null, compilationUnits).call();
                if (!ok) {
                    String errorMessage = errorStringWriter.toString();
                    log.error("Compile Error:{}" + errorMessage);
                    if (throwException) {
                        throw new CommonException(ErrorCode.SCRIPT_COMPILE_ERROR);
                    }
                }
                classInfos.add(new PcClassLoader.ScriptName(o.getScriptTag(), suffix));
            }

        });
        if (Objects.isNull(pcClassLoader)) {
            pcClassLoader = new PcClassLoader();
        }
        allBuffers = scriptFileManager.getAllBuffers();
        pcClassLoader.flushClazzInfo(classInfos);

    }

    public void scriptFlush(boolean remove, List<ScriptModel> scripts) {
        if (!remove) {
            getTask(scripts, true);
            log.info("========== update script completed, update count: {} ==========", scripts.size());
        } else {
            scripts.forEach(o -> scriptFileManager.remove(o.getScriptTag()));
            log.info("========== delete script completed, delete count: {} ==========", scripts.size());
        }
    }

    public void scriptFlush(List<ScriptModel> o) {
        scriptFlush(false, o);
    }

    public Class<?> getScriptClass(String scriptTag) {
        if (Objects.isNull(pcClassLoader)) {
            pcClassLoader = new PcClassLoader();
        }
        log.info("Take script :{}", scriptTag);
        Class<?> aClass = pcClassLoader.findClass(scriptTag);
        if (Objects.isNull(aClass)) {
            log.error("ClassNotFoundException: {}", scriptTag);
        }
        return aClass;
    }

}
