package software.ulpgc.kata2.infrastructure.adapters;

import software.ulpgc.kata2.application.domain.Videogame;
import software.ulpgc.kata2.application.ports.ItemLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class StreamItemLoader implements ItemLoader {
    @Override
    public List<Videogame> load(InputStream input) {
        try {
            InputStreamReader streamReader = new InputStreamReader(input);
            BufferedReader buffer = new BufferedReader(streamReader);
            return buffer.lines()
                    .skip(1)
                    .map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                    .filter(split -> ! split[3].equals("N/A"))
                    .map(split -> new Videogame(
                            Integer.parseInt(split[0]),
                            split[1],
                            Integer.parseInt(split[3]),
                            Double.parseDouble(split[10])
                    ))
                    .toList();
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid row format in dataset", e);
        }
    }
}