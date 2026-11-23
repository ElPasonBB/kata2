package software.ulpgc.kata2;

import software.ulpgc.kata2.application.domain.Videogame;
import software.ulpgc.kata2.application.ports.DataWriter;
import software.ulpgc.kata2.application.ports.ItemLoader;
import software.ulpgc.kata2.application.usecases.CalculateYearlySalesUseCase;
import software.ulpgc.kata2.infrastructure.adapters.StreamDataWriter;
import software.ulpgc.kata2.infrastructure.adapters.StreamItemLoader;

import java.io.*;
import java.net.URL;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ItemLoader loader = new StreamItemLoader();

        DataWriter consoleWriter = new StreamDataWriter(System.out);
        DataWriter fileWriter;
        DataWriter emptyFileWriter;

        List<Videogame> CSVVideogames;
        List<Videogame> URLVideogames;
        List<Videogame> emptyVideogames;

        CalculateYearlySalesUseCase calculateFile;
        CalculateYearlySalesUseCase calculateEmptyFile;

        try (InputStream CSVInput =  new FileInputStream("data/vgsales.csv")) {
            CSVVideogames = loader.load(CSVInput);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el InputStream del fichero", e);
        }

        try (InputStream URLInput = new URL("https://raw.githubusercontent.com/ElPasonBB/videogames-dataset-url-test/refs/heads/main/vgsales.csv").openStream()) {
            URLVideogames = loader.load(URLInput);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el InputStream de la URL", e);
        }

        try (InputStream emptyInput =  new FileInputStream("data/empty.csv")) {
            emptyVideogames = loader.load(emptyInput);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el InputStream del fichero", e);
        }

        try (InputStream emptyInput =  new FileInputStream("data/malformed.csv")) {
            loader.load(emptyInput);
        } catch (RuntimeException e) {
            System.out.println("Fila mal formada: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (InputStream URLInput = new URL("https://cualquier.cosa.nose/hola.txt").openStream()) {
            URLVideogames = loader.load(URLInput);
        } catch (IOException e) {
            System.out.println("URL vacía: " + e.getMessage());
        }

        CalculateYearlySalesUseCase calculateConsole = new CalculateYearlySalesUseCase(consoleWriter);
        calculateConsole.execute(CSVVideogames, 2006);
        calculateConsole.execute(URLVideogames, 2006);

        try (OutputStream fileOutput = new FileOutputStream("data/summary.txt")) {
            fileWriter = new StreamDataWriter(fileOutput);
            calculateFile = new CalculateYearlySalesUseCase(fileWriter);
            calculateFile.execute(CSVVideogames, 2006);
            calculateFile.execute(URLVideogames, 2006);

        } catch (IOException e) {
            throw new RuntimeException("Error al crear el OutputStream del fichero", e);
        }

        try (OutputStream fileOutput = new FileOutputStream("data/empty_summary.txt")) {
            emptyFileWriter = new StreamDataWriter(fileOutput);
            calculateEmptyFile = new CalculateYearlySalesUseCase(emptyFileWriter);
            calculateEmptyFile.execute(emptyVideogames, 2006);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el OutputStream del fichero", e);
        }
    }
}