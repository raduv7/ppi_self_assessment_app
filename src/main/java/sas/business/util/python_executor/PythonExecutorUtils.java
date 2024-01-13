package sas.business.util.python_executor;

import com.sun.jdi.InternalException;
import org.hibernate.CallbackException;

import java.util.ArrayList;
import java.util.List;

public class PythonExecutorUtils {
    public static void runPython(String pythonScriptPath, List<String> inputFilePaths, String outputFilePath) {
        try {
            List<String> command = new ArrayList<>();
            command.add("python");
            command.add(pythonScriptPath);
            command.add(String.valueOf(inputFilePaths.size()));
            command.addAll(inputFilePaths);
            command.add(outputFilePath);

            ProcessBuilder pb = new ProcessBuilder(command);
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
