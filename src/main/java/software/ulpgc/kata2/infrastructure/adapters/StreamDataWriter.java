package software.ulpgc.kata2.infrastructure.adapters;

import software.ulpgc.kata2.application.ports.DataWriter;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class StreamDataWriter implements DataWriter {
    private final OutputStream output;

    public StreamDataWriter(OutputStream output) {
        this.output = output;
    }

    @Override
    public void write(String message) {
        OutputStreamWriter streamWriter = new OutputStreamWriter(this.output) ;
        PrintWriter printer = new PrintWriter(streamWriter);
        printer.println(message);
        printer.flush();
    }
}
