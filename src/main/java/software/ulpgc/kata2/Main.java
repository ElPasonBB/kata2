package software.ulpgc.kata2;

import software.ulpgc.kata2.application.domain.Videogame;
import software.ulpgc.kata2.application.ports.DataWriter;
import software.ulpgc.kata2.application.ports.ItemLoader;
import software.ulpgc.kata2.application.usecases.CalculateYearlySalesUseCase;
import software.ulpgc.kata2.infrastructure.adapters.StreamDataWriter;
import software.ulpgc.kata2.infrastructure.adapters.StreamItemLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ItemLoader loader = new StreamItemLoader();

        DataWriter consoleWriter = new StreamDataWriter(System.out);
        DataWriter fileWriter;
        DataWriter emptyFileWriter;

        List<Videogame> CSVVideogames;
        List<Videogame> byteVideogames;
        List<Videogame> emptyVideogames;

        CalculateYearlySalesUseCase calculateFile;
        CalculateYearlySalesUseCase calculateEmptyFile;

        String testCSV = """
                Rank,Name,Platform,Year,Genre,Publisher,NA_Sales,EU_Sales,JP_Sales,Other_Sales,Global_Sales
                1,Wii Sports,Wii,2006,Sports,Nintendo,41.49,29.02,3.77,8.46,82.74
                2,New Super Mario Bros.,DS,2006,Platform,Nintendo,11.38,9.23,6.5,2.9,30.01
                """;

        try (InputStream CSVInput = new FileInputStream("data/vgsales.csv")) {
            CSVVideogames = loader.load(CSVInput);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el InputStream del fichero", e);
        }

        try (InputStream byteInput = new ByteArrayInputStream(testCSV.getBytes(StandardCharsets.UTF_8))) {
            byteVideogames = loader.load(byteInput);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el ByteArrayInputStream", e);
        }

        try (InputStream emptyInput = new FileInputStream("data/empty.csv")) {
            emptyVideogames = loader.load(emptyInput);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el InputStream del fichero", e);
        }

        try (InputStream malformedInput = new FileInputStream("data/malformed.csv")) {
            loader.load(malformedInput);
        } catch (RuntimeException e) {
            System.out.println("Fila mal formada: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (InputStream emptyInput = new FileInputStream("data/noexisto.csv")) {
            loader.load(emptyInput);
        } catch (IOException e) {
            System.out.println("Ruta inexistente: " + e.getMessage());
        }

        CalculateYearlySalesUseCase calculateConsole = new CalculateYearlySalesUseCase(consoleWriter);
        calculateConsole.execute(CSVVideogames, 2006);
        calculateConsole.execute(byteVideogames, 2006);

        try (OutputStream fileOutput = new FileOutputStream("data/summary.txt")) {
            fileWriter = new StreamDataWriter(fileOutput);
            calculateFile = new CalculateYearlySalesUseCase(fileWriter);
            calculateFile.execute(CSVVideogames, 2006);
            calculateFile.execute(byteVideogames, 2006);
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