package software.ulpgc.kata2.application.ports;

import software.ulpgc.kata2.application.domain.Videogame;

import java.io.InputStream;
import java.util.List;

public interface ItemLoader {
    List<Videogame> load(InputStream input);
}