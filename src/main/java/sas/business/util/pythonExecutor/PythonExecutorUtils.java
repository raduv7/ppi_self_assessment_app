package sas.business.util.pythonExecutor;

import com.sun.jdi.InternalException;
import org.hibernate.CallbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PythonExecutorUtils {
    private static final Logger log = LoggerFactory.getLogger(PythonExecutorUtils.class);

    public static void runPython(String pythonScriptPath, List<String> inputFilePaths, String outputFilePath) {
        try {
            List<String> command = new ArrayList<>();
            command.add("python");
            command.add(pythonScriptPath);
            command.add(String.valueOf(inputFilePaths.size()));
            command.addAll(inputFilePaths);
            command.add(outputFilePath);

            ProcessBuilder pb = new ProcessBuilder(command);
            log.info(pb.command().toString());
            Process p = pb.start();
            if(p.waitFor() != 0) {
                throw new CallbackException("Sir, python script failed.");
            }
        }
        catch (Exception e) {
            throw new InternalException("Error while running AI model.");
        }
    }
}
