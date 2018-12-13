import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Command {
    private final String[] command;
    private String output;

    public static Command of(String command) {
        return new Command(command);
    }

    private Command(String command) {
        this.command = String.format("cmd /c \"%s\"", command).split("\\s");
    }

    void run(String workingDir) throws InterruptedException, IOException {
        Process proc = new ProcessBuilder(this.command)
                .directory(new File(workingDir))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();
        proc.waitFor();
        output = new BufferedReader(new InputStreamReader(proc.getInputStream())).lines().collect(Collectors.joining());
    }
}
