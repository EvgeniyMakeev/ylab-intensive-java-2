package dev.makeev.training_diary_app.model;

import java.time.LocalDate;
import java.util.List;

public record Statistic(LocalDate from,
                        LocalDate to,
                        Double minValue,
                        Double averageValue,
                        Double maxValue,
                        Double totalValue,
                        List<Training> valuesList) {
}
