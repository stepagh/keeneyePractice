package dev.keeneye.dto;

import java.util.List;

public record CsvProcessingResult(
        int successCount,
        List<String> errors
) {}