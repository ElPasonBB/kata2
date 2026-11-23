package software.ulpgc.kata2.application.usecases;

import software.ulpgc.kata2.application.domain.Videogame;
import software.ulpgc.kata2.application.ports.DataWriter;

import java.util.List;

public class CalculateYearlySalesUseCase {
    private final DataWriter writer;

    public CalculateYearlySalesUseCase(DataWriter writer) {
        this.writer = writer;
    }

    public void execute(List<Videogame> videogameList, int year) {
        double sales = videogameList.stream().filter(v -> v.year() == year).mapToDouble(Videogame::globalSales).sum();
        this.writer.write(sales + "M total sales on year " + year);
    }
}