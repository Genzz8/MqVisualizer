package it.nexi.MqVisualizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class Test {

	
public static void main(String[] args) throws IOException, InterruptedException {
	
	ProcessBuilder builder = new ProcessBuilder();
	builder.command("sh", "-c", "mv -v test\\ \\ \\ \\  ../2/");
	builder.directory(new File("/home/vincenzo/copytest/1"));
	Process process = builder.start();
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	System.out.println(reader.readLine());
	
	int exitCod = process.waitFor();
	if(exitCod!=0) {
		throw new IOException("aa");
	}
	
//	StreamGobbler streamer = new StreamGobbler(process.getInputStream(), System.out::println);
//	Executors.newSingleThreadExecutor().submit(streamer);
//	int exitCode = process.waitFor();
//	assert exitCode == 0;

}
	

private static class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;
 
    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }
 
    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines()
          .forEach(consumer);
    }
}
}
